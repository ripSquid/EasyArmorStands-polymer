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

import re.koda.easyarmorstands.api.menu.button.MenuButton;
import re.koda.easyarmorstands.api.menu.button.MenuIcon;
import re.koda.easyarmorstands.api.menu.click.MenuClickContext;
import re.koda.easyarmorstands.menu.color.ColorPickerContext;
import re.koda.easyarmorstands.menu.layout.MenuLayoutRule;
import re.koda.easyarmorstands.platform.color.RGBColor;
import re.koda.easyarmorstands.util.Util;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public class ColorAxisButton implements MenuButton {
    private final ColorPickerContext context;
    private final Key key;
    private final ColorAxis axis;

    public ColorAxisButton(ColorPickerContext context, Key key, ColorAxis axis) {
        this.context = context;
        this.key = key;
        this.axis = axis;
    }

    public static MenuLayoutRule rule(ColorAxis axis) {
        return button -> button instanceof ColorAxisButton colorAxisButton && colorAxisButton.axis == axis;
    }

    @Override
    public Key key() {
        return key;
    }

    @Override
    public MenuIcon icon() {
        return axis.getIcon(context.platform());
    }

    @Override
    public Component name() {
        return axis.getDisplayName();
    }

    @Override
    public List<Component> description() {
        RGBColor color = context.getColor();
        int value = axis.get(color);
        return List.of(
                Component.text(value),
                Util.formatColor(color));
    }

    @Override
    public void onClick(MenuClickContext context) {
    }
}
