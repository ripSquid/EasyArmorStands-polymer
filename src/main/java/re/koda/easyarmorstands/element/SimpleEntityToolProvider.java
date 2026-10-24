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
import re.koda.easyarmorstands.api.editor.tool.MoveTool;
import re.koda.easyarmorstands.api.editor.tool.ScaleTool;
import re.koda.easyarmorstands.api.editor.tool.ToolContext;
import re.koda.easyarmorstands.api.editor.tool.ToolProvider;
import re.koda.easyarmorstands.api.property.Property;
import re.koda.easyarmorstands.api.property.PropertyContainer;
import re.koda.easyarmorstands.api.property.type.EntityPropertyTypes;
import re.koda.easyarmorstands.api.util.PositionProvider;
import re.koda.easyarmorstands.api.util.RotationProvider;
import re.koda.easyarmorstands.config.LimitScaleConfig;
import re.koda.easyarmorstands.editor.EntityPositionProvider;
import re.koda.easyarmorstands.editor.EntityRotationProvider;
import re.koda.easyarmorstands.platform.util.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleEntityToolProvider implements ToolProvider {
    protected final EasyArmorStandsCommon eas;
    protected final PropertyContainer properties;
    private final Property<Location> locationProperty;
    private final Property<Double> scaleProperty;
    protected PositionProvider positionProvider;
    protected RotationProvider rotationProvider;

    public SimpleEntityToolProvider(EasyArmorStandsCommon eas, PropertyContainer properties) {
        this.eas = eas;
        this.properties = properties;
        this.locationProperty = properties.getOrNull(EntityPropertyTypes.LOCATION);
        this.scaleProperty = properties.getOrNull(EntityPropertyTypes.SCALE);
        positionProvider = new EntityPositionProvider(properties);
        rotationProvider = new EntityRotationProvider(properties);
    }

    @Override
    public final @NotNull PositionProvider position() {
        return positionProvider;
    }

    @Override
    public final @NotNull RotationProvider rotation() {
        return rotationProvider;
    }

    @Override
    public @Nullable MoveTool move(@NotNull ToolContext context) {
        return MoveTool.of(context, properties, locationProperty);
    }

    @Override
    public @Nullable AxisRotateTool rotate(@NotNull ToolContext context, @NotNull Axis axis) {
        if (axis == Axis.Y && context.rotation() == RotationProvider.identity()) {
            return AxisRotateTool.ofYaw(context, properties, locationProperty);
        }
        if (axis == Axis.X && context.rotation() == rotation()) {
            return AxisRotateTool.ofPitch(context, properties, locationProperty);
        }
        return null;
    }

    @Override
    public @Nullable ScaleTool scale(@NotNull ToolContext context) {
        if (scaleProperty != null) {
            LimitScaleConfig limits = eas.getConfiguration().limits.entity;
            return ScaleTool.of(context, properties, locationProperty, scaleProperty, limits.minScale, limits.maxScale);
        }
        return ToolProvider.super.scale(context);
    }
}
