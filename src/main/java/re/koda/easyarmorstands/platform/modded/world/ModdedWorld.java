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
package re.koda.easyarmorstands.platform.modded.world;

import re.koda.easyarmorstands.platform.block.Block;
import re.koda.easyarmorstands.platform.entity.Entity;
import re.koda.easyarmorstands.platform.entity.EntityType;
import re.koda.easyarmorstands.platform.modded.ModdedAdapter;
import re.koda.easyarmorstands.platform.modded.ModdedPlatform;
import re.koda.easyarmorstands.platform.modded.ModdedPlatformHolder;
import re.koda.easyarmorstands.platform.modded.block.ModdedBlock;
import re.koda.easyarmorstands.platform.modded.entity.ModdedEntity;
import re.koda.easyarmorstands.platform.modded.entity.ModdedEntityType;
import re.koda.easyarmorstands.platform.util.Location;
import re.koda.easyarmorstands.platform.util.MappedCollection;
import re.koda.easyarmorstands.platform.util.MappedIterable;
import re.koda.easyarmorstands.platform.world.Chunk;
import re.koda.easyarmorstands.platform.world.World;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.platform.modcommon.MinecraftAudiences;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.phys.AABB;
import org.joml.Vector3dc;

import java.util.Collection;
import java.util.function.Consumer;

public interface ModdedWorld extends World, ModdedPlatformHolder {
    ServerLevel getNative();

    static ModdedWorld fromNative(ModdedPlatform platform, ServerLevel level) {
        return new ModdedWorldImpl(platform, level);
    }

    static ServerLevel toNative(World world) {
        return ((ModdedWorld) world).getNative();
    }

    @Override
    default Key key() {
        return MinecraftAudiences.key(getNative().dimension());
    }

    @Override
    default Iterable<Entity> getEntities() {
        ModdedPlatform platform = getPlatform();
        return new MappedIterable<>(getNative().getAllEntities(),
                e -> ModdedEntity.fromNative(platform, e));
    }

    @Override
    default Entity spawn(Location location, EntityType type, Consumer<Entity> configurer) {
        net.minecraft.world.entity.Entity entity = ModdedEntityType.toNative(type).create(getNative(), EntitySpawnReason.COMMAND);
        if (entity == null) {
            throw new IllegalArgumentException();
        }
        ModdedEntity moddedEntity = ModdedEntity.fromNative(getPlatform(), entity);
        configurer.accept(moddedEntity);
        getNative().addFreshEntity(entity);
        return moddedEntity;
    }

    @Override
    default Chunk getChunkAt(Location location) {
        return ModdedChunk.fromNative(getPlatform(), getNative().getChunkAt(ModdedAdapter.toBlockPos(location)));
    }

    @Override
    default Collection<Entity> getNearbyEntities(Location location, double dx, double dy, double dz) {
        ModdedPlatform platform = getPlatform();
        Vector3dc position = location.position();
        AABB bb = new AABB(
                position.x() - dx,
                position.y() - dy,
                position.z() - dz,
                position.x() + dx,
                position.y() + dy,
                position.z() + dz);
        return new MappedCollection<>(getNative().getEntities(null, bb),
                e -> ModdedEntity.fromNative(platform, e));
    }

    @Override
    default Block getBlock(Location location) {
        return ModdedBlock.fromNative(getPlatform(), getNative(), ModdedAdapter.toBlockPos(location));
    }
}
