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
package re.koda.easyarmorstands.editor.layer;

import re.koda.easyarmorstands.EasyArmorStandsCommon;
import re.koda.easyarmorstands.api.element.Element;
import re.koda.easyarmorstands.api.element.ElementDiscoveryEntry;
import re.koda.easyarmorstands.api.element.ElementDiscoverySource;
import re.koda.easyarmorstands.api.util.BoundingBox;
import re.koda.easyarmorstands.platform.entity.Entity;
import re.koda.easyarmorstands.platform.entity.Player;
import re.koda.easyarmorstands.platform.util.Location;
import re.koda.easyarmorstands.platform.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import org.joml.Vector3dc;

import java.util.function.Consumer;

public class EntityElementDiscoverySource implements ElementDiscoverySource {
    private final EasyArmorStandsCommon eas;
    private final Player player;

    public EntityElementDiscoverySource(EasyArmorStandsCommon eas, @NotNull Player player) {
        this.eas = eas;
        this.player = player;
    }

    public @NotNull ElementDiscoveryEntry getEntry(@NotNull Entity entity) {
        return new EntityElementDiscoveryEntry(this, player, entity);
    }

    public @Nullable Element getElement(@NotNull Entity entity) {
        return eas.getElement(entity);
    }

    @Override
    public void discover(@NotNull World world, @NotNull BoundingBox box, @NotNull Consumer<@NotNull ElementDiscoveryEntry> consumer) {
        Vector3dc delta = box.getMaxPosition().sub(box.getMinPosition(), new Vector3d()).div(2);
        Vector3dc center = box.getMinPosition().add(delta, new Vector3d());
        Location location = Location.of(world, center);
        for (Entity entity : world.getNearbyEntities(location, delta.x(), delta.y(), delta.z())) {
            if (entity.equals(player)) {
                continue;
            }
            consumer.accept(getEntry(entity));
        }
    }
}
