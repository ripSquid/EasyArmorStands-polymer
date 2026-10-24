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
package re.koda.easyarmorstands.api.formatter;

import re.koda.easyarmorstands.api.util.EasFormat;
import re.koda.easyarmorstands.platform.util.Rotations;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class RotationsFormatter implements ValueFormatter<Rotations> {
    private static String formatAngle(double angle) {
        return EasFormat.ANGLE_FORMAT.format(angle);
    }

    @Override
    public Component format(Rotations value) {
        return Component.text()
                .append(Component.text(formatAngle(value.x()), NamedTextColor.RED))
                .append(Component.text(", "))
                .append(Component.text(formatAngle(value.y()), NamedTextColor.GREEN))
                .append(Component.text(", "))
                .append(Component.text(formatAngle(value.z()), NamedTextColor.BLUE))
                .build();
    }

    @Override
    public String formatAsString(Rotations value) {
        double x = value.x();
        double y = value.y();
        double z = value.z();
        return x + "," + y + "," + z;
    }
}
