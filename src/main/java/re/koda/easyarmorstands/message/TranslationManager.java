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

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.Translator;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class TranslationManager {
    private static final Pattern PATTERN = Pattern.compile("(.+)\\.json");
    private static final Key key = Key.key("easyarmorstands", "translation");
    private final Set<Locale> loadedLocales = new HashSet<>();
    private PatternTranslationStore registry = new PatternTranslationStore(key);

    public void load(@Nullable Path dataFolder, ComponentLogger logger) {
        GlobalTranslator.translator().removeSource(registry);

        registry = new PatternTranslationStore(key);
        loadedLocales.clear();

        // Convert old message files
        if (dataFolder != null) {
            Path langPath = dataFolder.resolve("lang");
            try (Stream<Path> paths = Files.list(dataFolder)) {
                paths.forEach(path -> {
                    try {
                        if (MessageMigrator.migrate(path, langPath)) {
                            logger.info("Migrated custom messages: {}", path.getFileName().toString());
                        }
                    } catch (Exception e) {
                        logger.error("Failed to convert old message file: {}", path.getFileName().toString(), e);
                    }
                });
            } catch (NoSuchFileException ignored) {
            } catch (IOException e) {
                logger.error("Failed to convert old message files", e);
            }

            // Load custom locales
            try (Stream<Path> paths = Files.list(langPath)) {
                paths.forEach(path -> loadFile(path, logger));
            } catch (NoSuchFileException ignored) {
            } catch (IOException e) {
                logger.error("Failed to load messages", e);
            }
        }

        // Load included default locales
        try {
            loadDefaultLocale(Locale.of("en", "us"), logger);
            loadDefaultLocale(Locale.of("de", "de"), logger);
            loadDefaultLocale(Locale.of("ru", "ru"), logger);
        } catch (IOException e) {
            logger.error("Failed to load default messages", e);
        }

        GlobalTranslator.translator().addSource(registry);
    }

    private void loadDefaultLocale(Locale locale, ComponentLogger logger) throws IOException {
        if (loadedLocales.add(locale)) {
            String name = "/assets/easyarmorstands/lang/" + locale.toString().toLowerCase(Locale.ROOT) + ".json";
            InputStream resource = getClass().getResourceAsStream(name);
            if (resource != null) {
                registry.readLocale(resource, locale);
            } else {
                logger.warn("Default messages for locale {} are missing: {}", locale, name);
            }
        }
    }

    private void loadFile(Path path, ComponentLogger logger) {
        Matcher matcher = PATTERN.matcher(path.getFileName().toString());
        if (!matcher.matches()) {
            return;
        }

        Locale locale = Translator.parseLocale(matcher.group(1));
        if (locale == null) {
            logger.warn("Invalid locale: {}", matcher.group(1));
            return;
        }

        if (!loadedLocales.add(locale)) {
            return;
        }

        try {
            registry.readLocale(path, locale);
            logger.info("Loaded custom messages for language: {}", locale);
        } catch (Exception e) {
            logger.error("Failed to load translations from {}", path.getFileName(), e);
        }
    }
}
