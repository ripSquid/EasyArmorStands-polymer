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

import re.koda.easyarmorstands.api.property.Property;
import re.koda.easyarmorstands.api.property.type.ArmorStandPropertyTypes;
import re.koda.easyarmorstands.api.property.type.PropertyType;
import re.koda.easyarmorstands.platform.entity.ArmorStand;
import re.koda.easyarmorstands.platform.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

public class ArmorStandLockProperty implements Property<Boolean> {
    private final ArmorStand entity;

    public ArmorStandLockProperty(ArmorStand entity) {
        this.entity = entity;
    }

    @Override
    public @NotNull PropertyType<Boolean> getType() {
        return ArmorStandPropertyTypes.LOCK;
    }

    @Override
    public @NotNull Boolean getValue() {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            for (ArmorStand.LockType type : ArmorStand.LockType.values()) {
                if (entity.hasEquipmentLock(slot, type)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean setValue(@NotNull Boolean value) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            for (ArmorStand.LockType type : ArmorStand.LockType.values()) {
                if (value) {
                    entity.addEquipmentLock(slot, type);
                } else {
                    entity.removeEquipmentLock(slot, type);
                }
            }
        }
        return true;
    }
}
