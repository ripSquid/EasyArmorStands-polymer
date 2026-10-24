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
package re.koda.easyarmorstands.api.property;

import re.koda.easyarmorstands.api.element.Element;
import re.koda.easyarmorstands.api.property.type.PropertyType;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

/**
 * A property container which stores instances of properties.
 * <p>
 * Use {@link #register(Property)} to add a property.
 * Properties contained in a registry are typically bound to an element.
 */
public abstract class PropertyRegistry implements PropertyContainer {
    @SuppressWarnings("rawtypes")
    private final Map<PropertyType, Property> properties = new TreeMap<>(Comparator.comparing(PropertyType::key));

    public static @NotNull PropertyRegistry create(@NotNull Element element) {
        return new ElementPropertyRegistry(element);
    }

    public <T> void register(@NotNull Property<T> property) {
        properties.put(property.getType(), property);
    }

    @Override
    public void forEach(@NotNull Consumer<@NotNull Property<?>> consumer) {
        for (Property<?> property : properties.values()) {
            consumer.accept(property);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Property<T> getOrNull(@NotNull PropertyType<T> type) {
        return (Property<T>) properties.get(type);
    }

    @Override
    public void commit(@Nullable Component description) {
    }
}
