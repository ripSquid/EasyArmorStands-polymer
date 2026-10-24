/*
 * EasyArmorStands-Polymer - a Polymer based Fabric port of EasyArmorStands
 * Copyright (C) 56738 and EasyArmorStands contributors (original work)
 * Copyright (C) 2026 ripsquid (modifications)
 *
 * This file is derived from gizmo (https://github.com/56738/gizmo) by 56738,
 * a GPL-3.0 licensed library used by EasyArmorStands, and was modified on 2026-10-22
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
package re.koda.easyarmorstands.gizmo.api.cursor;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaterniondc;
import org.joml.Vector3dc;

@ApiStatus.NonExtendable
public interface Cursor {
    static @NotNull Cursor of(@NotNull Vector3dc origin, @NotNull Vector3dc direction, double length) {
        return new RayCursor(origin, direction, length);
    }

    static @NotNull Cursor of(@NotNull Vector3dc origin, @NotNull Vector3dc end) {
        return new RayCursor(origin, end);
    }

    @Nullable Intersection intersectPoint(@NotNull Vector3dc position, double threshold);

    @Nullable Intersection intersectLine(@NotNull Vector3dc start, @NotNull Vector3dc end, double threshold);

    @Nullable Intersection intersectPlane(@NotNull Vector3dc point, @NotNull Vector3dc normal);

    @Nullable Intersection intersectPlane(@NotNull Vector3dc point, @NotNull Vector3dc normal, double range);

    @Nullable Intersection intersectBox(@NotNull Vector3dc min, @NotNull Vector3dc max);

    @Nullable Intersection intersectBox(@NotNull Vector3dc min, @NotNull Vector3dc max, @NotNull Vector3dc center, @NotNull Quaterniondc rotation);

    @Nullable Intersection intersectCircle(@NotNull Vector3dc center, @NotNull Vector3dc axis, double radius, double threshold);
}
