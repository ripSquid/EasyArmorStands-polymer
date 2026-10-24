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
package re.koda.easyarmorstands.platform.modded;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import re.koda.easyarmorstands.platform.Platform;
import re.koda.easyarmorstands.platform.block.BlockData;
import re.koda.easyarmorstands.platform.dialog.DialogResponseView;
import re.koda.easyarmorstands.platform.entity.Entity;
import re.koda.easyarmorstands.platform.entity.EntityType;
import re.koda.easyarmorstands.platform.entity.Pose;
import re.koda.easyarmorstands.platform.event.EventBus;
import re.koda.easyarmorstands.platform.event.EventBusImpl;
import re.koda.easyarmorstands.platform.inventory.ItemType;
import re.koda.easyarmorstands.platform.modded.block.ModdedBlockData;
import re.koda.easyarmorstands.platform.modded.dialog.ModdedDialogFactory;
import re.koda.easyarmorstands.platform.modded.dialog.ModdedDialogResponseView;
import re.koda.easyarmorstands.platform.modded.entity.ModdedEntity;
import re.koda.easyarmorstands.platform.modded.entity.ModdedEntityType;
import re.koda.easyarmorstands.platform.modded.entity.ModdedPose;
import re.koda.easyarmorstands.platform.modded.inventory.ModdedInventoryFactory;
import re.koda.easyarmorstands.platform.modded.inventory.ModdedItemType;
import re.koda.easyarmorstands.platform.modded.scheduler.ModdedScheduler;
import re.koda.easyarmorstands.platform.modded.world.ModdedWorld;
import re.koda.easyarmorstands.platform.scheduler.Scheduler;
import re.koda.easyarmorstands.platform.util.MappedCollection;
import re.koda.easyarmorstands.platform.util.MappedIterable;
import re.koda.easyarmorstands.platform.world.World;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.platform.modcommon.MinecraftAudiences;
import net.kyori.adventure.platform.modcommon.MinecraftServerAudiences;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.decoration.Mannequin;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public abstract class ModdedPlatform implements Platform {
    private @Nullable MinecraftServer server;
    private @Nullable MinecraftServerAudiences adventure;
    private final ComponentLogger logger;
    private final ModdedScheduler scheduler;
    private final EventBus eventBus = new EventBusImpl();
    private final ModdedClickActionRegistry clickActionRegistry = new ModdedClickActionRegistry();
    private final ModdedDialogFactory dialogFactory = new ModdedDialogFactory(this);
    private final ModdedInventoryFactory inventoryFactory = new ModdedInventoryFactory(this);

    private static final ScheduledExecutorService SCHEDULER = Executors.newSingleThreadScheduledExecutor(
            r -> {
                Thread thread = new Thread(r);
                thread.setName("easyarmorstands-platform-modded-scheduler");
                thread.setDaemon(true);
                return thread;
            }
    );

    protected ModdedPlatform(ComponentLogger logger) {
        this.logger = logger;
        this.scheduler = new ModdedScheduler();

        SCHEDULER.scheduleAtFixedRate(clickActionRegistry::clean, 0, 10, TimeUnit.SECONDS);
    }

    public void initialize(MinecraftServer server) {
        this.server = server;
        this.adventure = MinecraftServerAudiences.of(server);
    }

    public void update() {
        scheduler.execute();
    }

    private void throwNotInitialized() {
        throw new IllegalStateException("Not initialized");
    }

    public MinecraftServer getServer() {
        if (server == null) {
            throwNotInitialized();
        }
        return server;
    }

    public RegistryAccess getRegistryAccess() {
        return getServer().registryAccess();
    }

    public MinecraftServerAudiences getAdventure() {
        if (adventure == null) {
            throwNotInitialized();
        }
        return adventure;
    }

    public ComponentLogger getLogger() {
        return logger;
    }

    @Override
    public String getGameVersion() {
        return getServer().getServerVersion();
    }

    @Override
    public EventBus getEventBus() {
        return eventBus;
    }

    @Override
    public Scheduler getScheduler() {
        return scheduler;
    }

    @Override
    public EntityType getEntityType(Key key) {
        return getRegistryAccess().get(ResourceKey.create(Registries.ENTITY_TYPE, MinecraftAudiences.asNative(key)))
                .map(r -> ModdedEntityType.fromNative(this, r.value()))
                .orElseThrow(NoSuchElementException::new);
    }

    @Override
    public Iterable<EntityType> getEntityTypes() {
        return new MappedIterable<>(getRegistryAccess().lookupOrThrow(Registries.ENTITY_TYPE), t -> ModdedEntityType.fromNative(this, t));
    }

    @Override
    public ItemType getItemType(Key key) {
        return getRegistryAccess().get(ResourceKey.create(Registries.ITEM, MinecraftAudiences.asNative(key)))
                .map(r -> ModdedItemType.fromNative(this, r.value()))
                .orElseThrow(NoSuchElementException::new);
    }

    @Override
    public @Nullable World getWorld(Key key) {
        ServerLevel level = getServer().getLevel(ResourceKey.create(Registries.DIMENSION, MinecraftAudiences.asNative(key)));
        if (level == null) {
            return null;
        }
        return ModdedWorld.fromNative(this, level);
    }

    @Override
    public @Nullable Entity getEntity(UUID uniqueId) {
        for (ServerLevel level : getServer().getAllLevels()) {
            net.minecraft.world.entity.Entity entity = level.getEntity(uniqueId);
            if (entity != null) {
                return ModdedEntity.fromNative(this, entity);
            }
        }
        return null;
    }

    @Override
    public void checkMainThread() {
        if (!getServer().isSameThread()) {
            throw new IllegalStateException("Must be called on main thread");
        }
    }

    @Override
    public BlockData parseBlockData(String input) throws IllegalArgumentException {
        BlockStateParser.BlockResult result;
        try {
            result = BlockStateParser.parseForBlock(getRegistryAccess().lookupOrThrow(Registries.BLOCK), input, false);
        } catch (CommandSyntaxException e) {
            throw new IllegalArgumentException(e);
        }
        return ModdedBlockData.fromNative(this, result.blockState());
    }

    @Override
    public Collection<Pose> getValidMannequinPoses() {
        return new MappedCollection<>(Mannequin.VALID_POSES, p -> ModdedPose.fromNative(this, p));
    }

    @Override
    public ModdedDialogFactory getDialogFactory() {
        return dialogFactory;
    }

    @Override
    public ModdedInventoryFactory getInventoryFactory() {
        return inventoryFactory;
    }

    public Identifier registerCustomClickAction(Consumer<DialogResponseView> action, ClickCallback.Options options) {
        return clickActionRegistry.registerClickAction(action, options);
    }

    public boolean dispatchCustomClick(Identifier id, @Nullable Tag payload) {
        Consumer<DialogResponseView> consumer = clickActionRegistry.resolve(id);
        if (consumer == null) {
            return false;
        }
        Tag tag = Objects.requireNonNullElseGet(payload, CompoundTag::new);
        ModdedDialogResponseView view = ModdedDialogResponseView.fromNative(this, tag);
        consumer.accept(view);
        return true;
    }

    public abstract boolean hasPermission(ServerPlayer player, String permission);

    public abstract boolean hasPermission(CommandSourceStack stack, String permission);
}
