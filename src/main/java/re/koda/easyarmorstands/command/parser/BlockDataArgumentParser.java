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
package re.koda.easyarmorstands.command.parser;

import re.koda.easyarmorstands.platform.Platform;
import re.koda.easyarmorstands.platform.block.BlockData;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.parser.ParserDescriptor;

public class BlockDataArgumentParser<C> implements ArgumentParser<C, BlockData> {
    public static <C> ParserDescriptor<C, BlockData> blockDataParser() {
        return ParserDescriptor.of(new BlockDataArgumentParser<>(), BlockData.class);
    }

    @Override
    public @NonNull ArgumentParseResult<@NonNull BlockData> parse(@NonNull CommandContext<@NonNull C> ctx, @NonNull CommandInput commandInput) {
        Platform platform = ctx.inject(Platform.class).orElseThrow();

        String input = commandInput.peekString();
        BlockData data;
        try {
            data = platform.parseBlockData(input);
        } catch (IllegalArgumentException e) {
            return ArgumentParseResult.failure(e);
        }

        commandInput.readString();
        return ArgumentParseResult.success(data);
    }
}
