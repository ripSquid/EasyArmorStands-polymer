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
import re.koda.easyarmorstands.api.editor.tool.AxisRotateTool;
import re.koda.easyarmorstands.api.particle.CircleParticle;
import re.koda.easyarmorstands.api.particle.ParticleColor;
import re.koda.easyarmorstands.util.Util;
import org.joml.Quaterniond;
import org.joml.Vector3d;
import org.joml.Vector3dc;

import java.util.function.Consumer;

public class AxisRotateButton implements Button {
    private final Session session;
    private final AxisRotateTool tool;
    private final Vector3d position = new Vector3d();
    private final Quaterniond rotation = new Quaterniond();
    private final Vector3d direction = new Vector3d();
    private final CircleParticle particle;
    private final double radius;
    private final ParticleColor color;
    private double scale;
    private Axis axis;

    public AxisRotateButton(Session session, AxisRotateTool tool, double radius, ParticleColor color) {
        this.session = session;
        this.tool = tool;
        this.particle = session.particleProvider().createCircle();
        this.radius = radius;
        this.color = color;
        this.axis = tool.getAxis();
    }

    @Override
    public void update() {
        position.set(tool.getPosition());
        rotation.set(tool.getRotation());
        scale = session.getScale(position);
        axis = tool.getAxis();
        axis.getDirection().rotate(rotation, direction);
    }

    @Override
    public void intersect(EyeRay ray, Consumer<ButtonResult> results) {
        Vector3dc intersection = ray.intersectCircle(position, direction, radius * scale, scale);
        if (intersection != null) {
            results.accept(ButtonResult.of(intersection));
        }
    }

    @Override
    public void updatePreview(boolean focused, boolean selected) {
        particle.setCenter(position);
        particle.setRotation(rotation);
        particle.setAxis(axis);
        particle.setColor(focused ? ParticleColor.YELLOW : color);
        particle.setRadius(radius * scale);
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
}
