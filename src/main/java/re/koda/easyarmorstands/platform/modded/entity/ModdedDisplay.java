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

import re.koda.easyarmorstands.platform.color.RGBColor;
import re.koda.easyarmorstands.platform.entity.Display;
import re.koda.easyarmorstands.platform.modded.ModdedAdapter;
import re.koda.easyarmorstands.platform.modded.ModdedPlatform;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

public interface ModdedDisplay extends Display, ModdedEntity {
    @Override
    net.minecraft.world.entity.Display getNative();

    static ModdedDisplay fromNative(ModdedPlatform platform, net.minecraft.world.entity.Display entity) {
        return switch (entity) {
            case net.minecraft.world.entity.Display.BlockDisplay e -> ModdedBlockDisplay.fromNative(platform, e);
            case net.minecraft.world.entity.Display.ItemDisplay e -> ModdedItemDisplay.fromNative(platform, e);
            case net.minecraft.world.entity.Display.TextDisplay e -> ModdedTextDisplay.fromNative(platform, e);
            default -> new ModdedDisplayImpl(platform, entity);
        };
    }

    static net.minecraft.world.entity.Display toNative(Display entity) {
        return ((ModdedDisplay) entity).getNative();
    }

    @Override
    default float getDisplayWidth() {
        return getNative().getWidth();
    }

    @Override
    default void setDisplayWidth(float width) {
        getNative().setWidth(width);
    }

    @Override
    default float getDisplayHeight() {
        return getNative().getHeight();
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    default void setDisplayHeight(float height) {
        getNative().setHeight(height);
    }

    @Override
    default Vector3fc getTranslation() {
        return getNative().getEntityData().get(net.minecraft.world.entity.Display.DATA_TRANSLATION_ID);
    }

    @Override
    default void setTranslation(Vector3fc translation) {
        getNative().getEntityData().set(net.minecraft.world.entity.Display.DATA_TRANSLATION_ID, new Vector3f(translation));
    }

    @Override
    default Quaternionfc getLeftRotation() {
        return getNative().getEntityData().get(net.minecraft.world.entity.Display.DATA_LEFT_ROTATION_ID);
    }

    @Override
    default void setLeftRotation(Quaternionfc rotation) {
        getNative().getEntityData().set(net.minecraft.world.entity.Display.DATA_LEFT_ROTATION_ID, new Quaternionf(rotation));
    }

    @Override
    default Vector3fc getScale() {
        return getNative().getEntityData().get(net.minecraft.world.entity.Display.DATA_SCALE_ID);
    }

    @Override
    default void setScale(Vector3fc scale) {
        getNative().getEntityData().set(net.minecraft.world.entity.Display.DATA_SCALE_ID, new Vector3f(scale));
    }

    @Override
    default Quaternionfc getRightRotation() {
        return getNative().getEntityData().get(net.minecraft.world.entity.Display.DATA_RIGHT_ROTATION_ID);
    }

    @Override
    default void setRightRotation(Quaternionfc rotation) {
        getNative().getEntityData().set(net.minecraft.world.entity.Display.DATA_RIGHT_ROTATION_ID, new Quaternionf(rotation));
    }

    @Override
    default Billboard getBillboard() {
        return ModdedAdapter.fromNative(getNative().getBillboardConstraints());
    }

    @Override
    default void setBillboard(Billboard billboard) {
        getNative().setBillboardConstraints(ModdedAdapter.toNative(billboard));
    }

    @Override
    default @Nullable Brightness getBrightness() {
        net.minecraft.util.Brightness brightness = getNative().getBrightnessOverride();
        if (brightness == null) {
            return null;
        }
        return ModdedAdapter.fromNative(brightness);
    }

    @Override
    default void setBrightness(@Nullable Brightness brightness) {
        getNative().setBrightnessOverride(brightness != null ? ModdedAdapter.toNative(brightness) : null);
    }

    @Override
    default @Nullable RGBColor getGlowColorOverride() {
        int value = getNative().getGlowColorOverride();
        if (value == -1) {
            return null;
        }
        return RGBColor.of(value);
    }

    @Override
    default void setGlowColorOverride(@Nullable RGBColor glowColor) {
        getNative().setGlowColorOverride(glowColor != null ? glowColor.value() : -1);
    }

    @Override
    default float getViewRange() {
        return getNative().getViewRange();
    }

    @Override
    default void setViewRange(float range) {
        getNative().setViewRange(range);
    }

    @Override
    default int getInterpolationDuration() {
        return getNative().getTransformationInterpolationDuration();
    }

    @Override
    default void setInterpolationDuration(int duration) {
        getNative().setTransformationInterpolationDuration(duration);
    }

    @Override
    default int getTeleportDuration() {
        return getNative().getPosRotInterpolationDuration();
    }

    @Override
    default void setTeleportDuration(int duration) {
        getNative().setPosRotInterpolationDuration(duration);
    }

    @Override
    default void setInterpolationDelay(int delay) {
        getNative().setTransformationInterpolationDelay(delay);
    }
}
