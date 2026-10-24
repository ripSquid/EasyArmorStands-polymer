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
import re.koda.easyarmorstands.api.editor.tool.AxisMoveTool;
import re.koda.easyarmorstands.api.editor.tool.AxisRotateTool;
import re.koda.easyarmorstands.api.editor.tool.MoveTool;
import re.koda.easyarmorstands.api.editor.tool.ToolContext;
import re.koda.easyarmorstands.api.editor.tool.ToolProvider;
import re.koda.easyarmorstands.api.util.PositionProvider;
import re.koda.easyarmorstands.api.util.RotationProvider;
import re.koda.easyarmorstands.group.Group;
import re.koda.easyarmorstands.group.GroupMember;
import re.koda.easyarmorstands.group.GroupPositionProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GroupToolProvider implements ToolProvider {
    private final Group group;
    private final PositionProvider positionProvider;

    public GroupToolProvider(Group group) {
        this.group = group;
        this.positionProvider = new GroupPositionProvider(group);
    }

    @Override
    public @NotNull PositionProvider position() {
        return positionProvider;
    }

    @Override
    public @NotNull RotationProvider rotation() {
        return RotationProvider.identity();
    }

    @Override
    public @Nullable MoveTool move(@NotNull ToolContext context) {
        List<MoveTool> tools = new ArrayList<>();
        for (GroupMember member : group.getMembers()) {
            MoveTool tool = member.getTools().move(context);
            if (tool == null) {
                return ToolProvider.super.move(context);
            }
            tools.add(tool);
        }
        return new GroupMoveTool(context, tools);
    }

    @Override
    public @Nullable AxisMoveTool move(@NotNull ToolContext context, @NotNull Axis axis) {
        List<AxisMoveTool> tools = new ArrayList<>();
        for (GroupMember member : group.getMembers()) {
            AxisMoveTool tool = member.getTools().move(context, axis);
            if (tool == null) {
                return ToolProvider.super.move(context, axis);
            }
            tools.add(tool);
        }
        return new GroupAxisMoveTool(context, axis, tools);
    }

    @Override
    public @Nullable AxisRotateTool rotate(@NotNull ToolContext context, @NotNull Axis axis) {
        List<AxisRotateTool> tools = new ArrayList<>();
        for (GroupMember member : group.getMembers()) {
            AxisRotateTool tool = member.getTools().rotate(context, axis);
            if (tool == null) {
                return ToolProvider.super.rotate(context, axis);
            }
            tools.add(tool);
        }
        return new GroupAxisRotateTool(context, axis, tools);
    }
}
