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
package re.koda.easyarmorstands.api;

import re.koda.easyarmorstands.platform.entity.ArmorStand;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public enum ArmorStandSize {
    SMALL(0.25, 0.9875, Component.translatable("easyarmorstands.property.armor-stand.size.small")),
    NORMAL(0.5, 1.975, Component.translatable("easyarmorstands.property.armor-stand.size.normal"));

    private final double width;
    private final double height;
    private final Component displayName;

    ArmorStandSize(double width, double height, Component displayName) {
        this.width = width;
        this.height = height;
        this.displayName = displayName;
    }

    public static @NotNull ArmorStandSize get(boolean small) {
        return small ? SMALL : NORMAL;
    }

    public static @NotNull ArmorStandSize get(@NotNull ArmorStand entity) {
        return get(entity.isSmall());
    }

    @Override
    public String toString() {
        return name().toLowerCase(Locale.ROOT);
    }

    public boolean isSmall() {
        return this == SMALL;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public Component displayName() {
        return displayName;
    }
}
