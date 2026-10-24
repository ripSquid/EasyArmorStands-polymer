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
package re.koda.easyarmorstands.api.editor.button;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3dc;

@ApiStatus.NonExtendable
public interface ButtonResult {
    @Contract(value = "_ -> new", pure = true)
    static @NotNull ButtonResult of(@NotNull Vector3dc position) {
        return ButtonResult.of(position, 0);
    }

    @Contract(value = "_, _ -> new", pure = true)
    static @NotNull ButtonResult of(@NotNull Vector3dc position, int priority) {
        return new ButtonResultImpl(position, priority);
    }

    /**
     * Returns the location where the eye ray intersects this button.
     *
     * @return Position of the intersection with the eye ray, or null if there is none.
     */
    @Contract(pure = true)
    @NotNull Vector3dc position();

    /**
     * Returns the priority of this result.
     * If the player is looking at multiple buttons, the result with the highest priority wins.
     *
     * @return The priority.
     */
    @Contract(pure = true)
    int priority();
}
