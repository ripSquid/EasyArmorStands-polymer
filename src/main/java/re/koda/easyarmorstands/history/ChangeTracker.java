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
package re.koda.easyarmorstands.history;

import re.koda.easyarmorstands.EasyArmorStandsCommon;
import re.koda.easyarmorstands.api.element.Element;
import re.koda.easyarmorstands.api.property.Property;
import re.koda.easyarmorstands.api.property.type.PropertyType;
import re.koda.easyarmorstands.history.action.Action;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ChangeTracker {
    private final EasyArmorStandsCommon eas;
    private final History history;
    private final Map<ChangeKey<?>, Object> originalValues = new HashMap<>();
    private final Map<ChangeKey<?>, Object> pendingValues = new HashMap<>();

    public ChangeTracker(EasyArmorStandsCommon eas, History history) {
        this.eas = eas;
        this.history = history;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void commit(@Nullable Component description) {
        List<Action> actions = new ArrayList<>();
        for (Map.Entry<ChangeKey<?>, Object> entry : pendingValues.entrySet()) {
            ChangeKey key = entry.getKey();
            Object oldValue = originalValues.get(key);
            Object value = entry.getValue();
            if (!Objects.equals(oldValue, value)) {
                actions.add(key.createChangeAction(oldValue, value));
            }
        }
        history.push(actions, description);
        originalValues.clear();
        pendingValues.clear();
    }

    public <T> void recordChange(Element element, Property<T> property, T oldValue, T value) {
        PropertyType<T> type = property.getType();
        ChangeKey<T> key = new ChangeKey<>(eas, element, type);
        originalValues.putIfAbsent(key, type.cloneValue(oldValue));
        pendingValues.put(key, type.cloneValue(value));
    }
}
