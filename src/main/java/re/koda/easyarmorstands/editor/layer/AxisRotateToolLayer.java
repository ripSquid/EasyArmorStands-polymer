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
package re.koda.easyarmorstands.editor.layer;

import re.koda.easyarmorstands.api.Axis;
import re.koda.easyarmorstands.api.editor.Session;
import re.koda.easyarmorstands.api.editor.context.EnterContext;
import re.koda.easyarmorstands.api.editor.context.ExitContext;
import re.koda.easyarmorstands.api.editor.context.UpdateContext;
import re.koda.easyarmorstands.api.editor.tool.AxisRotateToolSession;
import re.koda.easyarmorstands.api.particle.CircleParticle;
import re.koda.easyarmorstands.api.particle.LineParticle;
import re.koda.easyarmorstands.api.particle.ParticleColor;
import re.koda.easyarmorstands.platform.command.CommandSender;
import re.koda.easyarmorstands.util.Cursor2D;
import re.koda.easyarmorstands.util.Util;
import net.kyori.adventure.text.Component;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.parser.standard.DoubleParser;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaterniondc;
import org.joml.Vector3d;
import org.joml.Vector3dc;

public class AxisRotateToolLayer extends ToolLayer implements ValueLayer<Double> {
    private final Session session;
    private final AxisRotateToolSession toolSession;
    private final double radius;
    private final double length;
    private final CircleParticle circleParticle;
    private final LineParticle axisParticle;
    private final LineParticle cursorLineParticle;
    private final Cursor2D cursor;
    private final Vector3dc position;
    private final Vector3dc direction;
    private final Vector3d initialOffset = new Vector3d();
    private final Vector3d currentOffset = new Vector3d();
    private final Vector3d snappedCursor = new Vector3d();
    private boolean valid;
    private boolean hasManualInput;

    public AxisRotateToolLayer(Session session, AxisRotateToolSession toolSession, double radius, double length, Component name, ParticleColor color, Vector3dc position, Quaterniondc rotation, Axis axis) {
        super(session, toolSession, name);
        this.session = session;
        this.toolSession = toolSession;
        this.radius = radius;
        this.length = length;
        double scale = session.getScale(position);
        this.circleParticle = session.particleProvider().createCircle();
        this.circleParticle.setColor(color);
        this.circleParticle.setRadius(radius * scale);
        this.circleParticle.setCenter(position);
        this.circleParticle.setRotation(rotation);
        this.circleParticle.setAxis(axis);
        this.circleParticle.setWidth(Util.LINE_WIDTH * scale);
        this.axisParticle = session.particleProvider().createLine();
        this.axisParticle.setColor(color);
        this.axisParticle.setLength(length * scale);
        this.axisParticle.setCenter(position);
        this.axisParticle.setRotation(rotation);
        this.axisParticle.setAxis(axis);
        this.axisParticle.setWidth(Util.LINE_WIDTH * scale);
        this.cursorLineParticle = session.particleProvider().createLine();
        this.cursor = new Cursor2D(session);
        this.position = new Vector3d(position);
        this.direction = axis.getDirection().rotate(rotation, new Vector3d());
    }

    @Override
    public void onEnter(@NotNull EnterContext context) {
        cursor.start(context, position, context.cursorOrDefault(position), direction);
        cursorLineParticle.setFromTo(position, cursor.get());
        session.addParticle(circleParticle);
        session.addParticle(axisParticle);
        session.addParticle(cursorLineParticle);
        valid = false;
        hasManualInput = false;
    }

    @Override
    public void onExit(@NotNull ExitContext context) {
        super.onExit(context);
        cursor.stop();
        session.removeParticle(cursorLineParticle);
        session.removeParticle(axisParticle);
        session.removeParticle(circleParticle);
    }

    @Override
    public void onUpdate(@NotNull UpdateContext context) {
        cursor.update(context);

        double scale = session.getScale(position);
        circleParticle.setRadius(radius * scale);
        circleParticle.setWidth(Util.LINE_WIDTH * scale);
        axisParticle.setLength(length * scale);
        axisParticle.setWidth(Util.LINE_WIDTH * scale);
        cursorLineParticle.setWidth(Util.LINE_WIDTH * scale);

        if (!hasManualInput) {
            Vector3dc cursorPosition = cursor.get();
            cursorPosition.sub(position, currentOffset);

            double angle;
            if (valid) {
                angle = initialOffset.angleSigned(currentOffset, direction);
                angle = toolSession.snapChange(angle, session.snapper());
            } else {
                angle = 0;
                double minOffset = 0.2;
                if (currentOffset.lengthSquared() >= minOffset * minOffset) {
                    initialOffset.set(currentOffset);
                    valid = true;
                }
            }

            initialOffset.rotateAxis(angle, direction.x(), direction.y(), direction.z(), snappedCursor)
                    .normalize(currentOffset.length())
                    .add(position);

            toolSession.setChange(angle);
            cursorLineParticle.setFromTo(position, snappedCursor);
        }

        super.onUpdate(context);
    }

    @Override
    public Component formatValue(Double value) {
        return Util.formatDegrees(value);
    }

    @Override
    public ArgumentParser<CommandSender, Double> getParser() {
        return new DoubleParser<>(-360, 360);
    }

    @Override
    public boolean canSetValue() {
        return toolSession.canSetValue(session.player());
    }

    @Override
    public void setValue(Double value) {
        toolSession.setValue(Math.toRadians(value));
        hasManualInput = true;
        session.removeParticle(cursorLineParticle);
        cursor.stop();
    }
}
