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
package re.koda.easyarmorstands.api.element;

import re.koda.easyarmorstands.api.property.PropertyContainer;
import re.koda.easyarmorstands.api.property.PropertyMap;
import re.koda.easyarmorstands.platform.entity.Player;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The type of the element.
 */
public interface ElementType extends Keyed {
    /**
     * Attempt to create an element with the specified properties.
     *
     * @param properties the properties which should be applied to the element
     * @return the created element, or {@code null} on failure
     */
    @Nullable Element createElement(@NotNull PropertyContainer properties);

    /**
     * Modify the default properties used when {@link ElementSpawnRequest spawning} an element.
     *
     * @param properties the properties which may be modified
     */
    default void applyDefaultProperties(@NotNull PropertyMap properties) {
    }

    /**
     * Whether the element is spawned at the eye height of the player instead of at their feet.
     *
     * @return true if spawned at eye height
     */
    default boolean isSpawnedAtEyeHeight() {
        return true;
    }

    /**
     * Returns the display name of this element type.
     * <p>
     * For example, this is displayed in spawn buttons and history actions.
     *
     * @return the display name
     */
    @Contract(pure = true)
    @NotNull Component getDisplayName();

    /**
     * Checks whether a player is allowed to spawn an element of this type.
     *
     * @param player the player
     * @return whether the element may be spawned
     */
    @Contract(pure = true)
    boolean canSpawn(@NotNull Player player);
}
