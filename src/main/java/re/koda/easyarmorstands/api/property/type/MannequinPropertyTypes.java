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

import re.koda.easyarmorstands.api.SkinPart;
import re.koda.easyarmorstands.api.formatter.BooleanFormatter;
import re.koda.easyarmorstands.api.formatter.OptionalFormatter;
import re.koda.easyarmorstands.platform.entity.Pose;
import re.koda.easyarmorstands.platform.profile.Profile;
import re.koda.easyarmorstands.platform.util.MainHand;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

import java.util.Locale;
import java.util.Optional;

import static re.koda.easyarmorstands.api.EasyArmorStands.key;
import static net.kyori.adventure.text.Component.translatable;

@SuppressWarnings("PatternValidation")
@NullMarked
public final class MannequinPropertyTypes {
    public static final PropertyType<MainHand> MAIN_HAND = PropertyType.builder(key("mannequin/main_hand"), MainHand.class)
            .name(translatable("easyarmorstands.property.mannequin.main-hand.name"))
            .formatter(value -> switch (value) {
                case LEFT -> translatable("easyarmorstands.property.common.hand.left");
                case RIGHT -> translatable("easyarmorstands.property.common.hand.right");
            })
            .permission("easyarmorstands.property.mannequin.mainhand")
            .build();
    public static final PropertyType<Profile> PROFILE = PropertyType.builder(key("mannequin/profile"), Profile.class)
            .name(translatable("easyarmorstands.property.mannequin.profile.name"))
            .formatter(Component::object)
            .permission("easyarmorstands.property.mannequin.profile")
            .build();
    public static final PropertyType<Boolean> IMMOVABLE = PropertyType.builder(key("mannequin/immovable"), Boolean.class)
            .name(translatable("easyarmorstands.property.mannequin.immovable.name"))
            .formatter(BooleanFormatter.translatable(
                    "easyarmorstands.property.mannequin.immovable.enabled",
                    "easyarmorstands.property.mannequin.immovable.disabled"))
            .permission("easyarmorstands.property.mannequin.immovable")
            .build();
    public static final PropertyType<Optional<Component>> DESCRIPTION = PropertyType.<Optional<Component>>builder(key("mannequin/description"))
            .name(translatable("easyarmorstands.property.mannequin.description.name"))
            .formatter(new OptionalFormatter<>(value -> value))
            .permission("easyarmorstands.property.mannequin.description")
            .build();
    public static final PropertyType<Pose> POSE = PropertyType.builder(key("mannequin/pose"), Pose.class)
            .name(translatable("easyarmorstands.property.mannequin.pose.name"))
            .formatter(value -> translatable("easyarmorstands.property.mannequin.pose." + value.name().toLowerCase(Locale.ROOT).replace("_", "-")))
            .permission("easyarmorstands.property.mannequin.pose")
            .build();
    public static final KeyedPropertyType<SkinPart, Boolean> SKIN_PART_VISIBLE = new EnumKeyedPropertyType<>(SkinPart.class,
            part -> PropertyType.builder(key("mannequin/part/" + part.name().toLowerCase(Locale.ROOT) + "/visible"), Boolean.class)
                    .name(translatable("easyarmorstands.property.mannequin.part.visible.name",
                            translatable("easyarmorstands.mannequin.part." + part.name().toLowerCase(Locale.ROOT).replace("_", "-"))))
                    .formatter(BooleanFormatter.visibility())
                    .permission(switch (part) {
                        case CAPE -> "easyarmorstands.property.mannequin.part.visible.cape";
                        case JACKET -> "easyarmorstands.property.mannequin.part.visible.jacket";
                        case LEFT_SLEEVE -> "easyarmorstands.property.mannequin.part.visible.sleeve.left";
                        case RIGHT_SLEEVE -> "easyarmorstands.property.mannequin.part.visible.sleeve.right";
                        case LEFT_PANTS -> "easyarmorstands.property.mannequin.part.visible.pants.left";
                        case RIGHT_PANTS -> "easyarmorstands.property.mannequin.part.visible.pants.right";
                        case HAT -> "easyarmorstands.property.mannequin.part.visible.hat";
                    })
                    .build());

    private MannequinPropertyTypes() {
    }
}
