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
package re.koda.easyarmorstands.api.editor;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

@ApiStatus.NonExtendable
public interface Snapper {
    @Contract(pure = true)
    double snapOffset(double offset);

    default void snapOffset(@NotNull Vector3d offset) {
        offset.x = snapOffset(offset.x);
        offset.y = snapOffset(offset.y);
        offset.z = snapOffset(offset.z);
    }

    @Contract(pure = true)
    double snapPosition(double position);

    default void snapPosition(@NotNull Vector3d position) {
        position.x = snapPosition(position.x);
        position.y = snapPosition(position.y);
        position.z = snapPosition(position.z);
    }

    @Contract(pure = true)
    double snapAngle(double angle);
}
