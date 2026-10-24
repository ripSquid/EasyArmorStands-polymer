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
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

@NullMarked
record NullableProperty<T>(
        PropertyType<Optional<T>> type,
        Supplier<@Nullable T> getter,
        Consumer<@Nullable T> setter) implements Property<Optional<T>> {
    @Override
    public PropertyType<Optional<T>> getType() {
        return type;
    }

    @Override
    public Optional<T> getValue() {
        return Optional.ofNullable(getter.get());
    }

    @Override
    public boolean setValue(Optional<T> value) {
        setter.accept(value.orElse(null));
        return true;
    }
}
