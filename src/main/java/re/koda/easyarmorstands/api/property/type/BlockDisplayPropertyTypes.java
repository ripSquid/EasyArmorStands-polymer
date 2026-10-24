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
package re.koda.easyarmorstands.api.property.type;

import re.koda.easyarmorstands.platform.block.BlockData;
import org.jspecify.annotations.NullMarked;

import static re.koda.easyarmorstands.api.EasyArmorStands.key;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

@NullMarked
public final class BlockDisplayPropertyTypes {
    public static final PropertyType<BlockData> BLOCK = PropertyType.builder(key("block_display/block"), BlockData.class)
            .name(translatable("easyarmorstands.property.block-display.block.name"))
            .formatter(value -> text(value.getAsString()))
            .permission("easyarmorstands.property.display.block")
            .build();

    private BlockDisplayPropertyTypes() {
    }
}
