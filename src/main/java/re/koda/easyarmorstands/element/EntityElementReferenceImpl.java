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
package re.koda.easyarmorstands.element;

import re.koda.easyarmorstands.api.element.EntityElement;
import re.koda.easyarmorstands.api.element.EntityElementReference;
import re.koda.easyarmorstands.api.element.EntityElementType;
import re.koda.easyarmorstands.platform.Platform;
import re.koda.easyarmorstands.platform.entity.Entity;
import re.koda.easyarmorstands.platform.util.Location;
import re.koda.easyarmorstands.platform.world.World;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3dc;

import java.util.UUID;

public class EntityElementReferenceImpl<E extends Entity> implements EntityElementReference<E> {
    private final Platform platform;
    private final EntityElementType<E> type;
    private final Key worldKey;
    private final Vector3dc position;
    private UUID id;

    public EntityElementReferenceImpl(Platform platform, EntityElementType<E> type, Entity entity) {
        Location location = entity.location();
        this.platform = platform;
        this.type = type;
        this.worldKey = location.world().key();
        this.position = location.position();
        this.id = entity.uniqueId();
    }

    @Override
    public @NotNull EntityElementType<E> getType() {
        return type;
    }

    @Override
    public @Nullable EntityElement<E> getElement() {
        // Load chunk at the expected position
        World world = platform.getWorld(worldKey);
        if (world != null) {
            world.getChunkAt(Location.of(world, position));
        }

        Entity entity = platform.getEntity(id);
        if (entity == null) {
            return null;
        }

        if (!entity.type().equals(type.getEntityType())) {
            return null;
        }

        return type.getElement(type.getEntityClass().cast(entity));
    }

    @Override
    public void onEntityReplaced(@NotNull UUID oldId, @NotNull UUID newId) {
        if (id.equals(oldId)) {
            id = newId;
        }
    }

    @Override
    public @NotNull UUID getId() {
        return id;
    }
}
