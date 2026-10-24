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
package re.koda.easyarmorstands.platform.modded.entity;

import re.koda.easyarmorstands.platform.entity.Entity;
import re.koda.easyarmorstands.platform.entity.EntitySnapshot;
import re.koda.easyarmorstands.platform.modded.ModdedPlatform;
import re.koda.easyarmorstands.platform.modded.ModdedPlatformHolder;
import re.koda.easyarmorstands.platform.modded.world.ModdedWorld;
import re.koda.easyarmorstands.platform.util.Location;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityProcessor;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntitySpawnRequest;
import net.minecraft.world.entity.EntityType;
import org.joml.Vector3dc;

public interface ModdedEntitySnapshot extends EntitySnapshot, ModdedPlatformHolder {
    CompoundTag getNative();

    static ModdedEntitySnapshot fromNative(ModdedPlatform platform, CompoundTag tag) {
        CompoundTag compoundTag = tag.copy();
        filterTag(compoundTag);
        return new ModdedEntitySnapshotImpl(platform, compoundTag);
    }

    static void filterTag(CompoundTag tag) {
        tag.remove("Pos");
        tag.remove("Motion");
        tag.remove("sleeping_pos");
        tag.remove("UUID");
    }

    static CompoundTag toNative(EntitySnapshot snapshot) {
        return ((ModdedEntitySnapshot) snapshot).getNative();
    }

    @Override
    default Entity createEntity(Location location) {
        ServerLevel level = ModdedWorld.toNative(location.world());
        Vector3dc position = location.position();
        EntitySpawnRequest request = new EntitySpawnRequest(EntitySpawnReason.LOAD, false);
        net.minecraft.world.entity.Entity entity = EntityType.loadEntityRecursive(getNative(), level, request, EntityProcessor.NOP);
        if (entity == null) {
            throw new IllegalArgumentException();
        }
        entity.snapTo(position.x(), position.y(), position.z(), location.yaw(), location.pitch());
        level.addFreshEntity(entity);
        return ModdedEntity.fromNative(getPlatform(), entity);
    }
}
