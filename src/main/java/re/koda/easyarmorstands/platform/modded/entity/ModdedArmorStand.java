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

import re.koda.easyarmorstands.platform.entity.ArmorStand;
import re.koda.easyarmorstands.platform.inventory.EquipmentSlot;
import re.koda.easyarmorstands.platform.modded.ModdedAdapter;
import re.koda.easyarmorstands.platform.modded.ModdedPlatform;
import re.koda.easyarmorstands.platform.util.Rotations;

public interface ModdedArmorStand extends ArmorStand, ModdedLivingEntity {
    @Override
    net.minecraft.world.entity.decoration.ArmorStand getNative();

    static ModdedArmorStand fromNative(ModdedPlatform platform, net.minecraft.world.entity.decoration.ArmorStand entity) {
        return new ModdedArmorStandImpl(platform, entity);
    }

    static net.minecraft.world.entity.decoration.ArmorStand toNative(ArmorStand entity) {
        return ((ModdedArmorStand) entity).getNative();
    }

    @Override
    default float yaw() {
        return getNative().getYRot();
    }

    @Override
    default Rotations getHeadPose() {
        return ModdedAdapter.fromNative(getNative().getHeadPose());
    }

    @Override
    default void setHeadPose(Rotations rotations) {
        getNative().setHeadPose(ModdedAdapter.toNative(rotations));
    }

    @Override
    default Rotations getBodyPose() {
        return ModdedAdapter.fromNative(getNative().getBodyPose());
    }

    @Override
    default void setBodyPose(Rotations rotations) {
        getNative().setBodyPose(ModdedAdapter.toNative(rotations));
    }

    @Override
    default Rotations getLeftArmPose() {
        return ModdedAdapter.fromNative(getNative().getLeftArmPose());
    }

    @Override
    default void setLeftArmPose(Rotations rotations) {
        getNative().setLeftArmPose(ModdedAdapter.toNative(rotations));
    }

    @Override
    default Rotations getRightArmPose() {
        return ModdedAdapter.fromNative(getNative().getRightArmPose());
    }

    @Override
    default void setRightArmPose(Rotations rotations) {
        getNative().setRightArmPose(ModdedAdapter.toNative(rotations));
    }

    @Override
    default Rotations getLeftLegPose() {
        return ModdedAdapter.fromNative(getNative().getLeftLegPose());
    }

    @Override
    default void setLeftLegPose(Rotations rotations) {
        getNative().setLeftLegPose(ModdedAdapter.toNative(rotations));
    }

    @Override
    default Rotations getRightLegPose() {
        return ModdedAdapter.fromNative(getNative().getRightLegPose());
    }

    @Override
    default void setRightLegPose(Rotations rotations) {
        getNative().setRightLegPose(ModdedAdapter.toNative(rotations));
    }

    @Override
    default boolean isSmall() {
        return getNative().isSmall();
    }

    @Override
    default void setSmall(boolean small) {
        getNative().setSmall(small);
    }

    @Override
    default boolean hasEquipmentLock(EquipmentSlot slot, LockType type) {
        return (getNative().disabledSlots & ModdedAdapter.toMask(slot, type)) != 0;
    }

    @Override
    default void addEquipmentLock(EquipmentSlot slot, LockType type) {
        getNative().disabledSlots |= ModdedAdapter.toMask(slot, type);
    }

    @Override
    default void removeEquipmentLock(EquipmentSlot slot, LockType type) {
        getNative().disabledSlots &= ~ModdedAdapter.toMask(slot, type);
    }

    @Override
    default boolean hasArms() {
        return getNative().showArms();
    }

    @Override
    default void setArms(boolean arms) {
        getNative().setShowArms(arms);
    }

    @Override
    default boolean hasBasePlate() {
        return getNative().showBasePlate();
    }

    @Override
    default void setBasePlate(boolean basePlate) {
        getNative().setNoBasePlate(!basePlate);
    }

    @Override
    default boolean isMarker() {
        return getNative().isMarker();
    }

    @Override
    default void setMarker(boolean marker) {
        getNative().setMarker(marker);
    }

    @Override
    default boolean isVisible() {
        return !getNative().isInvisible();
    }

    @Override
    default void setVisible(boolean visible) {
        getNative().setInvisible(!visible);
    }

    @Override
    default boolean isCanTickSupported() {
        return false;
    }

    @Override
    default boolean canTick() {
        return true;
    }

    @Override
    default void setCanTick(boolean canTick) {
    }

    @Override
    default boolean hasGravity() {
        return !getNative().isNoGravity();
    }

    @Override
    default void setGravity(boolean gravity) {
        getNative().setNoGravity(!gravity);
    }

    @Override
    default boolean isInvulnerable() {
        return getNative().isPermanentlyInvulnerable();
    }

    @Override
    default void setInvulnerable(boolean invulnerable) {
        getNative().setPermanentlyInvulnerable(invulnerable);
    }
}
