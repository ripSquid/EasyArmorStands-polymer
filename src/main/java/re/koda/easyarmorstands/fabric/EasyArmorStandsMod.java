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
package re.koda.easyarmorstands.fabric;

import re.koda.easyarmorstands.EasyArmorStandsCommon;
import re.koda.easyarmorstands.EasyArmorStandsHolder;
import re.koda.easyarmorstands.command.parser.ArgumentParserProvider;
import re.koda.easyarmorstands.command.sender.CommandSenderMapper;
import re.koda.easyarmorstands.command.sender.EasCommandSender;
import re.koda.easyarmorstands.fabric.api.EasyArmorStandsFabricHolder;
import re.koda.easyarmorstands.message.TranslationManager;
import re.koda.easyarmorstands.modded.EasyArmorStandsModdedImpl;
import re.koda.easyarmorstands.modded.command.ModdedArgumentParserProvider;
import re.koda.easyarmorstands.modded.command.ModdedCommandSourceStackMapper;
import re.koda.easyarmorstands.modded.util.MainThreadExecutor;
import re.koda.easyarmorstands.platform.fabric.FabricPlatform;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.component.DataComponentGetter;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.fabric.FabricServerCommandManager;

public class EasyArmorStandsMod implements ModInitializer {
    private final MainThreadExecutor executor = new MainThreadExecutor(null);
    private final EasyArmorStandsHolder holder = new EasyArmorStandsHolder();

    @Override
    public void onInitialize() {
        FabricPlatform platform = new FabricPlatform(EasyArmorStandsModdedImpl.LOGGER);

        FabricServerCommandManager<EasCommandSender> commandManager = new FabricServerCommandManager<>(
                ExecutionCoordinator.coordinatorFor(executor),
                new ModdedCommandSourceStackMapper(platform, new CommandSenderMapper(holder)));

        ArgumentParserProvider parserProvider = new ModdedArgumentParserProvider(platform);
        EasyArmorStandsCommon.registerCommands(commandManager, parserProvider, getClass().getClassLoader(), holder);

        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            platform.initialize(server);
            executor.setServer(server);

            TranslationManager translationManager = new TranslationManager();
            translationManager.load(null, EasyArmorStandsModdedImpl.LOGGER);

            EasyArmorStandsFabricImpl eas = new EasyArmorStandsFabricImpl(translationManager, platform, commandManager);
            holder.initialize(eas);
            EasyArmorStandsFabricHolder.setInstance(eas);
            eas.onLoad();
        });

        ServerLifecycleEvents.SERVER_STOPPED.register(_ -> {
            EasyArmorStandsFabricImpl instance = (EasyArmorStandsFabricImpl) EasyArmorStandsFabricHolder.getInstance();
            if (instance != null) {
                instance.onDisable();
            }
            EasyArmorStandsFabricHolder.setInstance(null);
            holder.initialize(null);
            executor.setServer(null);
        });

        ServerTickEvents.END_SERVER_TICK.register(_ -> {
            if (holder.isInitialized()) {
                holder.get().update();
            }
        });
    }

    public static boolean isTool(DataComponentGetter item) {
        EasyArmorStandsModdedImpl eas = (EasyArmorStandsModdedImpl) EasyArmorStandsFabricHolder.getInstance();
        if (eas == null) {
            return false;
        }
        return eas.isTool(item);
    }
}
