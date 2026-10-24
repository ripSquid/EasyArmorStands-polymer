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
package re.koda.easyarmorstands.registry;

import re.koda.easyarmorstands.color.ColorPicker;
import re.koda.easyarmorstands.platform.Platform;
import net.kyori.adventure.key.Key;
import org.intellij.lang.annotations.Subst;

public final class ItemTypeKeys {
    private ItemTypeKeys() {
    }

    public static final Key AIR = Key.key("air");
    public static final Key ARMOR_STAND = Key.key("armor_stand");
    public static final Key BEDROCK = Key.key("bedrock");
    public static final Key BLACK_STAINED_GLASS = Key.key("black_stained_glass");
    public static final Key BLUE_BANNER = Key.key("blue_banner");
    public static final Key BLUE_CONCRETE = Key.key("blue_concrete");
    public static final Key BONE_MEAL = Key.key("bone_meal");
    public static final Key BRUSH = Key.key("brush");
    public static final Key FEATHER = Key.key("feather");
    public static final Key GLASS_PANE = Key.key("glass_pane");
    public static final Key GLOWSTONE_DUST = Key.key("glowstone_dust");
    public static final Key GOLDEN_APPLE = Key.key("golden_apple");
    public static final Key GRAY_CONCRETE = Key.key("gray_concrete");
    public static final Key IRON_BARS = Key.key("iron_bars");
    public static final Key IRON_CHESTPLATE = Key.key("iron_chestplate");
    public static final Key IRON_HELMET = Key.key("iron_helmet");
    public static final Key IRON_SWORD = Key.key("iron_sword");
    public static final Key LEVER = Key.key("lever");
    public static final Key LIGHT_BLUE_STAINED_GLASS_PANE = Key.key("light_blue_stained_glass_pane");
    public static final Key LIGHT_GRAY_CONCRETE = Key.key("light_gray_concrete");
    public static final Key LIME_CONCRETE = Key.key("lime_concrete");
    public static final Key NAME_TAG = Key.key("name_tag");
    public static final Key NOTE_BLOCK = Key.key("note_block");
    public static final Key OAK_PRESSURE_PLATE = Key.key("oak_pressure_plate");
    public static final Key OAK_STAIRS = Key.key("oak_stairs");
    public static final Key PLAYER_HEAD = Key.key("player_head");
    public static final Key POTION = Key.key("potion");
    public static final Key REDSTONE_BLOCK = Key.key("redstone_block");
    public static final Key RED_CONCRETE = Key.key("red_concrete");
    public static final Key STICK = Key.key("stick");
    public static final Key STONE = Key.key("stone");
    public static final Key STONE_SLAB = Key.key("stone_slab");
    public static final Key SUNFLOWER = Key.key("sunflower");
    public static final Key TARGET = Key.key("target");
    public static final Key TNT = Key.key("tnt");
    public static final Key YELLOW_STAINED_GLASS_PANE = Key.key("yellow_stained_glass_pane");

    public static void validate(Platform platform) {
        RegistryUtil.validate(ItemTypeKeys.class, platform::getItemType);
        for (@Subst("light_blue") String name : ColorPicker.PRESETS.keySet()) {
            platform.getItemType(Key.key(name + "_wool"));
        }
    }
}
