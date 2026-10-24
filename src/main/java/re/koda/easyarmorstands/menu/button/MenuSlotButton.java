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
package re.koda.easyarmorstands.menu.button;

import re.koda.easyarmorstands.api.menu.button.MenuButton;
import re.koda.easyarmorstands.api.menu.button.MenuButtonCategory;
import re.koda.easyarmorstands.api.menu.button.MenuIcon;
import re.koda.easyarmorstands.api.menu.click.MenuClickContext;
import re.koda.easyarmorstands.menu.slot.MenuButtonSlot;
import re.koda.easyarmorstands.menu.slot.MenuSlot;
import re.koda.easyarmorstands.platform.inventory.ItemStack;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Locale;

@NullMarked
public class MenuSlotButton implements MenuButton {
    private final Key key;
    private final MenuSlot slot;
    private final MenuButtonCategory category;

    private MenuSlotButton(Key key, MenuSlot slot, MenuButtonCategory category) {
        this.key = key;
        this.slot = slot;
        this.category = category;
    }

    public static MenuButton toButton(Key key, MenuSlot slot) {
        return toButton(key, slot, MenuButtonCategory.DEFAULT);
    }

    public static MenuButton toButton(Key key, MenuSlot slot, MenuButtonCategory category) {
        if (slot instanceof MenuButtonSlot menuButtonSlot) {
            return menuButtonSlot.getButton();
        } else {
            return new MenuSlotButton(key, slot, category);
        }
    }

    public MenuSlot getSlot() {
        return slot;
    }

    @Override
    public Key key() {
        return key;
    }

    @Override
    public MenuIcon icon() {
        throw new UnsupportedOperationException();
    }

    @Override
    public MenuButtonCategory category() {
        return category;
    }

    @Override
    public Component name() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Component> description() {
        throw new UnsupportedOperationException();
    }

    public @Nullable ItemStack getItem(Locale locale) {
        return slot.getItem(locale);
    }

    @Override
    public void onClick(MenuClickContext context) {
        throw new UnsupportedOperationException();
    }
}
