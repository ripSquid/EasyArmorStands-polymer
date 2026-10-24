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
import re.koda.easyarmorstands.platform.entity.Player;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

@NullMarked
public interface Property<T> {
    static <T> Property<T> of(
            PropertyType<T> type,
            Supplier<T> getter,
            Consumer<T> setter) {
        return new SimpleProperty<>(type, getter, setter);
    }

    static <T> Property<Optional<T>> ofNullable(
            PropertyType<Optional<T>> type,
            Supplier<@Nullable T> getter,
            Consumer<@Nullable T> setter) {
        return new NullableProperty<>(type, getter, setter);
    }

    /**
     * Returns the type of this property.
     *
     * @return The type.
     */
    PropertyType<T> getType();

    /**
     * Returns the current value of this property.
     *
     * @return The current value.
     */
    T getValue();

    /**
     * Attempts to set the value of this property.
     *
     * @param value The new value.
     * @return Whether changing the property succeeded.
     */
    boolean setValue(T value);

    /**
     * Prepares an action which will change the value of this property in the future.
     * <p>
     * This can be used to test whether a property can be edited before actually performing the change.
     * If this method returns null, the change is not allowed.
     * If a non-null change is returned, {@link PendingChange#execute() executing} it may still fail.
     *
     * @param value The new value.
     * @return The pending change, or null.
     */
    default @Nullable PendingChange prepareChange(T value) {
        return new PendingChangeImpl<>(this, value);
    }

    default boolean canChange(Player player) {
        return true;
    }

    default void commit() {
        commit(null);
    }

    default void commit(@Nullable Component description) {
    }
}
