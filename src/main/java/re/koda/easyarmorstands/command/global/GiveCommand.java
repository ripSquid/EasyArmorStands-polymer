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
package re.koda.easyarmorstands.command.global;

import re.koda.easyarmorstands.EasyArmorStandsCommon;
import re.koda.easyarmorstands.command.sender.EasCommandSender;
import re.koda.easyarmorstands.command.sender.EasPlayer;
import re.koda.easyarmorstands.command.util.MultiplePlayerSelector;
import re.koda.easyarmorstands.message.Message;
import re.koda.easyarmorstands.permission.Permissions;
import re.koda.easyarmorstands.platform.entity.Player;
import re.koda.easyarmorstands.platform.inventory.ItemStack;
import re.koda.easyarmorstands.platform.inventory.PlayerInventory;
import net.kyori.adventure.audience.Audience;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.CommandDescription;
import org.incendo.cloud.annotations.Permission;
import org.incendo.cloud.annotations.processing.CommandContainer;

import java.util.HashMap;

@CommandContainer
public class GiveCommand {
    @Command("eas give <player>")
    @Permission(Permissions.GIVE_OTHER)
    @CommandDescription("easyarmorstands.command.description.give.other")
    public void give(EasCommandSender sender, EasyArmorStandsCommon eas,
                     @Argument("player") MultiplePlayerSelector selector) {
        for (Player player : selector.values()) {
            HashMap<Integer, ItemStack> failed = player.getInventory().addItem(eas.sessionToolProvider().createTool(player.locale()));
            for (ItemStack item : failed.values()) {
                player.dropItem(item);
            }
            sendAddedOther(sender, player);
        }
    }

    @Command("eas give")
    @Permission(Permissions.GIVE)
    @CommandDescription("easyarmorstands.command.description.give")
    public void legacyGive(EasPlayer sender, EasyArmorStandsCommon eas) {
        give(sender, eas);
        sender.sendMessage(Message.hint("easyarmorstands.hint.give-tool", Message.command("/eas")));
    }

    @Command("eas")
    @Permission(Permissions.GIVE)
    @CommandDescription("easyarmorstands.command.description.give")
    public void give(EasPlayer sender, EasyArmorStandsCommon eas) {
        Player player = sender.get();
        PlayerInventory inventory = player.getInventory();

        // if tool is already selected: do nothing
        if (eas.sessionManager().isHoldingTool(player)) {
            sendSelected(sender);
            eas.sessionManager().updateHeldItem(player);
            return;
        }

        // attempt to select a tool which is already in the inventory
        if (selectExistingTool(inventory, eas)) {
            sendSelected(sender);
            eas.sessionManager().updateHeldItem(player);
            return;
        }

        if (inventory.getItemInMainHand().isEmpty()) {
            // put the tool into the selected slot
            inventory.setItemInMainHand(eas.sessionToolProvider().createTool(sender.locale()));
        } else {
            // put the tool into any slot
            HashMap<Integer, ItemStack> failed = inventory.addItem(eas.sessionToolProvider().createTool(sender.locale()));
            if (!failed.isEmpty()) {
                sendFull(sender);
                return;
            }
            selectExistingTool(inventory, eas);
        }

        sendAdded(sender);
        eas.sessionManager().updateHeldItem(player);
    }

    private boolean selectExistingTool(PlayerInventory inventory, EasyArmorStandsCommon eas) {
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (!eas.sessionToolProvider().isTool(item)) {
                continue;
            }

            if (i < 9) {
                // tool is already in the hotbar, select it
                inventory.setHeldItemSlot(i);
                return true;
            }

            if (inventory.getItemInMainHand().isEmpty()) {
                // selected slot is empty, move the tool into it
                inventory.clear(i);
                inventory.setItemInMainHand(item);
                return true;
            }

            for (int j = 0; j < 9; j++) {
                ItemStack existingItem = inventory.getItem(j);
                if (existingItem != null && !existingItem.isEmpty()) {
                    continue;
                }
                // found an empty hotbar slot, move the tool into it
                inventory.clear(i);
                inventory.setItem(j, item);
                inventory.setHeldItemSlot(j);
                return true;
            }

            // hotbar is full: swap tool with selected slot
            ItemStack existingItem = inventory.getItemInMainHand();
            inventory.setItemInMainHand(item);
            inventory.setItem(i, existingItem);
            return true;
        }
        return false;
    }

    private void sendAdded(Audience sender) {
        sender.sendMessage(Message.success("easyarmorstands.success.added-tool-to-inventory"));
    }

    private void sendAddedOther(Audience sender, Player player) {
        sender.sendMessage(Message.success("easyarmorstands.success.added-tool-to-inventory.other", player.displayName()));
    }

    private void sendSelected(Audience sender) {
        sender.sendMessage(Message.success("easyarmorstands.success.selected-tool"));
    }

    private void sendFull(Audience sender) {
        sender.sendMessage(Message.error("easyarmorstands.error.inventory-full"));
    }
}
