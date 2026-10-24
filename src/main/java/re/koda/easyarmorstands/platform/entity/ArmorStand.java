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

import re.koda.easyarmorstands.platform.inventory.EquipmentSlot;
import re.koda.easyarmorstands.platform.util.Rotations;

public interface ArmorStand extends LivingEntity {
    Rotations getHeadPose();

    void setHeadPose(Rotations rotations);

    Rotations getBodyPose();

    void setBodyPose(Rotations rotations);

    Rotations getLeftArmPose();

    void setLeftArmPose(Rotations rotations);

    Rotations getRightArmPose();

    void setRightArmPose(Rotations rotations);

    Rotations getLeftLegPose();

    void setLeftLegPose(Rotations rotations);

    Rotations getRightLegPose();

    void setRightLegPose(Rotations rotations);

    boolean isSmall();

    void setSmall(boolean small);

    boolean hasEquipmentLock(EquipmentSlot slot, LockType type);

    void addEquipmentLock(EquipmentSlot slot, LockType type);

    void removeEquipmentLock(EquipmentSlot slot, LockType type);

    boolean hasArms();

    void setArms(boolean arms);

    boolean hasBasePlate();

    void setBasePlate(boolean basePlate);

    boolean isMarker();

    void setMarker(boolean marker);

    boolean isVisible();

    void setVisible(boolean visible);

    boolean isCanTickSupported();

    boolean canTick();

    void setCanTick(boolean canTick);

    boolean hasGravity();

    void setGravity(boolean gravity);

    boolean isInvulnerable();

    void setInvulnerable(boolean invulnerable);

    enum LockType {
        ADDING_OR_CHANGING,
        REMOVING_OR_CHANGING,
        ADDING,
    }
}
