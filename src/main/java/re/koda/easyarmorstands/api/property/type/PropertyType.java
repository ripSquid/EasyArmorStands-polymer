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

import re.koda.easyarmorstands.api.formatter.ValueFormatter;
import re.koda.easyarmorstands.platform.entity.Player;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public interface PropertyType<T> extends Keyed {
    static <T> Builder<T> builder(Key key) {
        return new PropertyTypeImpl.Builder<>(key);
    }

    static <T> Builder<T> builder(Key key, Class<T> type) {
        return PropertyType.builder(key);
    }

    @Nullable String getPermission();

    default boolean canChange(@NotNull Player player) {
        String permission = getPermission();
        if (permission != null) {
            return player.hasPermission(permission);
        } else {
            return true;
        }
    }

    default boolean canCopy(@NotNull Player player) {
        return true;
    }

    /**
     * Display name of this property.
     *
     * @return Display name.
     */
    @NotNull Component getName();

    /**
     * Format a value for display in a chat message.
     *
     * @param value The value to format.
     * @return The formatted value.
     */
    @NotNull Component getValueComponent(@NotNull T value);

    /**
     * Format a value as a string.
     *
     * @param value The value to format.
     * @return The formatted value.
     */
    default @NotNull String getValueString(@NotNull T value) {
        return PlainTextComponentSerializer.plainText().serialize(getValueComponent(value));
    }

    default @NotNull T cloneValue(@NotNull T value) {
        return value;
    }

    interface Builder<T> {
        Builder<T> name(Component name);

        Builder<T> formatter(ValueFormatter<T> formatter);

        Builder<T> permission(String permission);

        Builder<T> canCopyPredicate(Predicate<Player> predicate);

        PropertyType<T> build();
    }
}
