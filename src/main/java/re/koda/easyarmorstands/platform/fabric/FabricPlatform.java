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
package re.koda.easyarmorstands.platform.fabric;

import me.lucko.fabric.api.permissions.v0.Permissions;
import re.koda.easyarmorstands.platform.modded.ModdedPlatform;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

public class FabricPlatform extends ModdedPlatform {
    public FabricPlatform(ComponentLogger logger) {
        super(logger);
        new FabricPlatformListener(this);
        FabricPermissionBridge.register();
    }

    @Override
    public String getName() {
        return FabricLoader.getInstance().getModContainer("fabricloader")
                .map(ModContainer::getMetadata)
                .map(ModMetadata::getName)
                .orElse("Fabric");
    }

    @Override
    public String getVersion() {
        return FabricLoader.getInstance().getModContainer("fabricloader")
                .map(ModContainer::getMetadata)
                .map(ModMetadata::getVersion)
                .map(Version::getFriendlyString)
                .orElse("Unknown");
    }

    @Override
    public boolean hasPermission(ServerPlayer player, String permission) {
        return Permissions.check(player, permission, getServer().operatorUserPermissions().level());
    }

    @Override
    public boolean hasPermission(CommandSourceStack stack, String permission) {
        return Permissions.check(stack, permission, getServer().operatorUserPermissions().level());
    }
}
