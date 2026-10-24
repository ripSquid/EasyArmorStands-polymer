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

import re.koda.easyarmorstands.editor.layer.ValueLayer;
import re.koda.easyarmorstands.message.Message;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.util.ComponentMessageThrowable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.suggestion.Suggestion;
import org.incendo.cloud.suggestion.SuggestionProvider;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class LayerValueArgumentParser<C> implements ArgumentParser<C, Object>, SuggestionProvider<C> {
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public @NonNull ArgumentParseResult<@NonNull Object> parse(@NonNull CommandContext<@NonNull C> context, @NonNull CommandInput input) {
        Optional<ValueLayer> optionalLayer = context.inject(ValueLayer.class);
        if (!optionalLayer.isPresent()) {
            return ArgumentParseResult.failure(new UnsupportedToolException());
        }
        ValueLayer layer = optionalLayer.get();
        return layer.getParser().parse(context, input);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public @NonNull CompletableFuture<@NonNull Iterable<@NonNull Suggestion>> suggestionsFuture(@NonNull CommandContext<C> context, @NonNull CommandInput input) {
        Optional<ValueLayer> optionalLayer = context.inject(ValueLayer.class);
        if (!optionalLayer.isPresent()) {
            return CompletableFuture.completedFuture(Collections.emptyList());
        }
        ValueLayer layer = optionalLayer.get();
        return layer.getParser().suggestionProvider().suggestionsFuture(context, input);
    }

    private static class UnsupportedToolException extends IllegalStateException implements ComponentMessageThrowable {
        @Override
        public @Nullable Component componentMessage() {
            return Message.component("easyarmorstands.error.set-unsupported");
        }
    }
}
