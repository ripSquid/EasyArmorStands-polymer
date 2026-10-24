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
import re.koda.easyarmorstands.menu.click.MenuClick;
import re.koda.easyarmorstands.menu.click.MenuClickInterceptor;
import re.koda.easyarmorstands.menu.slot.MenuSlot;
import re.koda.easyarmorstands.platform.entity.Player;
import re.koda.easyarmorstands.platform.inventory.Inventory;
import re.koda.easyarmorstands.platform.inventory.InventoryHolder;
import re.koda.easyarmorstands.platform.inventory.ItemStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.translation.GlobalTranslator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

public class MenuImpl implements Menu, InventoryHolder {
    private final EasyArmorStandsCommon eas;
    private final Inventory inventory;
    private final MenuSlot[] slots;
    private final Locale locale;
    private final List<MenuCloseListener> closeListeners = new ArrayList<>();
    private MenuClickInterceptor currentInterceptor;

    public MenuImpl(EasyArmorStandsCommon eas, Component title, MenuSlot[] slots, Locale locale) {
        this.eas = eas;
        this.locale = locale;
        this.inventory = eas.platform().getInventoryFactory().createInventory(this, GlobalTranslator.render(title, locale), slots.length);
        this.slots = slots;
        updateItems();
    }

    @Override
    public MenuSlot getSlot(int index) {
        if (index < 0 || index >= slots.length) {
            return null;
        }
        return slots[index];
    }

    @Override
    public int getSize() {
        return slots.length;
    }

    @Override
    public void onClick(@NotNull MenuClick click) {
        MenuSlot slot = click.slot();
        if (slot == null) {
            return;
        }

        MenuClickInterceptor interceptor = currentInterceptor;
        if (interceptor != null) {
            currentInterceptor = null;
            interceptor.interceptClick(click);
            return;
        }

        slot.onClick(click);
    }

    public void updateItems() {
        for (int i = 0; i < slots.length; i++) {
            updateItem(i);
        }
    }

    @Override
    public void updateItems(@NotNull Predicate<@NotNull MenuSlot> predicate) {
        for (int i = 0; i < slots.length; i++) {
            MenuSlot slot = slots[i];
            if (slot != null && predicate.test(slot)) {
                inventory.setItem(i, slot.getItem(locale));
            }
        }
    }

    @Override
    public @Nullable ItemStack getItem(int index) {
        MenuSlot slot = slots[index];
        if (slot != null) {
            return slot.getItem(locale);
        }
        return null;
    }

    @Override
    public void updateItem(int index) {
        MenuSlot slot = slots[index];
        if (slot != null) {
            inventory.setItem(index, slot.getItem(locale));
        }
    }

    @Override
    public void updateItem(@NotNull MenuSlot slot) {
        ItemStack item = slot.getItem(locale);
        for (int i = 0; i < slots.length; i++) {
            if (slots[i] == slot) {
                inventory.setItem(i, item);
            }
        }
    }

    @Override
    public void queueTask(@NotNull Runnable task) {
        eas.platform().getScheduler().runTask(task);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void interceptNextClick(@NotNull MenuClickInterceptor interceptor) {
        currentInterceptor = interceptor;
    }

    @Override
    public void addCloseListener(@NotNull MenuCloseListener listener) {
        closeListeners.add(listener);
    }

    @Override
    public void close(@NotNull Player player) {
        for (MenuCloseListener listener : closeListeners) {
            listener.onClose(player, this);
        }
        if (player.isInventoryOpen(inventory)) {
            player.closeInventory();
        }
    }
}
