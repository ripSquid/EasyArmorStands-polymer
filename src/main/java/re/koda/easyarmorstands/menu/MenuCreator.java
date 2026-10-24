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
package re.koda.easyarmorstands.menu;

import re.koda.easyarmorstands.EasyArmorStandsCommon;
import re.koda.easyarmorstands.menu.slot.BackgroundSlot;
import re.koda.easyarmorstands.menu.slot.MenuSlot;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class MenuCreator {
    private final EasyArmorStandsCommon eas;
    private final Locale locale;
    private final Map<Integer, MenuSlot> slots = new TreeMap<>();
    private Component title = Component.empty();
    private int height = 1;
    private MenuSlot background;

    public MenuCreator(EasyArmorStandsCommon eas, Locale locale) {
        this.eas = eas;
        this.locale = locale;
    }

    public boolean isEmpty(int index) {
        return !slots.containsKey(index);
    }

    public MenuSlot setSlot(int index, MenuSlot slot) {
        MenuSlot old = slots.put(index, slot);
        if (!(slot instanceof BackgroundSlot)) {
            height = Math.max(height, index / 9 + 1);
        }
        return old;
    }

    public void setTitle(@NotNull Component title) {
        this.title = title;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        if (this.height < height) {
            this.height = height;
        }
    }

    public void setBackground(MenuSlot background) {
        this.background = background;
    }

    public @NotNull Menu build() {
        Objects.requireNonNull(title, "Title not set");
        MenuSlot[] slots = new MenuSlot[height * 9];
        for (Map.Entry<Integer, MenuSlot> entry : this.slots.entrySet()) {
            int index = entry.getKey();
            if (index >= slots.length) {
                continue;
            }
            slots[index] = entry.getValue();
        }
        for (int i = 0; i < height * 9; i++) {
            if (slots[i] == null) {
                slots[i] = background;
            }
        }
        return new MenuImpl(eas, title, slots, locale);
    }
}
