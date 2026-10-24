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
package re.koda.easyarmorstands.property.type;

import re.koda.easyarmorstands.api.ArmorStandPart;
import re.koda.easyarmorstands.api.SkinPart;
import re.koda.easyarmorstands.api.property.type.ArmorStandPropertyTypes;
import re.koda.easyarmorstands.api.property.type.BlockDisplayPropertyTypes;
import re.koda.easyarmorstands.api.property.type.DisplayPropertyTypes;
import re.koda.easyarmorstands.api.property.type.EntityPropertyTypes;
import re.koda.easyarmorstands.api.property.type.InteractionPropertyTypes;
import re.koda.easyarmorstands.api.property.type.ItemDisplayPropertyTypes;
import re.koda.easyarmorstands.api.property.type.ItemFramePropertyTypes;
import re.koda.easyarmorstands.api.property.type.MannequinPropertyTypes;
import re.koda.easyarmorstands.api.property.type.PropertyTypeRegistry;
import re.koda.easyarmorstands.api.property.type.TextDisplayPropertyTypes;
import re.koda.easyarmorstands.platform.inventory.EquipmentSlot;

public class DefaultPropertyTypes {
    public DefaultPropertyTypes(PropertyTypeRegistry registry) {
        registerEntityProperties(registry);
        registerArmorStandProperties(registry);
        registerDisplayProperties(registry);
        registerMannequinProperties(registry);
        registerItemFrameProperties(registry);
    }

    private void registerEntityProperties(PropertyTypeRegistry registry) {
        registry.register(EntityPropertyTypes.AI);
        registry.register(EntityPropertyTypes.CUSTOM_NAME);
        registry.register(EntityPropertyTypes.CUSTOM_NAME_VISIBLE);
        registry.register(EntityPropertyTypes.GLOWING);
        registry.register(EntityPropertyTypes.LOCATION);
        registry.register(EntityPropertyTypes.SCALE);
        registry.register(EntityPropertyTypes.SILENT);
        registry.register(EntityPropertyTypes.TAGS);
        registry.register(EntityPropertyTypes.VISIBLE);
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            registry.register(EntityPropertyTypes.EQUIPMENT.get(slot));
        }
    }

    private void registerArmorStandProperties(PropertyTypeRegistry registry) {
        registry.register(ArmorStandPropertyTypes.ARMS);
        registry.register(ArmorStandPropertyTypes.BASE_PLATE);
        registry.register(ArmorStandPropertyTypes.CAN_TICK);
        registry.register(ArmorStandPropertyTypes.GRAVITY);
        registry.register(ArmorStandPropertyTypes.INVULNERABLE);
        registry.register(ArmorStandPropertyTypes.LOCK);
        registry.register(ArmorStandPropertyTypes.MARKER);
        registry.register(ArmorStandPropertyTypes.SIZE);
        for (ArmorStandPart part : ArmorStandPart.values()) {
            registry.register(ArmorStandPropertyTypes.POSE.get(part));
        }
    }

    private void registerDisplayProperties(PropertyTypeRegistry registry) {
        registry.register(DisplayPropertyTypes.BILLBOARD);
        registry.register(DisplayPropertyTypes.BOX_HEIGHT);
        registry.register(DisplayPropertyTypes.BOX_WIDTH);
        registry.register(DisplayPropertyTypes.BRIGHTNESS);
        registry.register(DisplayPropertyTypes.GLOW_COLOR);
        registry.register(DisplayPropertyTypes.LEFT_ROTATION);
        registry.register(DisplayPropertyTypes.RIGHT_ROTATION);
        registry.register(DisplayPropertyTypes.SCALE);
        registry.register(DisplayPropertyTypes.TRANSLATION);
        registry.register(DisplayPropertyTypes.VIEW_RANGE);
        registry.register(DisplayPropertyTypes.INTERPOLATION_DURATION);
        registry.register(DisplayPropertyTypes.TELEPORT_DURATION);
        registry.register(ItemDisplayPropertyTypes.ITEM);
        registry.register(ItemDisplayPropertyTypes.TRANSFORM);
        registry.register(TextDisplayPropertyTypes.ALIGNMENT);
        registry.register(TextDisplayPropertyTypes.BACKGROUND);
        registry.register(TextDisplayPropertyTypes.LINE_WIDTH);
        registry.register(TextDisplayPropertyTypes.SEE_THROUGH);
        registry.register(TextDisplayPropertyTypes.SHADOW);
        registry.register(TextDisplayPropertyTypes.TEXT);
        registry.register(BlockDisplayPropertyTypes.BLOCK);
        registry.register(InteractionPropertyTypes.RESPONSIVE);
    }

    private void registerMannequinProperties(PropertyTypeRegistry registry) {
        registry.register(MannequinPropertyTypes.MAIN_HAND);
        registry.register(MannequinPropertyTypes.PROFILE);
        registry.register(MannequinPropertyTypes.IMMOVABLE);
        registry.register(MannequinPropertyTypes.POSE);
        registry.register(MannequinPropertyTypes.DESCRIPTION);
        for (SkinPart part : SkinPart.values()) {
            registry.register(MannequinPropertyTypes.SKIN_PART_VISIBLE.get(part));
        }
    }

    private void registerItemFrameProperties(PropertyTypeRegistry registry) {
        registry.register(ItemFramePropertyTypes.FIXED);
        registry.register(ItemFramePropertyTypes.ITEM);
    }
}
