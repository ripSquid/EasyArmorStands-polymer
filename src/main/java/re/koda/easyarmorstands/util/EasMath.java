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
package re.koda.easyarmorstands.util;

import re.koda.easyarmorstands.platform.util.Location;
import org.joml.Math;
import org.joml.Quaterniond;
import org.joml.Vector3d;
import org.joml.Vector3dc;

public class EasMath {
    private static final Vector3d temp = new Vector3d();

    private EasMath() {
    }

    public static Vector3d getDirection(Location location) {
        float yaw = Math.toRadians(location.yaw());
        float pitch = Math.toRadians(location.pitch());
        double xz = Math.cos(pitch);
        double x = -xz * Math.sin(yaw);
        double y = -Math.sin(pitch);
        double z = xz * Math.cos(yaw);
        return new Vector3d(x, y, z);
    }

    public static double wrapDegrees(double degrees) {
        degrees %= 360;
        if (degrees > 180) {
            degrees -= 360;
        } else if (degrees < -180) {
            degrees += 360;
        }
        return degrees;
    }

    public static Quaterniond getEntityYawRotation(float yaw, Quaterniond dest) {
        return dest.rotationY(-Math.toRadians(yaw));
    }

    public static Quaterniond getInverseEntityYawRotation(float yaw, Quaterniond dest) {
        return getEntityYawRotation(-yaw, dest);
    }

    public static Quaterniond getEntityRotation(float yaw, float pitch, Quaterniond dest) {
        return getEntityYawRotation(yaw, dest)
                .rotateX(Math.toRadians(pitch));
    }

    public static Quaterniond getEntityRotation(Location location, Quaterniond dest) {
        return getEntityRotation(location.yaw(), location.pitch(), dest);
    }

    public static Quaterniond getInverseEntityRotation(float yaw, float pitch, Quaterniond dest) {
        return getEntityRotation(yaw, pitch, dest)
                .conjugate();
    }

    public static double getOffsetAlongLine(Vector3dc linePoint, Vector3dc lineDirection, Vector3dc point) {
        return point.sub(linePoint, temp).dot(lineDirection);
    }

    public static Vector3d getClosestPointOnLine(Vector3dc linePoint, Vector3dc lineDirection, Vector3dc point, Vector3d dest) {
        return linePoint.fma(getOffsetAlongLine(linePoint, lineDirection, point), lineDirection, dest);
    }
}
