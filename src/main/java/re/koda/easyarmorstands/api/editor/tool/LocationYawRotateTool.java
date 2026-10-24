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
import re.koda.easyarmorstands.api.property.Property;
import re.koda.easyarmorstands.api.property.PropertyContainer;
import re.koda.easyarmorstands.api.util.EasFormat;
import re.koda.easyarmorstands.platform.entity.Player;
import re.koda.easyarmorstands.platform.util.Location;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaterniondc;
import org.joml.Vector3d;
import org.joml.Vector3dc;

class LocationYawRotateTool implements AxisRotateTool {
    private final ToolContext context;
    private final PropertyContainer properties;
    private final Property<Location> locationProperty;

    LocationYawRotateTool(ToolContext context, PropertyContainer properties, Property<Location> locationProperty) {
        this.context = context;
        this.properties = properties;
        this.locationProperty = locationProperty;
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
        return Axis.Y;
    }

    @Override
    public @NotNull AxisRotateToolSession start() {
        return new SessionImpl();
    }

    @Override
    public boolean canUse(@NotNull Player player) {
        return locationProperty.canChange(player);
    }

    private class SessionImpl implements AxisRotateToolSession {
        private final Location originalLocation;
        private final Vector3dc originalOffset;
        private final Vector3d offsetChange = new Vector3d();

        public SessionImpl() {
            this.originalLocation = locationProperty.getValue();
            this.originalOffset = originalLocation.position().sub(getPosition(), new Vector3d());
        }

        @Override
        public void setChange(double change) {
            originalOffset.rotateY(change, offsetChange).sub(originalOffset);

            Vector3d position = new Vector3d(originalLocation.position());
            position.add(offsetChange.x(), offsetChange.y(), offsetChange.z());

            locationProperty.setValue(originalLocation
                    .withPosition(position)
                    .withYaw(originalLocation.yaw() - (float) Math.toDegrees(change)));
        }

        @Override
        public double snapChange(double change, @NotNull Snapper context) {
            double original = Math.toRadians(originalLocation.yaw());
            change -= original;
            change = context.snapAngle(change);
            change += original;
            return change;
        }

        @Override
        public void setValue(double value) {
            setChange(Math.toRadians(originalLocation.yaw()) - value);
        }

        @Override
        public void revert() {
            locationProperty.setValue(originalLocation);
        }

        @Override
        public void commit(@Nullable Component description) {
            properties.commit(description);
        }

        @Override
        public boolean isValid() {
            return properties.isValid();
        }

        @Override
        public @Nullable Component getStatus() {
            return EasFormat.formatDegrees(locationProperty.getValue().yaw());
        }

        @Override
        public @Nullable Component getDescription() {
            return Component.translatable("easyarmorstands.history.rotate-yaw");
        }
    }
}
