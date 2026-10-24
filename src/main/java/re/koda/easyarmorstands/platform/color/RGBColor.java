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
package re.koda.easyarmorstands.platform.color;

import net.kyori.adventure.util.RGBLike;
import org.jetbrains.annotations.Range;

public interface RGBColor extends RGBLike {
    RGBColor RED = RGBColor.of(0xFF0000);
    RGBColor GREEN = RGBColor.of(0x00FF00);
    RGBColor BLUE = RGBColor.of(0x0000FF);
    RGBColor WHITE = RGBColor.of(0xFFFFFF);

    static RGBColor of(int value) {
        return new RGBColorImpl(value & 0xFFFFFF);
    }

    static RGBColor of(RGBLike value) {
        return RGBColor.of(value.red(), value.green(), value.blue());
    }

    static RGBColor of(int red, int green, int blue) {
        return new RGBColorImpl((red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF);
    }

    int value();

    @Override
    @Range(from = 0L, to = 255L)
    default int red() {
        return value() >> 16 & 0xFF;
    }

    @Override
    @Range(from = 0L, to = 255L)
    default int green() {
        return value() >> 8 & 0xFF;
    }

    @Override
    @Range(from = 0L, to = 255L)
    default int blue() {
        return value() & 0xFF;
    }

    default RGBColor withRed(int red) {
        return new RGBColorImpl(value() & 0x00FFFF | (red & 0xFF) << 16);
    }

    default RGBColor withGreen(int green) {
        return new RGBColorImpl(value() & 0xFF00FF | (green & 0xFF) << 8);
    }

    default RGBColor withBlue(int blue) {
        return new RGBColorImpl(value() & 0xFFFF00 | blue & 0xFF);
    }
}
