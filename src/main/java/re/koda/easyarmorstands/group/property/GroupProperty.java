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
package re.koda.easyarmorstands.group.property;

import re.koda.easyarmorstands.api.property.PendingChange;
import re.koda.easyarmorstands.api.property.Property;
import re.koda.easyarmorstands.api.property.type.PropertyType;
import re.koda.easyarmorstands.platform.entity.Player;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GroupProperty<T> implements Property<T> {
    private final PropertyType<T> type;
    private final List<Property<T>> properties = new ArrayList<>();

    public GroupProperty(PropertyType<T> type) {
        this.type = type;
    }

    public void addProperty(Property<T> property) {
        properties.add(property);
    }

    boolean isEmpty() {
        return properties.isEmpty();
    }

    @Override
    public @NotNull PropertyType<T> getType() {
        return type;
    }

    @Override
    public @NotNull T getValue() {
        Map<T, Integer> votes = new HashMap<>();
        for (Property<T> property : properties) {
            votes.merge(property.getValue(), 1, Integer::sum);
        }

        T best = null;
        int bestVotes = 0;
        for (Map.Entry<T, Integer> entry : votes.entrySet()) {
            int n = entry.getValue();
            if (n > bestVotes) {
                best = entry.getKey();
                bestVotes = n;
            }
        }
        return Objects.requireNonNull(best);
    }

    @Override
    public boolean setValue(@NotNull T value) {
        boolean changed = false;
        for (Property<T> property : properties) {
            if (property.setValue(value)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public @Nullable PendingChange prepareChange(@NotNull T value) {
        List<PendingChange> changes = new ArrayList<>(properties.size());
        for (Property<T> property : properties) {
            PendingChange change = property.prepareChange(value);
            if (change != null) {
                changes.add(change);
            }
        }
        if (changes.isEmpty()) {
            return null;
        }
        return new GroupPendingChange(changes);
    }

    @Override
    public boolean canChange(@NotNull Player player) {
        for (Property<T> property : properties) {
            if (property.canChange(player)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void commit(@Nullable Component description) {
        for (Property<T> property : properties) {
            property.commit(description);
        }
    }
}
