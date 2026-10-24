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
package re.koda.easyarmorstands.platform.modded.inventory;

import re.koda.easyarmorstands.platform.inventory.ItemStack;
import re.koda.easyarmorstands.platform.inventory.PlayerInventory;
import re.koda.easyarmorstands.platform.modded.ModdedPlatform;
import net.kyori.adventure.text.Component;
import net.minecraft.network.protocol.game.ClientboundSetHeldSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;

import java.util.HashMap;

public interface ModdedPlayerInventory extends PlayerInventory, ModdedInventory {
    Inventory getNative();

    static ModdedPlayerInventory fromNative(ModdedPlatform platform, Inventory inventory) {
        return new ModdedPlayerInventoryImpl(platform, inventory);
    }

    static Inventory toNative(PlayerInventory inventory) {
        return ((ModdedPlayerInventory) inventory).getNative();
    }

    @Override
    default Component getTitle() {
        return Component.empty();
    }

    @Override
    default HashMap<Integer, ItemStack> addItem(ItemStack... items) {
        Inventory inventory = getNative();
        HashMap<Integer, ItemStack> remaining = new HashMap<>();
        for (int i = 0; i < items.length; i++) {
            net.minecraft.world.item.ItemStack item = ModdedItemStack.toNative(items[i]);
            if (inventory.add(item) && item.isEmpty()) {
                continue;
            }
            remaining.put(i, ModdedItemStack.fromNative(getPlatform(), item));
        }
        return remaining;
    }

    @Override
    default ItemStack getItemInMainHand() {
        return ModdedItemStack.fromNative(getPlatform(), getNative().player.getItemInHand(InteractionHand.MAIN_HAND));
    }

    @Override
    default ItemStack getItemInOffHand() {
        return ModdedItemStack.fromNative(getPlatform(), getNative().player.getItemInHand(InteractionHand.OFF_HAND));
    }

    @Override
    default void setItemInMainHand(ItemStack item) {
        getNative().player.setItemInHand(InteractionHand.MAIN_HAND, ModdedItemStack.toNative(item));
    }

    @Override
    default void setHeldItemSlot(int i) {
        Inventory inventory = getNative();
        inventory.setSelectedSlot(i);
        if (inventory.player instanceof ServerPlayer player) {
            player.connection.send(new ClientboundSetHeldSlotPacket(inventory.getSelectedSlot()));
        }
    }
}
