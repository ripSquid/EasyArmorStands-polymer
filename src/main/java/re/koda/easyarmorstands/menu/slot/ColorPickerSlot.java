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
package re.koda.easyarmorstands.menu.slot;

import re.koda.easyarmorstands.EasyArmorStandsCommon;
import re.koda.easyarmorstands.api.EasyArmorStands;
import re.koda.easyarmorstands.api.element.MenuElement;
import re.koda.easyarmorstands.menu.Menu;
import re.koda.easyarmorstands.menu.click.MenuClick;
import re.koda.easyarmorstands.menu.click.MenuClickInterceptor;
import re.koda.easyarmorstands.message.MessageStyle;
import re.koda.easyarmorstands.platform.entity.Player;
import re.koda.easyarmorstands.platform.inventory.ItemStack;
import re.koda.easyarmorstands.registry.ItemTypeKeys;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

public class ColorPickerSlot implements MenuSlot, MenuClickInterceptor {
    public static final Key KEY = EasyArmorStands.key("color_picker");
    private final EasyArmorStandsCommon eas;
    private final MenuElement element;
    private boolean active;

    public ColorPickerSlot(EasyArmorStandsCommon eas, MenuElement element) {
        this.eas = eas;
        this.element = element;
    }

    @Override
    public ItemStack getItem(Locale locale) {
        ItemStack item = eas.platform().getItemType(ItemTypeKeys.BRUSH).createItemStack();
        if (active) {
            item = item.withEnchantmentGlintOverride(true);
            item = item.withLore(List.of(MenuButtonSlot.format(Component.translatable("easyarmorstands.menu.color-picker.click-target"), MessageStyle.BUTTON_DESCRIPTION, locale)));
        }
        item = item.withCustomName(MenuButtonSlot.format(Component.translatable("easyarmorstands.menu.color-picker.open"), MessageStyle.BUTTON_NAME, locale));
        return item;
    }

    @Override
    public void onClick(@NotNull MenuClick click) {
        if (active) {
            active = false;
            click.updateItem();
            return;
        }

        if (!click.isLeftClick() || !click.cursor().isEmpty()) {
            return;
        }

        Menu menu = click.menu();
        int size = menu.getSize();
        ItemPropertySlot foundSlot = null;
        int foundSlots = 0;
        for (int i = 0; i < size; i++) {
            MenuSlot slot = menu.getSlot(i);
            if (slot instanceof ItemPropertySlot itemSlot) {
                if (isApplicable(itemSlot)) {
                    foundSlot = itemSlot;
                    foundSlots++;
                }
            }
        }

        if (foundSlots == 1) {
            // Found exactly one applicable item property slot
            open(click.player(), foundSlot);
        } else {
            // Ask the user to click a slot
            active = true;
            click.interceptNextClick(this);
        }

        click.updateItem();
    }

    @Override
    public void interceptClick(@NotNull MenuClick click) {
        active = false;
        click.updateItem(this);

        if (!click.isLeftClick() || !click.cursor().isEmpty()) {
            return;
        }

        MenuSlot slot = click.slot();
        if (slot instanceof ItemPropertySlot itemSlot) {
            if (isApplicable(itemSlot)) {
                open(click.player(), itemSlot);
            }
        }
    }

    private void open(Player player, ItemPropertySlot itemSlot) {
        Menu menu = eas.createColorPicker(player, itemSlot.getProperty());
        menu.addCloseListener((p, m) -> element.openMenu(p));
        player.openInventory(menu.getInventory());
    }

    private boolean isApplicable(ItemPropertySlot slot) {
        ItemStack item = slot.getProperty().getValue();
        return eas.isColorPickerSupported(item);
    }
}
