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

import re.koda.easyarmorstands.platform.color.RGBColor;
import re.koda.easyarmorstands.api.formatter.NumberFormatter;
import re.koda.easyarmorstands.api.formatter.OptionalFormatter;
import re.koda.easyarmorstands.api.formatter.QuaternionfcFormatter;
import re.koda.easyarmorstands.api.formatter.RGBColorFormatter;
import re.koda.easyarmorstands.api.formatter.Vector3fcFormatter;
import re.koda.easyarmorstands.platform.entity.Display.Billboard;
import re.koda.easyarmorstands.platform.entity.Display.Brightness;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.joml.Quaternionfc;
import org.joml.Vector3fc;
import org.jspecify.annotations.NullMarked;

import java.util.Optional;

import static re.koda.easyarmorstands.api.EasyArmorStands.key;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

@NullMarked
public final class DisplayPropertyTypes {
    public static final PropertyType<Billboard> BILLBOARD = PropertyType.builder(key("display/billboard"), Billboard.class)
            .name(translatable("easyarmorstands.property.display.billboard.name"))
            .formatter(value -> switch (value) {
                case FIXED -> translatable("easyarmorstands.property.display.billboard.fixed");
                case VERTICAL -> translatable("easyarmorstands.property.display.billboard.vertical");
                case HORIZONTAL -> translatable("easyarmorstands.property.display.billboard.horizontal");
                case CENTER -> translatable("easyarmorstands.property.display.billboard.center");
            })
            .permission("easyarmorstands.property.display.billboard")
            .build();
    public static final PropertyType<Float> BOX_HEIGHT = PropertyType.builder(key("display/box/height"), Float.class)
            .name(translatable("easyarmorstands.property.display.box.height.name"))
            .formatter(NumberFormatter.pattern("0.0"))
            .permission("easyarmorstands.property.display.size")
            .build();
    public static final PropertyType<Float> BOX_WIDTH = PropertyType.builder(key("display/box/width"), Float.class)
            .name(translatable("easyarmorstands.property.display.box.width.name"))
            .formatter(NumberFormatter.pattern("0.0"))
            .permission("easyarmorstands.property.display.size")
            .build();
    public static final PropertyType<Optional<Brightness>> BRIGHTNESS = PropertyType.<Optional<Brightness>>builder(key("display/brightness"))
            .name(translatable("easyarmorstands.property.display.brightness.name"))
            .formatter(value -> value
                    .<Component>map(brightness -> text()
                            .append(translatable("easyarmorstands.property.display.brightness.sky", NamedTextColor.GOLD, text(brightness.skyLight())))
                            .append(text(", "))
                            .append(translatable("easyarmorstands.property.display.brightness.block", NamedTextColor.YELLOW, text(brightness.blockLight())))
                            .build())
                    .orElseGet(() -> translatable("easyarmorstands.property.display.brightness.default", NamedTextColor.GRAY, TextDecoration.ITALIC)))
            .permission("easyarmorstands.property.display.brightness")
            .build();
    public static final PropertyType<Quaternionfc> LEFT_ROTATION = PropertyType.builder(key("display/left_rotation"), Quaternionfc.class)
            .name(translatable("easyarmorstands.property.display.rotation.name"))
            .formatter(new QuaternionfcFormatter())
            .permission("easyarmorstands.property.display.rotation")
            .build();
    public static final PropertyType<Quaternionfc> RIGHT_ROTATION = PropertyType.builder(key("display/right_rotation"), Quaternionfc.class)
            .name(translatable("easyarmorstands.property.display.shearing.name"))
            .formatter(new QuaternionfcFormatter())
            .permission("easyarmorstands.property.display.shearing")
            .build();
    public static final PropertyType<Vector3fc> SCALE = PropertyType.builder(key("display/scale"), Vector3fc.class)
            .name(translatable("easyarmorstands.property.display.scale.name"))
            .formatter(new Vector3fcFormatter())
            .permission("easyarmorstands.property.display.scale")
            .build();
    public static final PropertyType<Vector3fc> TRANSLATION = PropertyType.builder(key("display/translation"), Vector3fc.class)
            .name(translatable("easyarmorstands.property.display.translation.name"))
            .formatter(new Vector3fcFormatter())
            .permission("easyarmorstands.property.display.translation")
            .build();
    public static final PropertyType<Optional<RGBColor>> GLOW_COLOR = PropertyType.<Optional<RGBColor>>builder(key("display/glowing/color"))
            .name(translatable("easyarmorstands.property.display.glow.color.name"))
            .formatter(new OptionalFormatter<>(new RGBColorFormatter(),
                    Component.translatable("easyarmorstands.property.display.glow.color.default", NamedTextColor.WHITE)))
            .permission("easyarmorstands.property.display.glow.color")
            .build();
    public static final PropertyType<Float> VIEW_RANGE = PropertyType.builder(key("display/view_range"), Float.class)
            .name(translatable("easyarmorstands.property.display.view-range.name"))
            .formatter(NumberFormatter.pattern("0.0"))
            .permission("easyarmorstands.property.display.viewrange")
            .build();
    public static final PropertyType<Integer> INTERPOLATION_DURATION = PropertyType.builder(key("display/interpolation_duration"), Integer.class)
            .name(translatable("easyarmorstands.property.display.interpolation-duration.name"))
            .permission("easyarmorstands.property.display.interpolation.duration")
            .build();
    public static final PropertyType<Integer> TELEPORT_DURATION = PropertyType.builder(key("display/teleport_duration"), Integer.class)
            .name(translatable("easyarmorstands.property.display.teleport-duration.name"))
            .permission("easyarmorstands.property.display.teleport.duration")
            .build();

    private DisplayPropertyTypes() {
    }
}
