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
package re.koda.easyarmorstands.platform.entity;

import re.koda.easyarmorstands.platform.inventory.ItemStack;
import re.koda.easyarmorstands.platform.util.Location;
import re.koda.easyarmorstands.platform.world.World;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.Nullable;

import java.util.Set;
import java.util.UUID;

public interface Entity {
    World world();

    Location location();

    EntityType type();

    Entity copy(Location location);

    int id();

    UUID uniqueId();

    boolean isValid();

    double width();

    double height();

    void remove();

    boolean isGlowing();

    void setGlowing(boolean glowing);

    boolean teleport(Location location);

    void setFallDistance(float distance);

    Set<String> getScoreboardTags();

    boolean addScoreboardTag(String tag);

    boolean removeScoreboardTag(String tag);

    @Nullable EntitySnapshot createSnapshot();

    ItemStack getPickItemStack();

    @Nullable String getCustomDataString(Key key);

    void setCustomDataString(Key key, String value);

    void removeCustomData(Key key);

    boolean isSilent();

    void setSilent(boolean silent);

    @Nullable Component getCustomName();

    void setCustomName(@Nullable Component customName);

    boolean isCustomNameVisible();

    void setCustomNameVisible(boolean visible);

    boolean isPersistent();
}
