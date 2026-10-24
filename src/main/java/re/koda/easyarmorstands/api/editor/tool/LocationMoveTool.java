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

class LocationMoveTool implements MoveTool {
    private final ToolContext context;
    private final PropertyContainer properties;
    private final Property<Location> locationProperty;

    LocationMoveTool(ToolContext context, PropertyContainer properties, Property<Location> locationProperty) {
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
    public @NotNull MoveToolSession start() {
        return new SessionImpl();
    }

    @Override
    public boolean canUse(@NotNull Player player) {
        return locationProperty.canChange(player);
    }

    private class SessionImpl implements MoveToolSession {
        private final Location originalLocation;
        private final Vector3d offset = new Vector3d();

        public SessionImpl() {
            this.originalLocation = locationProperty.getValue();
        }

        @Override
        public void setChange(@NotNull Vector3dc change) {
            this.offset.set(change);
            Vector3d position = new Vector3d(originalLocation.position());
            position.add(change.x(), change.y(), change.z());
            locationProperty.setValue(originalLocation.withPosition(position));
        }

        @Override
        public void snapChange(@NotNull Vector3d change, @NotNull Snapper context) {
            change.add(originalLocation.position());
            context.snapPosition(change);
            change.sub(originalLocation.position());
        }

        @Override
        public @NotNull Vector3dc getPosition() {
            return locationProperty.getValue().position();
        }

        @Override
        public void setPosition(@NotNull Vector3dc position) {
            setChange(position.sub(originalLocation.position(), offset));
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
            return EasFormat.formatLocation(locationProperty.getValue());
        }

        @Override
        public @Nullable Component getDescription() {
            Component value = EasFormat.formatLocation(locationProperty.getValue());
            return Component.translatable("easyarmorstands.history.move", value);
        }

        @Override
        public boolean canSetPosition(Player player) {
            return player.hasPermission("easyarmorstands.position");
        }
    }
}
