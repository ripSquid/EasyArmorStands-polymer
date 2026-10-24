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
package re.koda.easyarmorstands.platform.modded.block;

import re.koda.easyarmorstands.platform.block.Block;
import re.koda.easyarmorstands.platform.block.BlockData;
import re.koda.easyarmorstands.platform.modded.ModdedPlatform;
import re.koda.easyarmorstands.platform.modded.ModdedPlatformHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LightLayer;

public interface ModdedBlock extends Block, ModdedPlatformHolder {
    static ModdedBlock fromNative(ModdedPlatform platform, ServerLevel level, BlockPos pos) {
        return new ModdedBlockImpl(platform, level, pos);
    }

    ServerLevel getLevel();

    BlockPos getPos();

    @Override
    default int blockLight() {
        return getLevel().getBrightness(LightLayer.BLOCK, getPos());
    }

    @Override
    default int skyLight() {
        return getLevel().getBrightness(LightLayer.SKY, getPos());
    }

    @Override
    default BlockData getBlockData() {
        return ModdedBlockData.fromNative(getPlatform(), getLevel().getBlockState(getPos()));
    }
}
