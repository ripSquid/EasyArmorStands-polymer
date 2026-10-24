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
package re.koda.easyarmorstands.api.property.type;

import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Objects;
import java.util.function.Function;

public final class EnumKeyedPropertyType<K extends Enum<K>, V> implements KeyedPropertyType<K, V> {
    private final EnumMap<K, PropertyType<V>> map;

    public EnumKeyedPropertyType(Class<K> type, Function<K, PropertyType<V>> provider) {
        map = new EnumMap<>(type);
        for (K key : type.getEnumConstants()) {
            map.put(key, Objects.requireNonNull(provider.apply(key), "Property type for key: " + key));
        }
    }

    @Override
    public @NotNull PropertyType<V> get(@NotNull K key) {
        return Objects.requireNonNull(map.get(key));
    }
}
