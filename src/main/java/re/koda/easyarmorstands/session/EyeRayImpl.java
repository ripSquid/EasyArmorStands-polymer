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
package re.koda.easyarmorstands.session;

import re.koda.easyarmorstands.api.editor.EyeRay;
import re.koda.easyarmorstands.api.util.BoundingBox;
import re.koda.easyarmorstands.platform.util.Location;
import re.koda.easyarmorstands.platform.world.World;
import re.koda.easyarmorstands.util.EasMath;
import re.koda.easyarmorstands.util.Util;
import re.koda.easyarmorstands.gizmo.api.cursor.Cursor;
import re.koda.easyarmorstands.gizmo.api.cursor.Intersection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4d;
import org.joml.Matrix4dc;
import org.joml.Vector3d;
import org.joml.Vector3dc;

public class EyeRayImpl implements EyeRay {
    private final World world;
    private final Vector3dc origin;
    private final Vector3dc direction;
    private final Vector3dc target;
    private final double length;
    private final Cursor cursor;
    private final double threshold;
    private final float yaw;
    private final float pitch;
    private final Matrix4dc matrix;
    private Matrix4dc inverseMatrix;

    public EyeRayImpl(World world, Location location, double length, double threshold) {
        this.world = world;
        this.origin = location.position();
        this.direction = EasMath.getDirection(location);
        this.target = origin.fma(length, direction, new Vector3d());
        this.length = length;
        this.cursor = Cursor.of(origin, target);
        this.threshold = threshold;
        this.yaw = location.yaw();
        this.pitch = location.pitch();
        this.matrix = Util.toMatrix4d(location);
    }

    private static @Nullable Vector3dc convert(@Nullable Intersection intersection) {
        if (intersection != null) {
            return intersection.positionOnTarget();
        } else {
            return null;
        }
    }

    @Override
    public @NotNull World world() {
        return world;
    }

    @Override
    public @NotNull Vector3dc origin() {
        return origin;
    }

    @Override
    public @NotNull Vector3dc target() {
        return target;
    }

    @Override
    public @NotNull Vector3dc point(double distance) {
        return origin.fma(distance, direction, new Vector3d());
    }

    @Override
    public double length() {
        return length;
    }

    @Override
    public double threshold() {
        return threshold;
    }

    @Override
    public float yaw() {
        return yaw;
    }

    @Override
    public float pitch() {
        return pitch;
    }

    @Override
    public @NotNull Matrix4dc matrix() {
        return matrix;
    }

    @Override
    public @NotNull Matrix4dc inverseMatrix() {
        if (inverseMatrix == null) {
            inverseMatrix = matrix.invert(new Matrix4d());
        }
        return inverseMatrix;
    }

    @Override
    public @Nullable Vector3dc intersectPoint(@NotNull Vector3dc position) {
        return intersectPoint(position, 1);
    }

    @Override
    public @Nullable Vector3dc intersectPoint(@NotNull Vector3dc position, double scale) {
        return convert(cursor.intersectPoint(position, threshold() * scale));
    }

    @Override
    public @Nullable Vector3dc intersectLine(@NotNull Vector3dc start, @NotNull Vector3dc end) {
        return intersectLine(start, end, 1);
    }

    @Override
    public @Nullable Vector3dc intersectLine(@NotNull Vector3dc start, @NotNull Vector3dc end, double scale) {
        return convert(cursor.intersectLine(start, end, threshold * scale));
    }

    @Override
    public @Nullable Vector3dc intersectPlane(@NotNull Vector3dc point, @NotNull Vector3dc normal) {
        return convert(cursor.intersectPlane(point, normal));
    }

    @Override
    public @Nullable Vector3dc intersectPlane(@NotNull Vector3dc point, @NotNull Vector3dc normal, double range) {
        return convert(cursor.intersectPlane(point, normal, range));
    }

    @Override
    public @Nullable Vector3dc intersectCircle(@NotNull Vector3dc point, @NotNull Vector3dc normal, double radius) {
        return intersectCircle(point, normal, radius, 1);
    }

    @Override
    public @Nullable Vector3dc intersectCircle(@NotNull Vector3dc point, @NotNull Vector3dc normal, double radius, double scale) {
        return convert(cursor.intersectCircle(point, normal, radius, threshold * scale));
    }

    @Override
    public @Nullable Vector3dc intersectBox(@NotNull BoundingBox box) {
        return convert(cursor.intersectBox(box.getMinPosition(), box.getMaxPosition()));
    }
}
