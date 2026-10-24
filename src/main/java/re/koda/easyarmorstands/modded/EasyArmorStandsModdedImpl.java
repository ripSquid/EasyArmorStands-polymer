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
package re.koda.easyarmorstands.modded;

import re.koda.easyarmorstands.EasyArmorStandsCommon;
import re.koda.easyarmorstands.api.property.Property;
import re.koda.easyarmorstands.color.ColorPicker;
import re.koda.easyarmorstands.command.sender.EasCommandSender;
import re.koda.easyarmorstands.menu.Menu;
import re.koda.easyarmorstands.message.TranslationManager;
import re.koda.easyarmorstands.modded.api.EasyArmorStandsModded;
import re.koda.easyarmorstands.modded.color.ModdedColorPickerContext;
import re.koda.easyarmorstands.modded.particle.ModdedParticleProviderFactory;
import re.koda.easyarmorstands.modded.session.ModdedSessionToolProvider;
import re.koda.easyarmorstands.particle.ParticleProviderFactory;
import re.koda.easyarmorstands.platform.entity.Entity;
import re.koda.easyarmorstands.platform.entity.Player;
import re.koda.easyarmorstands.platform.inventory.ItemStack;
import re.koda.easyarmorstands.platform.modded.ModdedPlatform;
import re.koda.easyarmorstands.platform.modded.entity.ModdedEntity;
import re.koda.easyarmorstands.platform.modded.entity.ModdedEntitySnapshot;
import re.koda.easyarmorstands.platform.modded.inventory.ModdedItemStack;
import re.koda.easyarmorstands.session.SessionToolProvider;
import re.koda.easyarmorstands.polymer.gizmo.PolymerGizmos;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.storage.TagValueOutput;
import org.incendo.cloud.CommandManager;

public abstract class EasyArmorStandsModdedImpl extends EasyArmorStandsCommon implements EasyArmorStandsModded {
    public static final ComponentLogger LOGGER = ComponentLogger.logger("EasyArmorStands");

    private final ModdedPlatform platform;
    private final ClassLoader classLoader;
    private final PolymerGizmos gizmos;
    private final ParticleProviderFactory particleProviderFactory;
    private final ModdedSessionToolProvider sessionToolProvider;

    public EasyArmorStandsModdedImpl(TranslationManager translationManager, ModdedPlatform platform, CommandManager<EasCommandSender> commandManager, PolymerGizmos gizmos, ClassLoader classLoader) {
        super(translationManager, platform, commandManager);
        this.platform = platform;
        this.classLoader = classLoader;
        this.gizmos = gizmos;
        this.particleProviderFactory = new ModdedParticleProviderFactory(gizmos);
        this.sessionToolProvider = new ModdedSessionToolProvider(this);
    }

    @Override
    public void update() {
        platform.update();
        super.update();
    }

    @Override
    public ModdedPlatform platform() {
        return platform;
    }

    @Override
    public ParticleProviderFactory particleProviderFactory() {
        return particleProviderFactory;
    }

    @Override
    public SessionToolProvider sessionToolProvider() {
        return sessionToolProvider;
    }

    @Override
    public ComponentLogger getLogger() {
        return LOGGER;
    }

    @Override
    public ClassLoader getClassLoader() {
        return classLoader;
    }

    @Override
    public Menu createColorPicker(Player player, Property<ItemStack> property) {
        return ColorPicker.create(this, player, new ModdedColorPickerContext(platform, property));
    }

    @Override
    public boolean isColorPickerSupported(ItemStack item) {
        return ModdedColorPickerContext.isSupported(item);
    }

    @Override
    public ItemStack createEntitySpawnEgg(Entity entity) {
        net.minecraft.world.entity.Entity nativeEntity = ModdedEntity.toNative(entity);
        net.minecraft.world.item.ItemStack item = nativeEntity.getPickResult();
        if (item == null) {
            return ModdedItemStack.empty(platform);
        }
        item = item.copy();
        CompoundTag tag;
        try (ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(nativeEntity.problemPath(), LOGGER)) {
            TagValueOutput output = TagValueOutput.createWithContext(reporter, nativeEntity.registryAccess());
            nativeEntity.saveAsPassenger(output);
            tag = output.buildResult();
        }
        ModdedEntitySnapshot.filterTag(tag);
        TypedEntityData<EntityType<?>> data = TypedEntityData.of(nativeEntity.getType(), tag);
        item.set(DataComponents.ENTITY_DATA, data);
        return ModdedItemStack.fromNative(platform, item);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        gizmos.close();
    }

    public boolean isTool(DataComponentGetter item) {
        return sessionToolProvider.isTool(item);
    }
}
