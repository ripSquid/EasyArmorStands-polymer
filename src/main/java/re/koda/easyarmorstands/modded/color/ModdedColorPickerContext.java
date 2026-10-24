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
package re.koda.easyarmorstands.modded.color;

import re.koda.easyarmorstands.api.property.Property;
import re.koda.easyarmorstands.menu.color.ColorPickerContext;
import re.koda.easyarmorstands.message.Message;
import re.koda.easyarmorstands.platform.Platform;
import re.koda.easyarmorstands.platform.color.RGBColor;
import re.koda.easyarmorstands.platform.inventory.ItemStack;
import re.koda.easyarmorstands.platform.modded.ModdedPlatform;
import re.koda.easyarmorstands.platform.modded.inventory.ModdedItemStack;
import re.koda.easyarmorstands.util.Util;
import net.minecraft.core.component.DataComponents;
import net.minecraft.references.ItemIds;
import net.minecraft.world.item.component.DyedItemColor;

import java.util.List;

public class ModdedColorPickerContext implements ColorPickerContext {
    private static final List<ItemColorAccessor> ACCESSORS = List.of(
            new DataItemColorAccessor<>(
                    DataComponents.DYED_COLOR,
                    new DyedItemColor(DyedItemColor.LEATHER_COLOR),
                    h -> h.is(ItemIds.LEATHER_HELMET)
                            || h.is(ItemIds.LEATHER_CHESTPLATE)
                            || h.is(ItemIds.LEATHER_LEGGINGS)
                            || h.is(ItemIds.LEATHER_BOOTS)
                            || h.is(ItemIds.LEATHER_HORSE_ARMOR),
                    DyedItemColor::new,
                    DyedItemColor::rgb));

    private final ModdedPlatform platform;
    private final Property<ItemStack> property;

    public ModdedColorPickerContext(ModdedPlatform platform, Property<ItemStack> property) {
        this.platform = platform;
        this.property = property;
    }

    @Override
    public Platform platform() {
        return platform;
    }

    @Override
    public ItemStack item() {
        return property.getValue();
    }

    public static boolean isSupported(ItemStack item) {
        return isSupported(ModdedItemStack.toNative(item));
    }

    private static boolean isSupported(net.minecraft.world.item.ItemStack item) {
        for (ItemColorAccessor accessor : ACCESSORS) {
            if (accessor.isSupported(item)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public RGBColor getColor() {
        net.minecraft.world.item.ItemStack item = ModdedItemStack.toNative(property.getValue());
        for (ItemColorAccessor accessor : ACCESSORS) {
            if (accessor.isSupported(item)) {
                return accessor.getColor(item);
            }
        }
        return RGBColor.WHITE;
    }

    @Override
    public void setColor(RGBColor color) {
        net.minecraft.world.item.ItemStack item = ModdedItemStack.toNative(property.getValue());
        for (ItemColorAccessor accessor : ACCESSORS) {
            if (accessor.isSupported(item)) {
                accessor.setColor(item, color);
                property.setValue(ModdedItemStack.fromNative(platform, item));
                property.commit(Message.component("easyarmorstands.history.changed-color", Util.formatColor(color)));
                return;
            }
        }
    }
}
