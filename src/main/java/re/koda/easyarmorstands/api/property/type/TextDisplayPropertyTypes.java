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

import re.koda.easyarmorstands.platform.color.ARGBColor;
import re.koda.easyarmorstands.api.formatter.ARGBColorFormatter;
import re.koda.easyarmorstands.api.formatter.BooleanFormatter;
import re.koda.easyarmorstands.api.formatter.OptionalFormatter;
import re.koda.easyarmorstands.platform.entity.TextDisplay.TextAlignment;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jspecify.annotations.NullMarked;

import java.util.Locale;
import java.util.Optional;

import static re.koda.easyarmorstands.api.EasyArmorStands.key;
import static net.kyori.adventure.text.Component.translatable;

@NullMarked
public final class TextDisplayPropertyTypes {
    public static final PropertyType<TextAlignment> ALIGNMENT = PropertyType.builder(key("text_display/alignment"), TextAlignment.class)
            .name(translatable("easyarmorstands.property.text-display.alignment.name"))
            .formatter(value -> translatable("easyarmorstands.property.text-display.alignment." + value.name().toLowerCase(Locale.ROOT)))
            .permission("easyarmorstands.property.display.text.alignment")
            .build();
    public static final PropertyType<Optional<ARGBColor>> BACKGROUND = PropertyType.<Optional<ARGBColor>>builder(key("text_display/background"))
            .name(translatable("easyarmorstands.property.text-display.background.name"))
            .formatter(new OptionalFormatter<>(
                    new ARGBColorFormatter(Component.translatable("easyarmorstands.property.text-display.background.none")),
                    Component.translatable("easyarmorstands.property.text-display.background.default", NamedTextColor.DARK_GRAY)))
            .permission("easyarmorstands.property.display.text.background")
            .build();
    public static final PropertyType<Integer> LINE_WIDTH = PropertyType.builder(key("text_display/line_width"), Integer.class)
            .name(translatable("easyarmorstands.property.text-display.line-width.name"))
            .permission("easyarmorstands.property.display.text.linewidth")
            .build();
    public static final PropertyType<Boolean> SEE_THROUGH = PropertyType.builder(key("text_display/see_through"), Boolean.class)
            .name(translatable("easyarmorstands.property.text-display.see-through.name"))
            .formatter(BooleanFormatter.toggle())
            .permission("easyarmorstands.property.display.text.seethrough")
            .build();
    public static final PropertyType<Boolean> SHADOW = PropertyType.builder(key("text_display/shadow"), Boolean.class)
            .name(translatable("easyarmorstands.property.text-display.shadow.name"))
            .formatter(BooleanFormatter.toggle())
            .permission("easyarmorstands.property.display.text.shadow")
            .build();
    public static final PropertyType<Component> TEXT = PropertyType.builder(key("text_display/text"), Component.class)
            .name(translatable("easyarmorstands.property.text-display.text.name"))
            .formatter(value -> value)
            .permission("easyarmorstands.property.display.text")
            .build();

    private TextDisplayPropertyTypes() {
    }
}
