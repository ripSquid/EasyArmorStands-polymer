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

import net.kyori.adventure.util.ARGBLike;
import org.jetbrains.annotations.Range;

public interface ARGBColor extends ARGBLike {
    ARGBColor WHITE = ARGBColor.of(0xFF000000);

    static ARGBColor of(int value) {
        return new ARGBColorImpl(value);
    }

    static ARGBColor of(ARGBLike value) {
        return ARGBColor.of(value.alpha(), value.red(), value.green(), value.blue());
    }

    static ARGBColor of(int alpha, int red, int green, int blue) {
        return new ARGBColorImpl((alpha & 0xFF) << 24 | (red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF);
    }

    int value();

    @Override
    @Range(from = 0L, to = 255L)
    default int alpha() {
        return (value() >> 24) & 0xFF;
    }

    @Override
    @Range(from = 0L, to = 255L)
    default int red() {
        return (value() >> 16) & 0xFF;
    }

    @Override
    @Range(from = 0L, to = 255L)
    default int green() {
        return (value() >> 8) & 0xFF;
    }

    @Override
    @Range(from = 0L, to = 255L)
    default int blue() {
        return value() & 0xFF;
    }

    default ARGBColor withAlpha(int alpha) {
        return new ARGBColorImpl(value() & 0x00FFFFFF | (alpha & 0xFF) << 24);
    }

    default ARGBColor withRed(int red) {
        return new ARGBColorImpl(value() & 0xFF00FFFF | (red & 0xFF) << 16);
    }

    default ARGBColor withGreen(int green) {
        return new ARGBColorImpl(value() & 0xFFFF00FF | (green & 0xFF) << 8);
    }

    default ARGBColor withBlue(int blue) {
        return new ARGBColorImpl(value() & 0xFFFFFF00 | blue & 0xFF);
    }
}
