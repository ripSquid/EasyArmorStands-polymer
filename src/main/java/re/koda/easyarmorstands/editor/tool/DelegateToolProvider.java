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
package re.koda.easyarmorstands.editor.tool;

import re.koda.easyarmorstands.api.Axis;
import re.koda.easyarmorstands.api.editor.tool.AxisMoveTool;
import re.koda.easyarmorstands.api.editor.tool.AxisRotateTool;
import re.koda.easyarmorstands.api.editor.tool.AxisScaleTool;
import re.koda.easyarmorstands.api.editor.tool.MoveTool;
import re.koda.easyarmorstands.api.editor.tool.ScaleTool;
import re.koda.easyarmorstands.api.editor.tool.ToolContext;
import re.koda.easyarmorstands.api.editor.tool.ToolProvider;
import re.koda.easyarmorstands.api.util.PositionProvider;
import re.koda.easyarmorstands.api.util.RotationProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DelegateToolProvider implements ToolProvider {
    private final ToolProvider toolProvider;
    private final PositionProvider positionProvider;
    private final RotationProvider rotationProvider;

    public DelegateToolProvider(ToolProvider toolProvider, PositionProvider positionProvider, RotationProvider rotationProvider) {
        this.toolProvider = toolProvider;
        if (positionProvider != null) {
            this.positionProvider = positionProvider;
        } else {
            this.positionProvider = toolProvider.position();
        }
        if (rotationProvider != null) {
            this.rotationProvider = rotationProvider;
        } else {
            this.rotationProvider = toolProvider.rotation();
        }
    }

    @Override
    public @NotNull PositionProvider position() {
        return positionProvider;
    }

    @Override
    public @NotNull RotationProvider rotation() {
        return rotationProvider;
    }

    @Override
    public @Nullable MoveTool move(@NotNull ToolContext context) {
        return toolProvider.move(context);
    }

    @Override
    public @Nullable AxisMoveTool move(@NotNull ToolContext context, @NotNull Axis axis) {
        return toolProvider.move(context, axis);
    }

    @Override
    public @Nullable AxisRotateTool rotate(@NotNull ToolContext context, @NotNull Axis axis) {
        return toolProvider.rotate(context, axis);
    }

    @Override
    public @Nullable ScaleTool scale(@NotNull ToolContext context) {
        return toolProvider.scale(context);
    }

    @Override
    public @Nullable AxisScaleTool scale(@NotNull ToolContext context, @NotNull Axis axis) {
        return toolProvider.scale(context, axis);
    }
}
