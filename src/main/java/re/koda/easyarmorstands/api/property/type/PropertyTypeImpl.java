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

import re.koda.easyarmorstands.api.formatter.StringFormatter;
import re.koda.easyarmorstands.api.formatter.ValueFormatter;
import re.koda.easyarmorstands.platform.entity.Player;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;
import java.util.function.Predicate;

@NullMarked
record PropertyTypeImpl<T>(
        Key key,
        Component name,
        ValueFormatter<T> formatter,
        @Nullable String permission,
        Predicate<Player> canCopyPredicate) implements PropertyType<T> {
    @Override
    public Component getName() {
        return name;
    }

    @Override
    public @Nullable String getPermission() {
        return permission;
    }

    @Override
    public Component getValueComponent(T value) {
        return formatter.format(value);
    }

    @Override
    public String getValueString(T value) {
        return formatter.formatAsString(value);
    }

    @Override
    public boolean canCopy(Player player) {
        return canCopyPredicate.test(player);
    }

    static class Builder<T> implements PropertyType.Builder<T> {
        private final Key key;
        private Component name;
        private ValueFormatter<T> formatter = new StringFormatter<>();
        private @Nullable String permission;
        private Predicate<Player> canCopyPredicate = p -> true;

        public Builder(Key key) {
            this.key = key;
            this.name = Component.text(key.asString());
        }

        @Override
        public Builder<T> name(Component name) {
            this.name = Objects.requireNonNull(name);
            return this;
        }

        @Override
        public Builder<T> formatter(ValueFormatter<T> formatter) {
            this.formatter = Objects.requireNonNull(formatter);
            return this;
        }

        @Override
        public Builder<T> permission(String permission) {
            this.permission = Objects.requireNonNull(permission);
            return this;
        }

        @Override
        public Builder<T> canCopyPredicate(Predicate<Player> predicate) {
            this.canCopyPredicate = predicate;
            return this;
        }

        @Override
        public PropertyType<T> build() {
            return new PropertyTypeImpl<>(key, name, formatter, permission, canCopyPredicate);
        }
    }
}
