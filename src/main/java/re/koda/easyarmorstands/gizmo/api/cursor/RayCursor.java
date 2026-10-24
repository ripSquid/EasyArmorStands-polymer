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

import re.koda.easyarmorstands.gizmo.intersection.SimpleIntersection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Intersectiond;
import org.joml.Quaterniond;
import org.joml.Quaterniondc;
import org.joml.Vector2d;
import org.joml.Vector3d;
import org.joml.Vector3dc;

class RayCursor implements Cursor {
    private final Vector3dc origin;
    private final Vector3dc direction;
    private final Vector3dc end;
    private final double length;

    RayCursor(@NotNull Vector3dc origin, @NotNull Vector3dc direction, double length) {
        this.origin = new Vector3d(origin);
        this.direction = new Vector3d(direction).normalize();
        this.end = new Vector3d(this.origin).fma(length, this.direction);
        this.length = length;
    }

    RayCursor(@NotNull Vector3dc origin, @NotNull Vector3dc end) {
        this.origin = new Vector3d(origin);
        this.end = new Vector3d(end);
        this.direction = new Vector3d(this.end).sub(this.origin).normalize();
        this.length = this.origin.distance(this.end);
    }

    @Override
    public @Nullable Intersection intersectPoint(@NotNull Vector3dc position, double threshold) {
        Vector3d cursorPoint = new Vector3d();
        Intersectiond.findClosestPointOnLineSegment(
                origin.x(),
                origin.y(),
                origin.z(),
                end.x(),
                end.y(),
                end.z(),
                position.x(),
                position.y(),
                position.z(),
                cursorPoint);

        double distanceSquared = cursorPoint.distanceSquared(position);
        if (distanceSquared <= threshold * threshold) {
            return new SimpleIntersection(cursorPoint, new Vector3d(position));
        } else {
            return null;
        }
    }

    @Override
    public @Nullable Intersection intersectLine(@NotNull Vector3dc start, @NotNull Vector3dc end, double threshold) {
        Vector3d cursorPoint = new Vector3d();
        Vector3d linePoint = new Vector3d();
        double distanceSquared = Intersectiond.findClosestPointsLineSegments(
                origin.x(),
                origin.y(),
                origin.z(),
                this.end.x(),
                this.end.y(),
                this.end.z(),
                start.x(),
                start.y(),
                start.z(),
                end.x(),
                end.y(),
                end.z(),
                cursorPoint,
                linePoint);

        if (distanceSquared <= threshold * threshold) {
            return new SimpleIntersection(cursorPoint, linePoint);
        } else {
            return null;
        }
    }

    @Override
    public @Nullable Intersection intersectPlane(@NotNull Vector3dc point, @NotNull Vector3dc normal) {
        return intersectPlane(point, normal, length);
    }

    @Override
    public @Nullable Intersection intersectPlane(@NotNull Vector3dc point, @NotNull Vector3dc normal, double range) {
        double t = Intersectiond.intersectRayPlane(
                origin,
                direction,
                point,
                normal,
                0.1);
        if (t < 0) {
            // didn't hit the front, try the back
            t = Intersectiond.intersectRayPlane(
                    origin,
                    direction,
                    point,
                    normal.negate(new Vector3d()),
                    0.1);
        }

        if (t >= 0 && t <= range) {
            return new SimpleIntersection(origin.fma(t, direction, new Vector3d()));
        } else {
            return null;
        }
    }

    @Override
    public @Nullable Intersection intersectBox(@NotNull Vector3dc min, @NotNull Vector3dc max) {
        Vector2d result = new Vector2d();
        int status = Intersectiond.intersectLineSegmentAab(origin, end, min, max, result);
        if (status != Intersectiond.OUTSIDE) {
            return new SimpleIntersection(origin.fma(result.x, direction, new Vector3d()));
        } else {
            return null;
        }
    }

    @Override
    public @Nullable Intersection intersectBox(@NotNull Vector3dc min, @NotNull Vector3dc max, @NotNull Vector3dc center, @NotNull Quaterniondc rotation) {
        if (rotation.equals(0.0, 0.0, 0.0, 1.0)) {
            return intersectBox(min, max);
        }

        Quaterniondc inverseRotation = rotation.conjugate(new Quaterniond());
        Vector3dc relativeOrigin = origin.sub(center, new Vector3d()).rotate(inverseRotation).add(center);
        Vector3dc relativeEnd = end.sub(center, new Vector3d()).rotate(inverseRotation).add(center);

        Vector2d result = new Vector2d();
        int status = Intersectiond.intersectLineSegmentAab(relativeOrigin, relativeEnd, min, max, result);
        if (status != Intersectiond.OUTSIDE) {
            return new SimpleIntersection(origin.fma(result.x, direction, new Vector3d()));
        } else {
            return null;
        }
    }

    @Override
    public @Nullable Intersection intersectCircle(@NotNull Vector3dc center, @NotNull Vector3dc axis, double radius, double threshold) {
        Intersection intersection = intersectPlane(center, axis);
        if (intersection == null) {
            return null;
        }

        Vector3dc planePosition = intersection.positionOnTarget();
        double distance = planePosition.distance(center);
        double circleDistance = Math.abs(distance - radius);

        if (circleDistance <= threshold) {
            Vector3d circlePosition = center.lerp(planePosition, radius / distance, new Vector3d());
            return new SimpleIntersection(planePosition, circlePosition);
        } else {
            return null;
        }
    }
}
