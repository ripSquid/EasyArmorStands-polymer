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
package re.koda.easyarmorstands.platform.modded;

import re.koda.easyarmorstands.platform.entity.ArmorStand;
import re.koda.easyarmorstands.platform.entity.Display;
import re.koda.easyarmorstands.platform.entity.ItemDisplay;
import re.koda.easyarmorstands.platform.inventory.EquipmentSlot;
import re.koda.easyarmorstands.platform.util.Location;
import re.koda.easyarmorstands.platform.util.MainHand;
import re.koda.easyarmorstands.platform.util.Rotations;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Brightness;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Vector3dc;

public final class ModdedAdapter {
    private ModdedAdapter() {
    }

    public static BlockPos toBlockPos(Location location) {
        return toBlockPos(location.position());
    }

    public static BlockPos toBlockPos(Vector3dc position) {
        return new BlockPos(
                (int) position.x(),
                (int) position.y(),
                (int) position.z());
    }

    public static EquipmentSlot fromNative(net.minecraft.world.entity.EquipmentSlot slot) {
        return EquipmentSlot.values()[slot.ordinal()];
    }

    public static net.minecraft.world.entity.EquipmentSlot toNative(EquipmentSlot slot) {
        return net.minecraft.world.entity.EquipmentSlot.values()[slot.ordinal()];
    }

    public static Rotations fromNative(net.minecraft.core.Rotations rotations) {
        return Rotations.ofDegrees(rotations.x(), rotations.y(), rotations.z());
    }

    public static net.minecraft.core.Rotations toNative(Rotations rotations) {
        return new net.minecraft.core.Rotations((float) rotations.x(), (float) rotations.y(), (float) rotations.z());
    }

    public static MainHand fromNative(HumanoidArm arm) {
        return MainHand.valueOf(arm.name());
    }

    public static HumanoidArm toNative(MainHand hand) {
        return HumanoidArm.valueOf(hand.name());
    }

    public static Display.Billboard fromNative(net.minecraft.world.entity.Display.BillboardConstraints constraints) {
        return Display.Billboard.valueOf(constraints.name());
    }

    public static net.minecraft.world.entity.Display.BillboardConstraints toNative(Display.Billboard billboard) {
        return net.minecraft.world.entity.Display.BillboardConstraints.valueOf(billboard.name());
    }

    public static Display.Brightness fromNative(Brightness brightness) {
        return new Display.Brightness(brightness.block(), brightness.sky());
    }

    public static Brightness toNative(Display.Brightness brightness) {
        return new Brightness(brightness.blockLight(), brightness.skyLight());
    }

    public static ItemDisplay.ItemDisplayTransform fromNative(ItemDisplayContext context) {
        return ItemDisplay.ItemDisplayTransform.valueOf(context.name());
    }

    public static ItemDisplayContext toNative(ItemDisplay.ItemDisplayTransform transform) {
        return ItemDisplayContext.valueOf(transform.name());
    }

    public static int toMask(EquipmentSlot slot, ArmorStand.LockType type) {
        return 1 << toNative(slot).getFilterBit(type.ordinal() * 8);
    }
}
