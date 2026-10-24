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
package re.koda.easyarmorstands.api.editor.tool;

import re.koda.easyarmorstands.api.Axis;
import re.koda.easyarmorstands.api.editor.Snapper;
import re.koda.easyarmorstands.platform.entity.Player;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaterniondc;
import org.joml.Vector3d;
import org.joml.Vector3dc;

import java.text.DecimalFormat;
import java.text.NumberFormat;

class SimpleAxisMoveTool implements AxisMoveTool {
    public static final NumberFormat OFFSET_FORMAT = new DecimalFormat("+0.0000;-0.0000");
    private final MoveTool moveTool;
    private final Axis axis;

    SimpleAxisMoveTool(MoveTool moveTool, Axis axis) {
        this.moveTool = moveTool;
        this.axis = axis;
    }

    @Override
    public @NotNull Vector3dc getPosition() {
        return moveTool.getPosition();
    }

    @Override
    public @NotNull Quaterniondc getRotation() {
        return moveTool.getRotation();
    }

    @Override
    public @NotNull Axis getAxis() {
        return axis;
    }

    @Override
    public @NotNull AxisMoveToolSession start() {
        return new AxisMoveToolSessionImpl(moveTool.start());
    }

    @Override
    public boolean canUse(@NotNull Player player) {
        return moveTool.canUse(player);
    }

    private class AxisMoveToolSessionImpl implements AxisMoveToolSession {
        private final MoveToolSession moveToolSession;
        private final Vector3dc direction;
        private double change;

        public AxisMoveToolSessionImpl(MoveToolSession moveToolSession) {
            this.moveToolSession = moveToolSession;
            this.direction = axis.getDirection().rotate(getRotation(), new Vector3d());
        }

        @Override
        public void setChange(double change) {
            this.change = change;
            moveToolSession.setChange(direction.mul(change, new Vector3d()));
        }

        @Override
        public double snapChange(double change, @NotNull Snapper context) {
            return context.snapOffset(change);
        }

        @Override
        public void revert() {
            moveToolSession.revert();
        }

        @Override
        public void commit(@Nullable Component description) {
            moveToolSession.commit(description);
        }

        @Override
        public boolean isValid() {
            return moveToolSession.isValid();
        }

        @Override
        public @Nullable Component getStatus() {
            return Component.text(OFFSET_FORMAT.format(change));
        }

        @Override
        public @Nullable Component getDescription() {
            return moveToolSession.getDescription();
        }

        @Override
        public boolean canSetValue(Player player) {
            return moveToolSession.canSetPosition(player);
        }
    }
}
