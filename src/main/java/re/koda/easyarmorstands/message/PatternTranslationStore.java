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
package re.koda.easyarmorstands.message;

import com.google.gson.stream.JsonReader;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.translation.AbstractTranslationStore;
import org.jetbrains.annotations.NotNullByDefault;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NotNullByDefault
public class PatternTranslationStore extends AbstractTranslationStore<String> {
    private static final Pattern ARGUMENT_PATTERN = Pattern.compile("%((\\d+)\\$)?s");

    public PatternTranslationStore(Key name) {
        super(name);
    }

    @Override
    public @Nullable MessageFormat translate(String key, Locale locale) {
        return null;
    }

    public void readLocale(Path input, Locale locale) throws IOException {
        readLocale(Files.newBufferedReader(input, StandardCharsets.UTF_8), locale);
    }

    public void readLocale(InputStream input, Locale locale) throws IOException {
        readLocale(new BufferedReader(new InputStreamReader(input)), locale);
    }

    public void readLocale(Reader reader, Locale locale) throws IOException {
        try (JsonReader jsonReader = new JsonReader(reader)) {
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                String name = jsonReader.nextName();
                String value = jsonReader.nextString();
                register(name, locale, value);
            }
            jsonReader.endObject();
        }
    }

    @Override
    public @Nullable Component translate(TranslatableComponent component, Locale locale) {
        String pattern = translationValue(component.key(), locale);
        if (pattern == null) {
            return null;
        }
        TextComponent.Builder builder = Component.text();
        Matcher matcher = ARGUMENT_PATTERN.matcher(pattern);
        IndexParser indexParser = new IndexParser();
        int lastEnd = 0;
        while (matcher.find()) {
            // text before match
            String before = pattern.substring(lastEnd, matcher.start());
            if (!before.isEmpty()) {
                builder.append(Component.text(before));
            }

            // value of match
            int index = indexParser.parseIndex(matcher.group(2));
            if (index >= 1 && index <= component.arguments().size()) {
                builder.append(component.arguments().get(index - 1));
            } else {
                builder.append(Component.text(matcher.group()));
            }

            lastEnd = matcher.end();
        }

        // text after last match
        String tail = pattern.substring(lastEnd);
        if (!tail.isEmpty()) {
            builder.append(Component.text(tail));
        }

        return builder.append(component.children()).style(component.style()).build().compact();
    }

    private static class IndexParser {
        private int counter;

        public int parseIndex(@Nullable String input) {
            int index;
            if (input != null) {
                index = Integer.parseInt(input);
            } else {
                index = ++counter;
            }
            return index;
        }
    }
}
