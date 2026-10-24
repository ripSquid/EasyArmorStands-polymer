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
package re.koda.easyarmorstands.property;

import re.koda.easyarmorstands.api.element.Element;
import re.koda.easyarmorstands.api.property.PendingChange;
import re.koda.easyarmorstands.api.property.Property;
import re.koda.easyarmorstands.api.property.type.PropertyType;
import re.koda.easyarmorstands.command.sender.EasPlayer;
import re.koda.easyarmorstands.platform.entity.Player;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class PermissionCheckedPropertyWrapper<T> implements Property<T> {
    private final Property<T> property;
    private final Element element;
    private final EasPlayer player;
    private Boolean hasPermissionCache;

    PermissionCheckedPropertyWrapper(Property<T> property, Element element, EasPlayer player) {
        this.property = property;
        this.element = element;
        this.player = player;
    }

    private boolean hasPermission() {
        if (hasPermissionCache != null) {
            return hasPermissionCache;
        }

        boolean result = property.getType().canChange(player.get());
        hasPermissionCache = result;
        return result;
    }

    @Override
    public @NotNull PropertyType<T> getType() {
        return property.getType();
    }

    @Override
    public @NotNull T getValue() {
        return property.getValue();
    }

    private boolean isAllowed(T value) {
        if (!hasPermission()) {
            return false;
        }
        return player.canChangeProperty(element, property, value);
    }

    @Override
    public boolean setValue(@NotNull T value) {
        if (!isAllowed(value)) {
            return false;
        }
        return property.setValue(value);
    }

    @Override
    public @Nullable PendingChange prepareChange(@NotNull T value) {
        if (!isAllowed(value)) {
            return null;
        }
        return property.prepareChange(value);
    }

    @Override
    public boolean canChange(@NotNull Player player) {
        return hasPermission();
    }

    @Override
    public void commit(@Nullable Component description) {
        property.commit(description);
    }
}
