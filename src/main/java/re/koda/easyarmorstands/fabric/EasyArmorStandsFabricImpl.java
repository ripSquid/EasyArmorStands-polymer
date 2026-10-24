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

import re.koda.easyarmorstands.command.sender.EasCommandSender;
import re.koda.easyarmorstands.event.EventDispatcher;
import re.koda.easyarmorstands.fabric.api.EasyArmorStandsFabric;
import re.koda.easyarmorstands.fabric.event.FabricEventDispatcher;
import re.koda.easyarmorstands.message.TranslationManager;
import re.koda.easyarmorstands.modded.EasyArmorStandsModdedImpl;
import re.koda.easyarmorstands.platform.entity.Entity;
import re.koda.easyarmorstands.platform.modded.ModdedPlatform;
import re.koda.easyarmorstands.polymer.gizmo.PolymerGizmos;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.incendo.cloud.fabric.FabricServerCommandManager;

import java.nio.file.Path;

public class EasyArmorStandsFabricImpl extends EasyArmorStandsModdedImpl implements EasyArmorStandsFabric {
    private final FabricEventDispatcher eventDispatcher = new FabricEventDispatcher();

    public EasyArmorStandsFabricImpl(TranslationManager translationManager, ModdedPlatform platform, FabricServerCommandManager<EasCommandSender> commandManager) {
        super(translationManager, platform, commandManager, PolymerGizmos.create(), EasyArmorStandsFabricImpl.class.getClassLoader());
    }

    @Override
    public String getVersion() {
        return FabricLoader.getInstance().getModContainer("easyarmorstands-polymer")
                .map(ModContainer::getMetadata)
                .map(ModMetadata::getVersion)
                .map(Version::getFriendlyString)
                .orElse("Unknown");
    }

    @Override
    public Path getConfigFolder() {
        return FabricLoader.getInstance().getConfigDir().resolve("easyarmorstands");
    }

    @Override
    public EventDispatcher eventDispatcher() {
        return eventDispatcher;
    }

    @Override
    public boolean isIgnored(Entity entity) {
        return false;
    }
}
