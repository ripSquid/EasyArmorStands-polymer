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

import re.koda.easyarmorstands.api.editor.Session;
import re.koda.easyarmorstands.api.editor.context.EnterContext;
import re.koda.easyarmorstands.api.editor.context.ExitContext;
import re.koda.easyarmorstands.api.editor.context.UpdateContext;
import re.koda.easyarmorstands.api.editor.tool.ScaleToolSession;
import re.koda.easyarmorstands.api.particle.LineParticle;
import re.koda.easyarmorstands.api.particle.ParticleColor;
import re.koda.easyarmorstands.editor.input.tool.ActivateInput;
import re.koda.easyarmorstands.message.Message;
import re.koda.easyarmorstands.platform.command.CommandSender;
import re.koda.easyarmorstands.util.Cursor3D;
import re.koda.easyarmorstands.util.Util;
import net.kyori.adventure.text.Component;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.parser.standard.DoubleParser;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector3dc;

public class ScaleToolLayer extends ToolLayer implements ValueLayer<Double> {
    private static final double ACTIVATION_DISTANCE = 0.2;
    protected final Session session;
    protected final ScaleToolSession toolSession;
    private final Vector3dc position;
    private final ParticleColor color;
    private final Cursor3D cursor;
    private final Vector3d delta = new Vector3d();
    private final LineParticle cursorLineParticle;
    private double distance;
    private double initialDistance;
    private boolean canActivate;
    private boolean active;
    private boolean hasManualInput;

    public ScaleToolLayer(Session session, ScaleToolSession toolSession, Component name, ParticleColor color, Vector3dc position) {
        super(session, toolSession, name);
        this.session = session;
        this.toolSession = toolSession;
        this.cursorLineParticle = session.particleProvider().createLine();
        this.position = new Vector3d(position);
        this.color = color;
        this.cursor = new Cursor3D(session);
    }

    public void activate() {
        initialDistance = distance;
        active = true;
        canActivate = false;
        cursorLineParticle.setColor(color);
    }

    @Override
    public void onEnter(@NotNull EnterContext context) {
        cursor.start(context, context.cursorOrDefault(() -> context.eyeRay().point(1)));
        distance = cursor.get().distance(position);

        cursorLineParticle.setColor(ParticleColor.GRAY);
        cursorLineParticle.setFromTo(position, cursor.get());

        session.addParticle(cursorLineParticle);
    }

    @Override
    public void onUpdate(@NotNull UpdateContext context) {
        if (hasManualInput) {
            super.onUpdate(context);
            return;
        }

        cursor.update(context);
        cursor.get().sub(position, delta);
        distance = delta.length();

        if (active) {
            double change = toolSession.snapChange(distance / initialDistance, session.snapper());
            distance = change * initialDistance;
            toolSession.setChange(change);
            delta.normalize(distance); // adjust the length of delta to the snapped value
        } else {
            canActivate = distance > ACTIVATION_DISTANCE;
            cursorLineParticle.setColor(canActivate ? ParticleColor.GRAY : ParticleColor.RED);
        }

        if (canActivate) {
            context.addInput(new ActivateInput(this));
        }

        cursorLineParticle.setFromTo(position, delta.add(position));
        super.onUpdate(context);
    }

    @Override
    protected void updateActionBar(@NotNull UpdateContext context) {
        if (!active && !hasManualInput) {
            if (canActivate) {
                context.setActionBar(Message.component("easyarmorstands.node.scale.right-click"));
            } else {
                context.setActionBar(Message.error("easyarmorstands.node.scale.too-close"));
            }
            return;
        }
        super.updateActionBar(context);
    }

    @Override
    protected boolean canConfirm() {
        return active;
    }

    @Override
    public void onExit(@NotNull ExitContext context) {
        super.onExit(context);
        cursor.stop();
        session.removeParticle(cursorLineParticle);
    }

    @Override
    public Component formatValue(Double value) {
        return Component.text(Util.OFFSET_FORMAT.format(value));
    }

    @Override
    public ArgumentParser<CommandSender, Double> getParser() {
        return new DoubleParser<>(0, Double.POSITIVE_INFINITY);
    }

    @Override
    public boolean canSetValue() {
        return toolSession.canSetValue(session.player());
    }

    @Override
    public void setValue(Double value) {
        toolSession.setValue(value);
        hasManualInput = true;
        cursor.stop();
        session.removeParticle(cursorLineParticle);
    }
}
