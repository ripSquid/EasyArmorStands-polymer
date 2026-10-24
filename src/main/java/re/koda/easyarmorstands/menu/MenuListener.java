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
import re.koda.easyarmorstands.api.editor.Session;
import re.koda.easyarmorstands.command.sender.EasPlayer;
import re.koda.easyarmorstands.menu.click.MenuClick;
import re.koda.easyarmorstands.menu.click.MenuClickInterceptor;
import re.koda.easyarmorstands.menu.slot.MenuSlot;
import re.koda.easyarmorstands.platform.entity.Player;
import re.koda.easyarmorstands.platform.event.callback.MenuClickCallback;
import re.koda.easyarmorstands.platform.event.callback.MenuDragCallback;
import re.koda.easyarmorstands.platform.inventory.Inventory;
import re.koda.easyarmorstands.platform.inventory.ItemStack;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.Nullable;

public class MenuListener implements MenuClickCallback, MenuDragCallback {
    private final EasyArmorStandsCommon eas;

    public MenuListener(EasyArmorStandsCommon eas) {
        this.eas = eas;
    }

    @Override
    public boolean onClick(Player player, Inventory inventory, int slot, ClickOptions options) {
        if (!(inventory.getHolder() instanceof Menu menu)) {
            return false;
        }

        if (slot >= menu.getSize()) {
            // Not the upper inventory
            return false;
        }

        menu.onClick(new SingleClick(eas, player, menu, slot, options));
        return true;
    }

    @Override
    public boolean onDrag(Player player, Inventory inventory) {
        // disallow dragging
        return inventory.getHolder() instanceof Menu;
    }

    private static abstract class Click implements MenuClick {
        private final Menu menu;
        private final @Nullable MenuSlot slot;
        private final int index;
        private final EasPlayer player;

        private Click(EasyArmorStandsCommon eas, Player player, Menu menu, int index) {
            this.menu = menu;
            this.slot = menu.getSlot(index);
            this.index = index;
            this.player = new EasPlayer(eas, player);
        }

        @Override
        public Menu menu() {
            return menu;
        }

        @Override
        public @Nullable MenuSlot slot() {
            return slot;
        }

        @Override
        public int index() {
            return index;
        }

        @Override
        public Player player() {
            return player.get();
        }

        @Override
        public ItemStack cursor() {
            return player.get().getItemOnCursor();
        }

        @Override
        public @Nullable Session session() {
            return player.session();
        }

        @Override
        public void close() {
            queueTask(() -> menu.close(player.get()));
        }

        @Override
        public void updateItem() {
            queueTask(() -> menu.updateItem(index));
        }

        @Override
        public void updateItem(MenuSlot slot) {
            queueTask(() -> menu.updateItem(slot));
        }

        @Override
        public void queueTask(Runnable task) {
            menu.queueTask(task);
        }

        @Override
        public void interceptNextClick(MenuClickInterceptor interceptor) {
            menu.interceptNextClick(interceptor);
        }

        @Override
        public Audience audience() {
            return player;
        }
    }

    private static class SingleClick extends Click {
        private final ClickOptions options;

        private SingleClick(EasyArmorStandsCommon eas, Player player, Menu menu, int index, ClickOptions options) {
            super(eas, player, menu, index);
            this.options = options;
        }

        @Override
        public boolean isLeftClick() {
            return options.left();
        }

        @Override
        public boolean isRightClick() {
            return options.right();
        }

        @Override
        public boolean isShiftClick() {
            return options.shift();
        }

        @Override
        public void updateItem() {
            queueTask(() -> menu().updateItem(index()));
        }
    }
}
