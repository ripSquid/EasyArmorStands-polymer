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
package re.koda.easyarmorstands.session;

import re.koda.easyarmorstands.EasyArmorStandsCommon;
import re.koda.easyarmorstands.api.editor.context.ClickContext;
import re.koda.easyarmorstands.api.element.Element;
import re.koda.easyarmorstands.command.sender.EasPlayer;
import re.koda.easyarmorstands.history.action.ElementCreateAction;
import re.koda.easyarmorstands.history.action.ElementDestroyAction;
import re.koda.easyarmorstands.platform.block.Block;
import re.koda.easyarmorstands.platform.entity.ArmorStand;
import re.koda.easyarmorstands.platform.entity.Entity;
import re.koda.easyarmorstands.platform.entity.Player;
import re.koda.easyarmorstands.platform.event.callback.MenuCloseCallback;
import re.koda.easyarmorstands.platform.event.callback.PlayerAddCallback;
import re.koda.easyarmorstands.platform.event.callback.PlayerDestroyEntityCallback;
import re.koda.easyarmorstands.platform.event.callback.PlayerDropItemCallback;
import re.koda.easyarmorstands.platform.event.callback.PlayerLeftClickBlockCallback;
import re.koda.easyarmorstands.platform.event.callback.PlayerLeftClickCallback;
import re.koda.easyarmorstands.platform.event.callback.PlayerLeftClickEntityCallback;
import re.koda.easyarmorstands.platform.event.callback.PlayerPickUpItemCallback;
import re.koda.easyarmorstands.platform.event.callback.PlayerPlacedEntityCallback;
import re.koda.easyarmorstands.platform.event.callback.PlayerRemoveCallback;
import re.koda.easyarmorstands.platform.event.callback.PlayerReplaceCallback;
import re.koda.easyarmorstands.platform.event.callback.PlayerRightClickBlockCallback;
import re.koda.easyarmorstands.platform.event.callback.PlayerRightClickCallback;
import re.koda.easyarmorstands.platform.event.callback.PlayerRightClickEntityCallback;
import re.koda.easyarmorstands.platform.event.callback.PlayerSwapHandsCallback;
import re.koda.easyarmorstands.platform.event.callback.PlayerSwitchSelectedSlotCallback;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class SessionListener implements
        MenuCloseCallback,
        PlayerAddCallback,
        PlayerDestroyEntityCallback,
        PlayerDropItemCallback,
        PlayerLeftClickBlockCallback,
        PlayerLeftClickCallback,
        PlayerLeftClickEntityCallback,
        PlayerPickUpItemCallback,
        PlayerPlacedEntityCallback,
        PlayerRemoveCallback,
        PlayerReplaceCallback,
        PlayerRightClickBlockCallback,
        PlayerRightClickCallback,
        PlayerRightClickEntityCallback,
        PlayerSwapHandsCallback,
        PlayerSwitchSelectedSlotCallback {
    private final EasyArmorStandsCommon eas;

    public SessionListener(EasyArmorStandsCommon eas) {
        this.eas = eas;
    }

    private boolean handleClick(Player player, ClickContext.Type type, @Nullable Entity entity, @Nullable Block block) {
        return eas.handleClick(player, type, entity, block);
    }

    public boolean handleLeftClick(Player player, @Nullable Entity entity, @Nullable Block block) {
        return handleClick(player, ClickContext.Type.LEFT_CLICK, entity, block);
    }

    private boolean handleRightClick(Player player, @Nullable Entity entity, @Nullable Block block) {
        return handleClick(player, ClickContext.Type.RIGHT_CLICK, entity, block);
    }

    public boolean handleSwap(Player player) {
        return handleClick(player, ClickContext.Type.SWAP_HANDS, null, null);
    }

    public boolean handleDrop(Player player) {
        scheduleUpdateHeldItem(player);
        return handleClick(player, ClickContext.Type.DROP, null, null);
    }

    private void scheduleUpdateHeldItem(Player player) {
        eas.platform().getScheduler().runTask(() -> eas.sessionManager().updateHeldItem(player));
    }

    @Override
    public void onAddPlayer(Player player) {
        scheduleUpdateHeldItem(player);
    }

    @Override
    public void onRemovePlayer(Player player) {
        eas.sessionManager().stopSession(player);
        eas.clipboardManager().remove(player);
        eas.historyManager().remove(player);
    }

    @Override
    public void onReplacePlayer(Player newPlayer) {
        eas.sessionManager().replacePlayer(newPlayer);
        eas.clipboardManager().replacePlayer(newPlayer);
    }

    @Override
    public boolean onPlayerSwapHands(Player player) {
        return handleSwap(player);
    }

    @Override
    public boolean onPlayerLeftClick(Player player) {
        return handleLeftClick(player, null, null);
    }

    @Override
    public boolean onPlayerLeftClickBlock(Player player, Block block) {
        return handleLeftClick(player, null, block);
    }

    @Override
    public boolean onPlayerLeftClickEntity(Player player, Entity entity) {
        return handleLeftClick(player, entity, null);
    }

    @Override
    public boolean onPlayerRightClick(Player player) {
        return handleRightClick(player, null, null);
    }

    @Override
    public boolean onPlayerRightClickBlock(Player player, Block block) {
        return handleRightClick(player, null, block);
    }

    @Override
    public boolean onPlayerRightClickEntity(Player player, Entity entity) {
        return handleRightClick(player, entity, null);
    }

    @Override
    public boolean onPlayerDropItem(Player player) {
        return handleDrop(player);
    }

    @Override
    public void onPlayerSwitchSelectedSlot(Player player) {
        scheduleUpdateHeldItem(player);
    }

    @Override
    public void onPlayerPickUpItem(Player player) {
        scheduleUpdateHeldItem(player);
    }

    @Override
    public void onMenuClose(Player player) {
        scheduleUpdateHeldItem(player);
    }

    @Override
    public void onPlayerPlacedEntity(Player player, Entity entity) {
        if (!(entity instanceof ArmorStand)) {
            return;
        }
        EasPlayer context = new EasPlayer(eas, player);
        Element element = eas.getElement(entity);
        if (element != null) {
            context.history().push(new ElementCreateAction(eas, element));
            context.clipboard().handleAutoApply(element);
        }
    }

    @Override
    public void onPlayerDestroyEntity(Player player, Entity entity) {
        if (!(entity instanceof ArmorStand)) {
            return;
        }
        Element element = eas.getElement(entity);
        if (element == null) {
            return;
        }
        eas.getHistory(player).push(new ElementDestroyAction(eas, element));
    }
}
