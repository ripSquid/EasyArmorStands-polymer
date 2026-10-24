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

import re.koda.easyarmorstands.api.ArmorStandPart;
import re.koda.easyarmorstands.api.ArmorStandSize;
import re.koda.easyarmorstands.api.formatter.BooleanFormatter;
import re.koda.easyarmorstands.api.formatter.RotationsFormatter;
import re.koda.easyarmorstands.platform.util.Rotations;
import org.jspecify.annotations.NullMarked;

import java.util.Locale;

import static re.koda.easyarmorstands.api.EasyArmorStands.key;
import static net.kyori.adventure.text.Component.translatable;

@SuppressWarnings("PatternValidation")
@NullMarked
public final class ArmorStandPropertyTypes {
    public static final PropertyType<Boolean> ARMS = PropertyType.builder(key("armor_stand/arms"), Boolean.class)
            .name(translatable("easyarmorstands.property.armor-stand.arms.name"))
            .formatter(BooleanFormatter.visibility())
            .permission("easyarmorstands.property.armorstand.arms")
            .build();
    public static final PropertyType<Boolean> BASE_PLATE = PropertyType.builder(key("armor_stand/base_plate"), Boolean.class)
            .name(translatable("easyarmorstands.property.armor-stand.base-plate.name"))
            .formatter(BooleanFormatter.visibility())
            .permission("easyarmorstands.property.armorstand.baseplate")
            .build();
    public static final PropertyType<Boolean> CAN_TICK = PropertyType.builder(key("armor_stand/can_tick"), Boolean.class)
            .name(translatable("easyarmorstands.property.armor-stand.can-tick.name"))
            .formatter(BooleanFormatter.toggle())
            .permission("easyarmorstands.property.armorstand.cantick")
            .build();
    public static final PropertyType<Boolean> GRAVITY = PropertyType.builder(key("armor_stand/gravity"), Boolean.class)
            .name(translatable("easyarmorstands.property.gravity.name"))
            .formatter(BooleanFormatter.toggle())
            .permission("easyarmorstands.property.gravity")
            .build();
    public static final PropertyType<Boolean> INVULNERABLE = PropertyType.builder(key("armor_stand/invulnerable"), Boolean.class)
            .name(translatable("easyarmorstands.property.invulnerability.name"))
            .formatter(BooleanFormatter.translatable(
                    "easyarmorstands.property.invulnerability.enabled",
                    "easyarmorstands.property.invulnerability.disabled"))
            .permission("easyarmorstands.property.invulnerable")
            .build();
    public static final PropertyType<Boolean> LOCK = PropertyType.builder(key("armor_stand/lock"), Boolean.class)
            .name(translatable("easyarmorstands.property.armor-stand.lock.name"))
            .formatter(BooleanFormatter.translatable(
                    "easyarmorstands.property.armor-stand.lock.enabled",
                    "easyarmorstands.property.armor-stand.lock.disabled"))
            .permission("easyarmorstands.property.armorstand.lock")
            .build();
    public static final PropertyType<Boolean> MARKER = PropertyType.builder(key("armor_stand/marker"), Boolean.class)
            .name(translatable("easyarmorstands.property.armor-stand.marker.name"))
            .formatter(BooleanFormatter.toggle())
            .permission("easyarmorstands.property.armorstand.marker")
            .build();
    public static final KeyedPropertyType<ArmorStandPart, Rotations> POSE = new EnumKeyedPropertyType<>(ArmorStandPart.class,
            part -> PropertyType.builder(key("armor_stand/pose/" + part.name().toLowerCase(Locale.ROOT)), Rotations.class)
                    .name(translatable("easyarmorstands.property.armor-stand.pose.name", part.displayName()))
                    .formatter(new RotationsFormatter())
                    .permission("easyarmorstands.property.armorstand.pose." + part.name().toLowerCase(Locale.ROOT).replace("_", ""))
                    .build());
    public static final PropertyType<ArmorStandSize> SIZE = PropertyType.builder(key("armor_stand/size"), ArmorStandSize.class)
            .name(translatable("easyarmorstands.property.armor-stand.size.name"))
            .formatter(ArmorStandSize::displayName)
            .permission("easyarmorstands.property.armorstand.size")
            .build();

    private ArmorStandPropertyTypes() {
    }
}
