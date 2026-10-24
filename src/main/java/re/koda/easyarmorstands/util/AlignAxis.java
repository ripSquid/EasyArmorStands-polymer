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

import org.joml.Vector3d;
import org.joml.Vector3dc;

public enum AlignAxis {
    X(true, false, false),
    Y(false, true, false),
    Z(false, false, true),
    XZ(true, false, true),
    ALL(true, true, true);

    private final boolean x;
    private final boolean y;
    private final boolean z;

    AlignAxis(boolean x, boolean y, boolean z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public boolean x() {
        return x;
    }

    public boolean y() {
        return y;
    }

    public boolean z() {
        return z;
    }

    public Vector3d snap(Vector3dc value, double increment, Vector3dc offset, Vector3d dest) {
        if (x) {
            dest.x = Util.snap(value.x() - offset.x(), increment) + offset.x();
        } else {
            dest.x = value.x();
        }
        if (y) {
            dest.y = Util.snap(value.y() - offset.y(), increment) + offset.y();
        } else {
            dest.y = value.y();
        }
        if (z) {
            dest.z = Util.snap(value.z() - offset.z(), increment) + offset.z();
        } else {
            dest.z = value.z();
        }
        return dest;
    }
}
