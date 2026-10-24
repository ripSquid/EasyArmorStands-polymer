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
package re.koda.easyarmorstands.platform.util;

import re.koda.easyarmorstands.platform.world.World;
import org.joml.Vector3d;
import org.joml.Vector3dc;

public interface Location {
    static Location of(World world, Vector3dc position, float yaw, float pitch) {
        return new LocationImpl(world, new Vector3d(position), yaw, pitch);
    }

    static Location of(World world, Vector3dc position) {
        return new LocationImpl(world, new Vector3d(position), 0, 0);
    }

    World world();

    Vector3dc position();

    float yaw();

    float pitch();

    default Location withPosition(Vector3dc position) {
        return new LocationImpl(world(), new Vector3d(position), yaw(), pitch());
    }

    default Location withYaw(float yaw) {
        return new LocationImpl(world(), position(), yaw, pitch());
    }

    default Location withPitch(float pitch) {
        return new LocationImpl(world(), position(), yaw(), pitch);
    }
}
