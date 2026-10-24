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

import re.koda.easyarmorstands.platform.entity.ItemFrame;
import re.koda.easyarmorstands.platform.inventory.ItemStack;
import re.koda.easyarmorstands.platform.modded.ModdedPlatform;
import re.koda.easyarmorstands.platform.modded.inventory.ModdedItemStack;

public interface ModdedItemFrame extends ItemFrame, ModdedEntity {
    @Override
    net.minecraft.world.entity.decoration.ItemFrame getNative();

    static ModdedItemFrame fromNative(ModdedPlatform platform, net.minecraft.world.entity.decoration.ItemFrame entity) {
        return new ModdedItemFrameImpl(platform, entity);
    }

    static net.minecraft.world.entity.decoration.ItemFrame toNative(ItemFrame entity) {
        return ((ModdedItemFrame) entity).getNative();
    }

    @Override
    default ItemStack getItem() {
        return ModdedItemStack.fromNative(getPlatform(), getNative().getItem());
    }

    @Override
    default void setItem(ItemStack item) {
        getNative().setItem(ModdedItemStack.toNative(item));
    }

    @Override
    default boolean isFixed() {
        return getNative().fixed;
    }

    @Override
    default void setFixed(boolean fixed) {
        getNative().fixed = fixed;
    }

    @Override
    default boolean isVisible() {
        return !getNative().isInvisible();
    }

    @Override
    default void setVisible(boolean visible) {
        getNative().setInvisible(!visible);
    }
}
