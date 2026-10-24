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
import re.koda.easyarmorstands.history.ChangeTracker;
import re.koda.easyarmorstands.platform.entity.Player;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

class TrackedPropertyWrapper<T> implements Property<T> {
    private final ChangeTracker tracker;
    private final Element element;
    private final Property<T> property;

    TrackedPropertyWrapper(ChangeTracker tracker, Element element, Property<T> property) {
        this.tracker = tracker;
        this.element = element;
        this.property = property;
    }

    @Override
    public @NotNull PropertyType<T> getType() {
        return property.getType();
    }

    @Override
    public @NotNull T getValue() {
        return property.getValue();
    }

    @Override
    public boolean setValue(@NotNull T value) {
        Objects.requireNonNull(value);
        T oldValue = property.getValue();
        if (Objects.equals(oldValue, value)) {
            return true;
        }
        if (!property.setValue(value)) {
            return false;
        }
        tracker.recordChange(element, property, oldValue, value);
        return true;
    }

    @Override
    public @Nullable PendingChange prepareChange(@NotNull T value) {
        PendingChange change = property.prepareChange(value);
        if (change == null) {
            return null;
        }
        return new TrackedPendingChangeWrapper<>(tracker, element, property, value, change);
    }

    @Override
    public boolean canChange(@NotNull Player player) {
        return property.canChange(player);
    }

    @Override
    public void commit(@Nullable Component description) {
        tracker.commit(description);
        property.commit(description);
    }
}
