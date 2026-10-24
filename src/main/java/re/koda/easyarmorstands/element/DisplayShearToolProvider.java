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
package re.koda.easyarmorstands.element;

import re.koda.easyarmorstands.api.Axis;
import re.koda.easyarmorstands.api.editor.tool.AxisRotateTool;
import re.koda.easyarmorstands.api.editor.tool.ToolContext;
import re.koda.easyarmorstands.api.editor.tool.ToolProvider;
import re.koda.easyarmorstands.api.property.PropertyContainer;
import re.koda.easyarmorstands.api.property.type.DisplayPropertyTypes;
import re.koda.easyarmorstands.api.util.PositionProvider;
import re.koda.easyarmorstands.api.util.RotationProvider;
import re.koda.easyarmorstands.editor.EntityPositionProvider;
import re.koda.easyarmorstands.editor.display.DisplayRotationProvider;
import re.koda.easyarmorstands.editor.display.tool.DisplayAxisRotateTool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DisplayShearToolProvider implements ToolProvider {
    private final PropertyContainer properties;
    private final PositionProvider positionProvider;
    private final RotationProvider rotationProvider;

    public DisplayShearToolProvider(PropertyContainer properties) {
        this.properties = properties;
        positionProvider = new EntityPositionProvider(properties);
        rotationProvider = new DisplayRotationProvider(properties);
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
    public @Nullable AxisRotateTool rotate(@NotNull ToolContext context, @NotNull Axis axis) {
        if (context.position() == position() && context.rotation() == rotation()) {
            return new DisplayAxisRotateTool(context, properties, DisplayPropertyTypes.RIGHT_ROTATION, axis, rotation());
        }
        return null;
    }
}
