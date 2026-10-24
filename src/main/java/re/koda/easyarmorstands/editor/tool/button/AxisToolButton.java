/*
 * EasyArmorStands-Polymer - a Polymer based Fabric port of EasyArmorStands
 * Copyright (C) 56738 and EasyArmorStands contributors (original work)
 * Copyright (C) 2026 ripsquid (modifications)
 *
 * This file is derived from EasyArmorStands (https://github.com/56738/EasyArmorStands),
 * commit c589944e23b49170fb7380742fe755c392f1e0c7, and was modified on 2026-10-22
 * (package relocation and adaptation to the Polymer based Fabric port).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package re.koda.easyarmorstands.editor.tool.button;

import re.koda.easyarmorstands.api.Axis;
import re.koda.easyarmorstands.api.editor.EyeRay;
import re.koda.easyarmorstands.api.editor.Session;
import re.koda.easyarmorstands.api.editor.button.Button;
import re.koda.easyarmorstands.api.editor.button.ButtonResult;
import re.koda.easyarmorstands.api.editor.tool.AxisTool;
import re.koda.easyarmorstands.api.particle.LineParticle;
import re.koda.easyarmorstands.api.particle.ParticleColor;
import re.koda.easyarmorstands.util.Util;
import org.joml.Quaterniond;
import org.joml.Quaterniondc;
import org.joml.Vector3d;
import org.joml.Vector3dc;

import java.util.function.Consumer;

public class AxisToolButton implements Button {
    private final Session session;
    private final AxisTool<?> tool;
    private final Vector3d position = new Vector3d();
    private final Quaterniond rotation = new Quaterniond();
    private final Vector3d direction = new Vector3d();
    private final Vector3d negativeEnd = new Vector3d();
    private final Vector3d positiveEnd = new Vector3d();
    private final LineParticle particle;
    private final double length;
    private final ParticleColor color;
    private double scale;
    private Axis axis;

    public AxisToolButton(Session session, AxisTool<?> tool, double length, ParticleColor color) {
        this.session = session;
        this.tool = tool;
        this.length = length;
        this.particle = session.particleProvider().createLine();
        this.color = color;
        this.axis = tool.getAxis();
    }

    @Override
    public void update() {
        position.set(tool.getPosition());
        rotation.set(tool.getRotation());
        axis = tool.getAxis();
        axis.getDirection().rotate(rotation, direction);
        scale = session.getScale(position);
        position.fma(-length / 2 * scale, direction, negativeEnd);
        position.fma(length / 2 * scale, direction, positiveEnd);
    }

    @Override
    public void intersect(EyeRay ray, Consumer<ButtonResult> results) {
        Vector3dc intersection = ray.intersectLine(negativeEnd, positiveEnd, scale);
        if (intersection != null) {
            results.accept(ButtonResult.of(intersection));
        }
    }

    @Override
    public void updatePreview(boolean focused, boolean selected) {
        particle.setCenter(position);
        particle.setRotation(rotation);
        particle.setAxis(axis);
        particle.setColor(Button.color(focused, selected, color));
        particle.setLength(length * scale);
        particle.setWidth(Util.LINE_WIDTH * scale);
    }

    @Override
    public void showPreview() {
        session.addParticle(particle);
    }

    @Override
    public void hidePreview() {
        session.removeParticle(particle);
    }

    public Session getSession() {
        return session;
    }

    public Vector3dc getPosition() {
        return position;
    }

    public Quaterniondc getRotation() {
        return rotation;
    }

    public Axis getAxis() {
        return axis;
    }

    public ParticleColor getColor() {
        return color;
    }

    public double getLength() {
        return length;
    }
}
