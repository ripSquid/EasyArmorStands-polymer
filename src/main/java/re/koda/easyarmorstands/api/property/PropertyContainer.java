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

import re.koda.easyarmorstands.api.property.type.PropertyType;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface PropertyContainer {
    @Contract(pure = true)
    static @NotNull PropertyContainer immutable(@NotNull PropertyContainer container) {
        if (container instanceof ImmutablePropertyContainer) {
            return container;
        }
        return new ImmutablePropertyContainer(container);
    }

    void forEach(@NotNull Consumer<@NotNull Property<?>> consumer);

    @Contract(pure = true)
    <T> @Nullable Property<T> getOrNull(@NotNull PropertyType<T> type);

    @Contract(pure = true)
    default <T> @NotNull Property<T> get(@NotNull PropertyType<T> type) {
        Property<T> property = getOrNull(type);
        if (property == null) {
            throw new UnknownPropertyException(type);
        }
        return property;
    }

    @Contract(pure = true)
    boolean isValid();

    void commit(@Nullable Component description);

    default void commit() {
        commit(null);
    }
}
