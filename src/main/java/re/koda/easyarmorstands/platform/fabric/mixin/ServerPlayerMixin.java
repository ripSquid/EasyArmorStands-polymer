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
package re.koda.easyarmorstands.platform.fabric.mixin;

import com.mojang.authlib.GameProfile;
import re.koda.easyarmorstands.platform.fabric.FabricPlatformEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {
    private ServerPlayerMixin(Level level, GameProfile gameProfile) {
        super(level, gameProfile);
    }

    @Inject(method = "drop(Z)V", at = @At("HEAD"), cancellable = true)
    private void drop(boolean all, CallbackInfo ci) {
        if (FabricPlatformEvents.DROP_ITEM.invoker().onDropItem(this)) {
            containerMenu.sendAllDataToRemote();
            ci.cancel();
        }
    }

    @Inject(method = "doCloseContainer", at = @At("HEAD"))
    private void doCloseContainer(CallbackInfo ci) {
        FabricPlatformEvents.CLOSE_CONTAINER.invoker().onCloseContainer(this);
    }

    @Inject(method = "onItemPickup", at = @At("HEAD"))
    private void onItemPickup(ItemEntity entity, CallbackInfo ci) {
        FabricPlatformEvents.PICK_UP_ITEM.invoker().onPickUpItem(this, entity);
    }
}
