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

import re.koda.easyarmorstands.api.property.Property;
import re.koda.easyarmorstands.api.property.PropertyContainer;
import re.koda.easyarmorstands.api.property.type.PropertyType;
import re.koda.easyarmorstands.message.Message;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class GroupPropertyContainer implements PropertyContainer {
    private final List<PropertyContainer> containers;

    public GroupPropertyContainer(Collection<PropertyContainer> containers) {
        this.containers = new ArrayList<>(containers);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void forEach(@NotNull Consumer<Property<?>> consumer) {
        Map<PropertyType, GroupProperty> properties = new LinkedHashMap<>();
        for (PropertyContainer container : containers) {
            container.forEach(property ->
                    properties.computeIfAbsent(property.getType(), GroupProperty::new)
                            .addProperty(property));
        }
        for (GroupProperty property : properties.values()) {
            consumer.accept(property);
        }
    }

    @Override
    public @Nullable <T> Property<T> getOrNull(@NotNull PropertyType<T> type) {
        GroupProperty<T> property = new GroupProperty<>(type);
        for (PropertyContainer container : containers) {
            Property<T> member = container.getOrNull(type);
            if (member != null) {
                property.addProperty(member);
            }
        }
        if (property.isEmpty()) {
            return null;
        }
        return property;
    }

    @Override
    public boolean isValid() {
        for (PropertyContainer container : containers) {
            if (container.isValid()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void commit(@Nullable Component description) {
        for (PropertyContainer container : containers) {
            container.commit(description);
        }
    }

    @Override
    public void commit() {
        commit(Message.component("easyarmorstands.history.edit-group", Component.text(containers.size())));
    }
}
