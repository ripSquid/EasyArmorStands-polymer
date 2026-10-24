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
package re.koda.easyarmorstands.group.tool;

import re.koda.easyarmorstands.api.Axis;
import re.koda.easyarmorstands.api.editor.Snapper;
import re.koda.easyarmorstands.api.editor.tool.AxisMoveTool;
import re.koda.easyarmorstands.api.editor.tool.AxisMoveToolSession;
import re.koda.easyarmorstands.api.editor.tool.ToolContext;
import re.koda.easyarmorstands.message.Message;
import re.koda.easyarmorstands.platform.entity.Player;
import re.koda.easyarmorstands.util.Util;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaterniondc;
import org.joml.Vector3dc;

import java.util.ArrayList;
import java.util.List;

public class GroupAxisMoveTool implements AxisMoveTool {
    private final ToolContext context;
    private final Axis axis;
    private final List<AxisMoveTool> tools;

    public GroupAxisMoveTool(ToolContext context, Axis axis, List<AxisMoveTool> tools) {
        this.context = context;
        this.axis = axis;
        this.tools = new ArrayList<>(tools);
    }

    @Override
    public @NotNull Vector3dc getPosition() {
        return context.position().getPosition();
    }

    @Override
    public @NotNull Quaterniondc getRotation() {
        return context.rotation().getRotation();
    }

    @Override
    public @NotNull Axis getAxis() {
        return axis;
    }

    @Override
    public @NotNull AxisMoveToolSession start() {
        return new SessionImpl();
    }

    @Override
    public boolean canUse(@NotNull Player player) {
        for (AxisMoveTool tool : tools) {
            if (tool.canUse(player)) {
                return true;
            }
        }
        return false;
    }

    private class SessionImpl extends GroupScalarToolSession<AxisMoveToolSession> implements AxisMoveToolSession {
        private double change;

        private SessionImpl() {
            super(tools);
        }

        @Override
        public void setChange(double change) {
            this.change = change;
            for (AxisMoveToolSession session : sessions) {
                session.setChange(change);
            }
        }

        @Override
        public double snapChange(double change, @NotNull Snapper context) {
            return context.snapOffset(change);
        }

        @Override
        public @Nullable Component getStatus() {
            return Component.text(Util.OFFSET_FORMAT.format(change));
        }

        @Override
        public @Nullable Component getDescription() {
            Component count = Component.text(sessions.size());
            Component axisName = Component.text(axis.getName(), TextColor.color(axis.getColor()));
            return Message.component("easyarmorstands.history.group.move-axis", count, axisName);
        }
    }
}
