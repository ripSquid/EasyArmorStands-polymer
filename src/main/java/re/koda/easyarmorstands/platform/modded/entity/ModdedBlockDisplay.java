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

import re.koda.easyarmorstands.platform.block.BlockData;
import re.koda.easyarmorstands.platform.entity.BlockDisplay;
import re.koda.easyarmorstands.platform.modded.ModdedPlatform;
import re.koda.easyarmorstands.platform.modded.block.ModdedBlockData;
import net.minecraft.world.entity.Display;

public interface ModdedBlockDisplay extends BlockDisplay, ModdedDisplay {
    @Override
    Display.BlockDisplay getNative();

    static ModdedBlockDisplay fromNative(ModdedPlatform platform, Display.BlockDisplay entity) {
        return new ModdedBlockDisplayImpl(platform, entity);
    }

    static Display.BlockDisplay toNative(BlockDisplay display) {
        return ((ModdedBlockDisplay) display).getNative();
    }

    @Override
    default BlockData getBlock() {
        return ModdedBlockData.fromNative(getPlatform(), getNative().getBlockState());
    }

    @Override
    default void setBlock(BlockData data) {
        getNative().setBlockState(ModdedBlockData.toNative(data));
    }
}
