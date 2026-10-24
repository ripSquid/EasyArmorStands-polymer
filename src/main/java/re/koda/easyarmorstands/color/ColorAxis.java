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
package re.koda.easyarmorstands.color;

import re.koda.easyarmorstands.platform.color.RGBColor;
import re.koda.easyarmorstands.api.menu.button.MenuIcon;
import re.koda.easyarmorstands.message.Message;
import re.koda.easyarmorstands.platform.Platform;
import re.koda.easyarmorstands.registry.ItemTypeKeys;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.util.RGBLike;

public enum ColorAxis {
    RED(Message.component("easyarmorstands.color.red").color(NamedTextColor.RED), RGBLike::red, RGBColor::withRed, RGBColor.RED, ItemTypeKeys.RED_CONCRETE),
    GREEN(Message.component("easyarmorstands.color.green").color(NamedTextColor.GREEN), RGBLike::green, RGBColor::withGreen, RGBColor.GREEN, ItemTypeKeys.LIME_CONCRETE),
    BLUE(Message.component("easyarmorstands.color.blue").color(NamedTextColor.BLUE), RGBLike::blue, RGBColor::withBlue, RGBColor.BLUE, ItemTypeKeys.BLUE_CONCRETE);

    private final Component displayName;
    private final Getter getter;
    private final Setter setter;
    private final RGBColor color;
    private final Key itemTypeKey;

    ColorAxis(Component displayName, Getter getter, Setter setter, RGBColor color, Key itemTypeKey) {
        this.displayName = displayName;
        this.getter = getter;
        this.setter = setter;
        this.color = color;
        this.itemTypeKey = itemTypeKey;
    }

    public Component getDisplayName() {
        return displayName;
    }

    public int get(RGBLike color) {
        return getter.get(color);
    }

    public RGBColor set(RGBColor color, int value) {
        return setter.set(color, value);
    }

    public RGBColor getColor() {
        return color;
    }

    public MenuIcon getIcon(Platform platform) {
        return MenuIcon.of(platform.getItemType(itemTypeKey));
    }

    @FunctionalInterface
    private interface Getter {
        int get(RGBLike color);
    }

    @FunctionalInterface
    private interface Setter {
        RGBColor set(RGBColor color, int value);
    }
}
