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

import re.koda.easyarmorstands.api.property.PropertyMap;
import re.koda.easyarmorstands.platform.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A request to spawn an element.
 *
 * @see re.koda.easyarmorstands.api.EasyArmorStands#elementSpawnRequest(ElementType) Creating a request
 */
public interface ElementSpawnRequest {
    /**
     * Returns the type of the element which should be spawned.
     *
     * @return the type
     */
    @Contract(pure = true)
    @NotNull
    ElementType getType();

    /**
     * Returns the player who is performing the request.
     *
     * @return the player or {@code null}
     */
    @Contract(pure = true)
    @Nullable
    Player getPlayer();

    /**
     * Set which player is performing the request.
     * <p>
     * For example, this is used to perform permission checks and determine the default location.
     *
     * @param player the player or {@code null}
     */
    void setPlayer(@Nullable Player player);

    /**
     * The properties which will be applied to the element.
     * <p>
     * Can be modified.
     *
     * @return the modifiable property map
     */
    @Contract(pure = true)
    @NotNull PropertyMap getProperties();

    /**
     * Attempts to spawn the element.
     *
     * @return the spawned element or {@code null}
     */
    @Nullable Element spawn();
}
