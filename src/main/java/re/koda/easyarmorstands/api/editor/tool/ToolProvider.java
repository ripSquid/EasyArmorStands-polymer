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
import re.koda.easyarmorstands.api.util.PositionProvider;
import re.koda.easyarmorstands.api.util.RotationProvider;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ToolProvider {
    @Contract(pure = true)
    @NotNull
    PositionProvider position();

    @Contract(pure = true)
    @NotNull
    RotationProvider rotation();

    default @Nullable MoveTool move(@NotNull ToolContext context) {
        return null;
    }

    @Contract(pure = true)
    default @Nullable AxisMoveTool move(@NotNull ToolContext context, @NotNull Axis axis) {
        MoveTool moveTool = move(context);
        if (moveTool != null) {
            return new SimpleAxisMoveTool(moveTool, axis);
        }
        return null;
    }

    @Contract(pure = true)
    default @Nullable AxisRotateTool rotate(@NotNull ToolContext context, @NotNull Axis axis) {
        return null;
    }

    @Contract(pure = true)
    default @Nullable ScaleTool scale(@NotNull ToolContext context) {
        return null;
    }

    @Contract(pure = true)
    default @Nullable AxisScaleTool scale(@NotNull ToolContext context, @NotNull Axis axis) {
        return null;
    }
}
