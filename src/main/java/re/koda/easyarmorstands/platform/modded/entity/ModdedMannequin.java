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

import re.koda.easyarmorstands.platform.entity.Mannequin;
import re.koda.easyarmorstands.platform.entity.Pose;
import re.koda.easyarmorstands.platform.modded.ModdedAdapter;
import re.koda.easyarmorstands.platform.modded.ModdedPlatform;
import re.koda.easyarmorstands.platform.modded.profile.ModdedProfile;
import re.koda.easyarmorstands.platform.profile.Profile;
import re.koda.easyarmorstands.platform.util.MainHand;
import net.kyori.adventure.text.Component;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.player.PlayerModelPart;
import org.jspecify.annotations.Nullable;

public interface ModdedMannequin extends Mannequin, ModdedLivingEntity {
    @Override
    net.minecraft.world.entity.decoration.Mannequin getNative();

    static ModdedMannequin fromNative(ModdedPlatform platform, net.minecraft.world.entity.decoration.Mannequin entity) {
        return new ModdedMannequinImpl(platform, entity);
    }

    static net.minecraft.world.entity.decoration.Mannequin toNative(Mannequin entity) {
        return ((ModdedMannequin) entity).getNative();
    }

    @Override
    default MainHand getMainHand() {
        return ModdedAdapter.fromNative(getNative().getMainArm());
    }

    @Override
    default void setMainHand(MainHand mainHand) {
        getNative().setMainArm(ModdedAdapter.toNative(mainHand));
    }

    @Override
    default Profile getProfile() {
        return ModdedProfile.fromNative(getPlatform(), getNative().getProfile());
    }

    @Override
    default void setProfile(Profile profile) {
        getNative().setProfile(ModdedProfile.toNative(profile));
    }

    @Override
    default boolean isImmovable() {
        return getNative().getImmovable();
    }

    @Override
    default void setImmovable(boolean immovable) {
        getNative().setImmovable(immovable);
    }

    @Override
    default @Nullable Component getDescription() {
        return getPlatform().getAdventure().asAdventure(getNative().getDescription());
    }

    @Override
    default void setDescription(@Nullable Component description) {
        getNative().setHideDescription(description == null);
        if (description != null) {
            getNative().setDescription(getPlatform().getAdventure().asNative(description));
        }
    }

    @Override
    default Pose getPose() {
        return ModdedPose.fromNative(getPlatform(), getNative().getPose());
    }

    @Override
    default void setPose(Pose pose) {
        getNative().setPose(ModdedPose.toNative(pose));
    }

    default boolean isPartVisible(PlayerModelPart part) {
        return getNative().isModelPartShown(part);
    }

    default void setPartVisible(PlayerModelPart part, boolean visible) {
        int value = getNative().getEntityData().get(Avatar.DATA_PLAYER_MODE_CUSTOMISATION);
        if (visible) {
            value |= part.getMask();
        } else {
            value &= ~part.getMask();
        }
        getNative().getEntityData().set(Avatar.DATA_PLAYER_MODE_CUSTOMISATION, (byte) value);
    }

    @Override
    default boolean isCapeVisible() {
        return isPartVisible(PlayerModelPart.CAPE);
    }

    @Override
    default void setCapeVisible(boolean visible) {
        setPartVisible(PlayerModelPart.CAPE, visible);
    }

    @Override
    default boolean isJacketVisible() {
        return isPartVisible(PlayerModelPart.JACKET);
    }

    @Override
    default void setJacketVisible(boolean visible) {
        setPartVisible(PlayerModelPart.JACKET, visible);
    }

    @Override
    default boolean isLeftSleeveVisible() {
        return isPartVisible(PlayerModelPart.LEFT_SLEEVE);
    }

    @Override
    default void setLeftSleeveVisible(boolean visible) {
        setPartVisible(PlayerModelPart.LEFT_SLEEVE, visible);
    }

    @Override
    default boolean isRightSleeveVisible() {
        return isPartVisible(PlayerModelPart.RIGHT_SLEEVE);
    }

    @Override
    default void setRightSleeveVisible(boolean visible) {
        setPartVisible(PlayerModelPart.RIGHT_SLEEVE, visible);
    }

    @Override
    default boolean isLeftPantsVisible() {
        return isPartVisible(PlayerModelPart.LEFT_PANTS_LEG);
    }

    @Override
    default void setLeftPantsVisible(boolean visible) {
        setPartVisible(PlayerModelPart.LEFT_PANTS_LEG, visible);
    }

    @Override
    default boolean isRightPantsVisible() {
        return isPartVisible(PlayerModelPart.RIGHT_PANTS_LEG);
    }

    @Override
    default void setRightPantsVisible(boolean visible) {
        setPartVisible(PlayerModelPart.RIGHT_PANTS_LEG, visible);
    }

    @Override
    default boolean isHatVisible() {
        return isPartVisible(PlayerModelPart.HAT);
    }

    @Override
    default void setHatVisible(boolean visible) {
        setPartVisible(PlayerModelPart.HAT, visible);
    }
}
