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
import re.koda.easyarmorstands.api.editor.tool.ScalarToolSession;
import re.koda.easyarmorstands.api.particle.LineParticle;
import re.koda.easyarmorstands.api.particle.ParticleColor;
import re.koda.easyarmorstands.platform.command.CommandSender;
import re.koda.easyarmorstands.util.Cursor3D;
import re.koda.easyarmorstands.util.EasMath;
import re.koda.easyarmorstands.util.Util;
import net.kyori.adventure.text.Component;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.parser.standard.DoubleParser;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaterniond;
import org.joml.Quaterniondc;
import org.joml.Vector3d;
import org.joml.Vector3dc;

public abstract class AxisLineToolLayer extends ToolLayer implements ValueLayer<Double> {
    private final Session session;
    private final ScalarToolSession toolSession;
    private final double length;
    private final Vector3dc position;
    private final Quaterniondc rotation;
    private final Axis axis;
    private final Vector3dc direction;
    private final LineParticle particle;
    private final LineParticle cursorLineParticle;
    private final Cursor3D cursor;
    private final Vector3d initialCursor = new Vector3d();
    private final Vector3d temp = new Vector3d();
    private double initialOffset;
    private boolean hasManualInput;

    public AxisLineToolLayer(Session session, ScalarToolSession toolSession, Component name, ParticleColor color, double length, Vector3dc position, Quaterniondc rotation, Axis axis) {
        super(session, toolSession, name);
        this.session = session;
        this.toolSession = toolSession;
        this.length = length;
        this.position = new Vector3d(position);
        this.rotation = new Quaterniond(rotation);
        this.axis = axis;
        this.direction = axis.getDirection().rotate(rotation, new Vector3d());
        this.particle = session.particleProvider().createLine();
        this.particle.setColor(color);
        this.cursorLineParticle = session.particleProvider().createLine();
        this.cursor = new Cursor3D(session);
    }

    protected Vector3dc getDefaultCursor() {
        return position;
    }

    @Override
    public void onEnter(@NotNull EnterContext context) {
        initialCursor.set(context.cursorOrDefault(this::getDefaultCursor));
        initialOffset = EasMath.getOffsetAlongLine(position, direction, initialCursor);
        cursor.start(context, initialCursor);

        double scale = session.getScale(position);
        particle.setRotation(rotation);
        particle.setAxis(axis);

        updateParticles(initialOffset, scale);

        session.addParticle(particle);
        session.addParticle(cursorLineParticle);

        hasManualInput = false;
    }

    @Override
    public void onExit(@NotNull ExitContext context) {
        super.onExit(context);
        cursor.stop();
        session.removeParticle(cursorLineParticle);
        session.removeParticle(particle);
    }

    @Override
    public void onUpdate(@NotNull UpdateContext context) {
        cursor.update(context);

        double scale = session.getScale(EasMath.getClosestPointOnLine(position, direction, context.eyeRay().origin(), temp));

        if (!hasManualInput) {
            double offset = EasMath.getOffsetAlongLine(position, direction, cursor.get());
            double change = toolSession.snapChange(getChange(offset, initialOffset), session.snapper());
            toolSession.setChange(change);
            updateParticles(offset, scale);
        } else {
            updateParticleScales(scale);
        }

        super.onUpdate(context);
    }

    private void updateParticles(double offset, double scale) {
        double min = Math.min(0, offset) - length / 2 * scale;
        double max = Math.max(0, offset) + length / 2 * scale;
        particle.setCenter(position.fma(offset, direction, temp));
        particle.setLength(max - min);
        particle.setOffset(min - offset + (max - min) / 2);
        cursorLineParticle.setFromTo(position.fma(offset, direction, temp), cursor.get());
        updateParticleScales(scale);
    }

    private void updateParticleScales(double scale) {
        particle.setWidth(Util.LINE_WIDTH * scale);
        cursorLineParticle.setWidth(Util.LINE_WIDTH * scale);
    }

    protected abstract double getChange(double currentOffset, double initialOffset);

    @Override
    public Component formatValue(Double value) {
        return Component.text(Util.OFFSET_FORMAT.format(value));
    }

    @Override
    public ArgumentParser<CommandSender, Double> getParser() {
        return new DoubleParser<>(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    @Override
    public boolean canSetValue() {
        return toolSession.canSetValue(session.player());
    }

    @Override
    public void setValue(Double value) {
        toolSession.setValue(value);
        hasManualInput = true;
        session.removeParticle(cursorLineParticle);
        cursor.stop();
    }
}
