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

import re.koda.easyarmorstands.api.util.BoundingBox;
import re.koda.easyarmorstands.platform.util.Location;
import re.koda.easyarmorstands.platform.world.World;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Intersectiond;
import org.joml.Matrix4dc;
import org.joml.Vector3d;
import org.joml.Vector3dc;

@ApiStatus.NonExtendable
public interface EyeRay {
    @Contract(pure = true)
    @NotNull World world();

    @Contract(pure = true)
    @NotNull Vector3dc origin();

    @Contract(pure = true)
    @NotNull Vector3dc target();

    @Contract(pure = true)
    default @NotNull Vector3dc point(double distance) {
        Vector3dc origin = origin();
        return target().sub(origin, new Vector3d())
                .normalize(distance)
                .add(origin);
    }

    @Contract(pure = true)
    double length();

    @Contract(pure = true)
    double threshold();

    @Contract(pure = true)
    float yaw();

    @Contract(pure = true)
    float pitch();

    @Contract(pure = true)
    @NotNull Matrix4dc matrix();

    @Contract(pure = true)
    @NotNull Matrix4dc inverseMatrix();

    default boolean isInRange(@NotNull Location location) {
        return world().equals(location.world()) && isInRange(location.position());
    }

    default boolean isInRange(@NotNull Vector3dc position) {
        return isInRange(position.x(), position.y(), position.z());
    }

    default boolean isInRange(double x, double y, double z) {
        double range = length();
        return origin().distanceSquared(x, y, z) <= range * range;
    }

    @Contract(pure = true)
    default @NotNull Vector3dc projectPoint(@NotNull Vector3dc position) {
        Vector3dc origin = origin();
        Vector3dc target = target();
        return Intersectiond.findClosestPointOnLineSegment(
                origin.x(), origin.y(), origin.z(),
                target.x(), target.y(), target.z(),
                position.x(), position.y(), position.z(),
                new Vector3d());
    }

    @Contract(pure = true)
    @Nullable Vector3dc intersectPoint(@NotNull Vector3dc position);

    @Contract(pure = true)
    @Nullable Vector3dc intersectPoint(@NotNull Vector3dc position, double scale);

    @Contract(pure = true)
    @Nullable Vector3dc intersectLine(@NotNull Vector3dc start, @NotNull Vector3dc end);

    @Contract(pure = true)
    @Nullable Vector3dc intersectLine(@NotNull Vector3dc start, @NotNull Vector3dc end, double scale);

    @Contract(pure = true)
    @Nullable Vector3dc intersectPlane(@NotNull Vector3dc point, @NotNull Vector3dc normal);

    @Contract(pure = true)
    @Nullable Vector3dc intersectPlane(@NotNull Vector3dc point, @NotNull Vector3dc normal, double range);

    @Contract(pure = true)
    @Nullable Vector3dc intersectCircle(@NotNull Vector3dc point, @NotNull Vector3dc normal, double radius);

    @Contract(pure = true)
    @Nullable Vector3dc intersectCircle(@NotNull Vector3dc point, @NotNull Vector3dc normal, double radius, double scale);

    @Contract(pure = true)
    @Nullable Vector3dc intersectBox(@NotNull BoundingBox box);
}
