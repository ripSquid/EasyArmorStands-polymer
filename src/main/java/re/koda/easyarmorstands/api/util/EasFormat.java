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
package re.koda.easyarmorstands.api.util;

import re.koda.easyarmorstands.platform.util.Location;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3dc;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public final class EasFormat {
    public static final NumberFormat POSITION_FORMAT = new DecimalFormat("0.0000");
    public static final NumberFormat ANGLE_FORMAT = new DecimalFormat("+0.00°;-0.00°");
    public static final NumberFormat SCALE_FORMAT = new DecimalFormat("0.0000");

    private EasFormat() {
    }

    private static @NotNull Component format3D(@NotNull Vector3dc vector, @NotNull NumberFormat format) {
        return Component.text()
                .append(Component.text(format.format(vector.x()), NamedTextColor.RED))
                .append(Component.text(", "))
                .append(Component.text(format.format(vector.y()), NamedTextColor.GREEN))
                .append(Component.text(", "))
                .append(Component.text(format.format(vector.z()), NamedTextColor.BLUE))
                .build();
    }

    public static @NotNull Component formatPosition(@NotNull Vector3dc position) {
        return format3D(position, POSITION_FORMAT);
    }

    public static @NotNull Component formatLocation(@NotNull Location location) {
        return formatPosition(location.position());
    }

    public static @NotNull Component formatDegrees(double degrees) {
        return Component.text(ANGLE_FORMAT.format(EasMath.wrapDegrees(degrees)));
    }

    public static @NotNull Component formatScale(double scale) {
        return Component.text(SCALE_FORMAT.format(scale));
    }
}
