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

import re.koda.easyarmorstands.platform.entity.LivingEntity;
import re.koda.easyarmorstands.platform.inventory.EquipmentSlot;
import re.koda.easyarmorstands.platform.inventory.ItemStack;
import re.koda.easyarmorstands.platform.modded.ModdedAdapter;
import re.koda.easyarmorstands.platform.modded.ModdedPlatform;
import re.koda.easyarmorstands.platform.modded.inventory.ModdedItemStack;
import re.koda.easyarmorstands.platform.util.Location;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.decoration.Mannequin;
import org.joml.Vector3d;

public interface ModdedLivingEntity extends LivingEntity, ModdedEntity {
    @Override
    net.minecraft.world.entity.LivingEntity getNative();

    static ModdedLivingEntity fromNative(ModdedPlatform platform, net.minecraft.world.entity.LivingEntity entity) {
        return switch (entity) {
            case ServerPlayer e -> ModdedPlayer.fromNative(platform, e);
            case ArmorStand e -> ModdedArmorStand.fromNative(platform, e);
            case Mannequin e -> ModdedMannequin.fromNative(platform, e);
            case Mob e -> ModdedMob.fromNative(platform, e);
            default -> new ModdedLivingEntityImpl(platform, entity);
        };
    }

    static net.minecraft.world.entity.LivingEntity toNative(LivingEntity entity) {
        return ((ModdedLivingEntity) entity).getNative();
    }

    @Override
    default float yaw() {
        return getNative().getYHeadRot();
    }

    @Override
    default Location eyeLocation() {
        net.minecraft.world.entity.Entity entity = getNative();
        Vector3d position = new Vector3d(entity.getX(), entity.getEyeY(), entity.getZ());
        return Location.of(world(), position, yaw(), pitch());
    }

    @Override
    default boolean hasEquipmentSlot(EquipmentSlot slot) {
        return getNative().canUseSlot(ModdedAdapter.toNative(slot));
    }

    @Override
    default ItemStack getEquipment(EquipmentSlot slot) {
        return ModdedItemStack.fromNative(getPlatform(), getNative().getItemBySlot(ModdedAdapter.toNative(slot)));
    }

    @Override
    default void setEquipment(EquipmentSlot slot, ItemStack item) {
        getNative().setItemSlot(ModdedAdapter.toNative(slot), ModdedItemStack.toNative(item));
    }

    @Override
    default boolean hasScaleAttribute() {
        return getNative().getAttribute(Attributes.SCALE) != null;
    }

    @Override
    default double getScaleAttribute() {
        AttributeInstance attribute = getNative().getAttribute(Attributes.SCALE);
        if (attribute != null) {
            return attribute.getBaseValue();
        } else {
            return 1;
        }
    }

    @Override
    default void setScaleAttribute(double value) {
        AttributeInstance attribute = getNative().getAttribute(Attributes.SCALE);
        if (attribute != null) {
            attribute.setBaseValue(value);
        }
    }
}
