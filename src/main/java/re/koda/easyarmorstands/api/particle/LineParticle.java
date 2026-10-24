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
package re.koda.easyarmorstands.api.particle;

import re.koda.easyarmorstands.api.Axis;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaterniond;
import org.joml.Quaterniondc;
import org.joml.Vector3d;
import org.joml.Vector3dc;

@ApiStatus.NonExtendable
public interface LineParticle extends ColoredParticle {
    /**
     * Sets the center, rotation, offset and length to connect two points with this line.
     *
     * @param from The first point of the line.
     * @param to   The second point of the line.
     */
    default void setFromTo(@NotNull Vector3dc from, @NotNull Vector3dc to) {
        Vector3dc delta = to.sub(from, new Vector3d());
        if (delta.lengthSquared() < 1e-4) {
            setCenter(from);
            setRotation(new Quaterniond());
            setLength(0);
            setOffset(0);
            return;
        }
        Quaterniondc rotation = new Quaterniond().rotationTo(getAxis().getDirection(), delta);
        double length = delta.length();
        setCenter(from);
        setRotation(rotation);
        setLength(length);
        setOffset(length / 2);
    }

    @NotNull Vector3dc getCenter();

    void setCenter(@NotNull Vector3dc center);

    @NotNull Axis getAxis();

    void setAxis(@NotNull Axis axis);

    double getWidth();

    void setWidth(double width);

    @NotNull Quaterniondc getRotation();

    void setRotation(@NotNull Quaterniondc rotation);

    double getLength();

    void setLength(double length);

    double getOffset();

    void setOffset(double offset);
}
