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
package re.koda.easyarmorstands.property.armorstand;

import re.koda.easyarmorstands.api.ArmorStandPart;
import re.koda.easyarmorstands.api.property.Property;
import re.koda.easyarmorstands.api.property.type.ArmorStandPropertyTypes;
import re.koda.easyarmorstands.api.property.type.PropertyType;
import re.koda.easyarmorstands.platform.entity.ArmorStand;
import re.koda.easyarmorstands.platform.util.Rotations;
import org.jetbrains.annotations.NotNull;

public class ArmorStandPoseProperty implements Property<Rotations> {
    private final ArmorStand entity;
    private final ArmorStandPart part;
    private final PropertyType<Rotations> type;

    public ArmorStandPoseProperty(ArmorStand entity, ArmorStandPart part) {
        this.entity = entity;
        this.part = part;
        this.type = ArmorStandPropertyTypes.POSE.get(part);
    }

    @Override
    public @NotNull PropertyType<Rotations> getType() {
        return type;
    }

    @Override
    public @NotNull Rotations getValue() {
        return part.getPose(entity);
    }

    @Override
    public boolean setValue(@NotNull Rotations value) {
        part.setPose(entity, value);
        return true;
    }
}
