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

import re.koda.easyarmorstands.EasyArmorStandsCommon;
import re.koda.easyarmorstands.api.Axis;
import re.koda.easyarmorstands.api.editor.tool.AxisRotateTool;
import re.koda.easyarmorstands.api.editor.tool.AxisScaleTool;
import re.koda.easyarmorstands.api.editor.tool.ScaleTool;
import re.koda.easyarmorstands.api.editor.tool.ToolContext;
import re.koda.easyarmorstands.api.editor.tool.ToolProvider;
import re.koda.easyarmorstands.api.property.PropertyContainer;
import re.koda.easyarmorstands.api.property.type.DisplayPropertyTypes;
import re.koda.easyarmorstands.api.util.RotationProvider;
import re.koda.easyarmorstands.editor.EntityPositionProvider;
import re.koda.easyarmorstands.editor.EntityRotationProvider;
import re.koda.easyarmorstands.editor.OffsetProvider;
import re.koda.easyarmorstands.editor.display.DisplayRotationProvider;
import re.koda.easyarmorstands.editor.display.tool.DisplayAxisRotateTool;
import re.koda.easyarmorstands.editor.display.tool.DisplayAxisScaleTool;
import re.koda.easyarmorstands.editor.display.tool.DisplayScaleTool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DisplayToolProvider extends SimpleEntityToolProvider implements ToolProvider {
    private final RotationProvider entityRotationProvider;

    public DisplayToolProvider(EasyArmorStandsCommon eas, PropertyContainer properties, OffsetProvider offsetProvider) {
        super(eas, properties);
        positionProvider = new EntityPositionProvider(properties, offsetProvider);
        rotationProvider = new DisplayRotationProvider(properties);
        entityRotationProvider = new EntityRotationProvider(properties);
    }

    @Override
    public @Nullable AxisRotateTool rotate(@NotNull ToolContext context, @NotNull Axis axis) {
        return new DisplayAxisRotateTool(context, properties, DisplayPropertyTypes.LEFT_ROTATION, axis, entityRotationProvider);
    }

    @Override
    public @NotNull ScaleTool scale(@NotNull ToolContext context) {
        return new DisplayScaleTool(eas, context, properties);
    }

    @Override
    public @Nullable AxisScaleTool scale(@NotNull ToolContext context, @NotNull Axis axis) {
        if (context.rotation() == rotation()) {
            return new DisplayAxisScaleTool(eas, context, properties, axis);
        }
        return super.scale(context, axis);
    }
}
