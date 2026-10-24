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

import re.koda.easyarmorstands.api.formatter.ItemStackFormatter;
import re.koda.easyarmorstands.platform.entity.ItemDisplay.ItemDisplayTransform;
import re.koda.easyarmorstands.platform.entity.Player;
import re.koda.easyarmorstands.platform.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import java.util.Locale;

import static re.koda.easyarmorstands.api.EasyArmorStands.key;
import static net.kyori.adventure.text.Component.translatable;

@NullMarked
public final class ItemDisplayPropertyTypes {
    public static final PropertyType<ItemStack> ITEM = PropertyType.builder(key("item_display/item"), ItemStack.class)
            .name(translatable("easyarmorstands.property.item.name"))
            .formatter(new ItemStackFormatter())
            .permission("easyarmorstands.property.display.item")
            .canCopyPredicate(Player::isCreativeMode)
            .build();
    public static final PropertyType<ItemDisplayTransform> TRANSFORM = PropertyType.builder(key("item_display/transform"), ItemDisplayTransform.class)
            .name(translatable("easyarmorstands.property.item-display.transform.name"))
            .formatter(value -> translatable("easyarmorstands.property.item-display.transform." + value.name().toLowerCase(Locale.ROOT).replace("_", "-")))
            .permission("easyarmorstands.property.display.item.transform")
            .build();

    private ItemDisplayPropertyTypes() {
    }
}
