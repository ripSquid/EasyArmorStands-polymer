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

import re.koda.easyarmorstands.platform.entity.ItemDisplay;
import re.koda.easyarmorstands.platform.inventory.ItemStack;
import re.koda.easyarmorstands.platform.modded.ModdedAdapter;
import re.koda.easyarmorstands.platform.modded.ModdedPlatform;
import re.koda.easyarmorstands.platform.modded.inventory.ModdedItemStack;
import net.minecraft.world.entity.Display;

public interface ModdedItemDisplay extends ItemDisplay, ModdedDisplay {
    @Override
    Display.ItemDisplay getNative();

    static ModdedItemDisplay fromNative(ModdedPlatform platform, Display.ItemDisplay entity) {
        return new ModdedItemDisplayImpl(platform, entity);
    }

    static Display.ItemDisplay toNative(ItemDisplay entity) {
        return ((ModdedItemDisplay) entity).getNative();
    }

    @Override
    default ItemStack getItemStack() {
        return ModdedItemStack.fromNative(getPlatform(), getNative().getItemStack());
    }

    @Override
    default void setItemStack(ItemStack item) {
        getNative().setItemStack(ModdedItemStack.toNative(item));
    }

    @Override
    default ItemDisplayTransform getItemDisplayTransform() {
        return ModdedAdapter.fromNative(getNative().getItemTransform());
    }

    @Override
    default void setItemDisplayTransform(ItemDisplayTransform transform) {
        getNative().setItemTransform(ModdedAdapter.toNative(transform));
    }
}
