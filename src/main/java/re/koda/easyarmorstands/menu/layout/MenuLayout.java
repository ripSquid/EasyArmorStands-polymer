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
package re.koda.easyarmorstands.menu.layout;

import re.koda.easyarmorstands.EasyArmorStandsCommon;
import re.koda.easyarmorstands.api.EasyArmorStands;
import re.koda.easyarmorstands.api.menu.button.MenuButton;
import re.koda.easyarmorstands.api.menu.button.MenuButtonCategory;
import re.koda.easyarmorstands.menu.Menu;
import re.koda.easyarmorstands.menu.MenuCreator;
import re.koda.easyarmorstands.menu.button.DestroyButton;
import re.koda.easyarmorstands.menu.button.MenuButtonFactory;
import re.koda.easyarmorstands.menu.button.MenuSlotButton;
import re.koda.easyarmorstands.menu.slot.BackgroundSlot;
import re.koda.easyarmorstands.menu.slot.MenuButtonSlot;
import re.koda.easyarmorstands.platform.inventory.EquipmentSlot;
import re.koda.easyarmorstands.platform.inventory.ItemStack;
import re.koda.easyarmorstands.platform.inventory.ItemType;
import re.koda.easyarmorstands.registry.ItemTypeKeys;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Queue;
import java.util.Set;

public class MenuLayout {
    public static final MenuButtonFactory PRIMARY_BACKGROUND = createBackground(ItemTypeKeys.LIGHT_BLUE_STAINED_GLASS_PANE);
    public static final MenuButtonFactory SECONDARY_BACKGROUND = createBackground(ItemTypeKeys.YELLOW_STAINED_GLASS_PANE);
    private final EasyArmorStandsCommon eas;
    private final List<RuleEntry> rules;

    MenuLayout(EasyArmorStandsCommon eas, List<RuleEntry> rules) {
        this.eas = eas;
        this.rules = rules;
    }

    public static MenuButtonFactory createBackground(Key itemTypeKey) {
        return e -> createBackground(e.platform().getItemType(itemTypeKey));
    }

    public static MenuButton createBackground(ItemType itemType) {
        ItemStack item = itemType.createItemStack().withHideTooltip(true);
        return MenuSlotButton.toButton(
                EasyArmorStands.key("background"),
                new BackgroundSlot(item));
    }

    public static MenuLayout createDefault(EasyArmorStandsCommon eas) {
        MenuLayoutBuilder builder = new MenuLayoutBuilder(eas);
        builder.addRule(0, 8, b -> b instanceof DestroyButton);
        for (int column = 0; column < 9; column++) {
            builder.addRule(0, column, MenuLayoutRule.category(MenuButtonCategory.HEADER));
        }
        for (int column = 0; column < 9; column++) {
            builder.addRule(5, column, MenuLayoutRule.category(MenuButtonCategory.FOOTER));
        }
        for (int row = 1; row < 5; row++) {
            for (int column = 0; column < 9; column++) {
                builder.addRule(row, column, MenuLayoutRule.matchAll());
            }
        }
        for (int column = 0; column < 9; column++) {
            builder.addRule(0, column, MenuLayoutRule.background(SECONDARY_BACKGROUND));
        }
        for (int row = 1; row < 5; row++) {
            for (int column = 0; column < 9; column++) {
                builder.addRule(row, column, MenuLayoutRule.background(PRIMARY_BACKGROUND));
            }
        }
        for (int column = 0; column < 9; column++) {
            builder.addRule(5, column, MenuLayoutRule.background(SECONDARY_BACKGROUND));
        }
        return builder.build();
    }

    public static MenuLayout createEquipment(EasyArmorStandsCommon eas) {
        MenuLayoutBuilder builder = new MenuLayoutBuilder(eas);
        builder.addRule(0, 8, b -> b instanceof DestroyButton);
        for (int column = 0; column < 9; column++) {
            builder.addRule(0, column, MenuLayoutRule.category(MenuButtonCategory.HEADER));
        }
        for (int column = 4; column < 9; column++) {
            builder.addRule(5, column, MenuLayoutRule.category(MenuButtonCategory.FOOTER));
        }
        builder.addRule(2, 1, MenuLayoutRule.equipmentSlot(EquipmentSlot.HEAD));
        builder.addRule(3, 0, MenuLayoutRule.equipmentSlot(EquipmentSlot.OFF_HAND));
        builder.addRule(3, 1, MenuLayoutRule.equipmentSlot(EquipmentSlot.CHEST));
        builder.addRule(3, 2, MenuLayoutRule.equipmentSlot(EquipmentSlot.HAND));
        builder.addRule(4, 1, MenuLayoutRule.equipmentSlot(EquipmentSlot.LEGS));
        builder.addRule(5, 1, MenuLayoutRule.equipmentSlot(EquipmentSlot.FEET));
        for (int row = 1; row < 5; row++) {
            for (int column = 4; column < 9; column++) {
                builder.addRule(row, column, MenuLayoutRule.matchAll());
            }
        }
        for (int column = 0; column < 9; column++) {
            builder.addRule(0, column, MenuLayoutRule.background(SECONDARY_BACKGROUND));
        }
        for (int row = 1; row < 5; row++) {
            for (int column = 0; column < 9; column++) {
                builder.addRule(row, column, MenuLayoutRule.background(PRIMARY_BACKGROUND));
            }
        }
        for (int column = 0; column < 4; column++) {
            builder.addRule(5, column, MenuLayoutRule.background(PRIMARY_BACKGROUND));
        }
        for (int column = 4; column < 9; column++) {
            builder.addRule(5, column, MenuLayoutRule.background(SECONDARY_BACKGROUND));
        }
        return builder.build();
    }

    public static MenuLayout createSimple(EasyArmorStandsCommon eas) {
        MenuLayoutBuilder builder = new MenuLayoutBuilder(eas);
        for (int row = 0; row < 6; row++) {
            for (int column = 0; column < 9; column++) {
                builder.addRule(row, column, MenuLayoutRule.matchAll());
            }
        }
        for (int row = 0; row < 6; row++) {
            for (int column = 0; column < 9; column++) {
                builder.addRule(row, column, MenuLayoutRule.background(PRIMARY_BACKGROUND));
            }
        }
        return builder.build();
    }

    public Menu createMenu(Component title, Locale locale, Collection<MenuButton> buttons) {
        MenuCreator creator = new MenuCreator(eas, locale);
        creator.setTitle(title);
        Set<Integer> filledSlots = new HashSet<>();
        LinkedList<MenuButton> queue = new LinkedList<>(buttons);
        for (RuleEntry entry : rules) {
            int row = entry.row();
            int column = entry.column();
            int index = 9 * row + column;
            if (filledSlots.contains(index)) {
                continue;
            }
            MenuLayoutRule rule = entry.rule();
            MenuButton button = matchButton(rule, queue);
            if (button != null) {
                filledSlots.add(index);
                creator.setSlot(index, MenuButtonSlot.toSlot(button));
            }
        }
        return creator.build();
    }

    private @Nullable MenuButton matchButton(MenuLayoutRule rule, Queue<MenuButton> queue) {
        Iterator<MenuButton> iterator = queue.iterator();
        while (iterator.hasNext()) {
            MenuButton button = iterator.next();
            if (rule.matches(button)) {
                iterator.remove();
                return button;
            }
        }
        MenuButtonFactory fallback = rule.fallback();
        if (fallback != null) {
            return fallback.create(eas);
        }
        return null;
    }

    record RuleEntry(int row, int column, MenuLayoutRule rule) {
    }
}
