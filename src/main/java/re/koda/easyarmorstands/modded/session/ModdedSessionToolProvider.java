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
package re.koda.easyarmorstands.modded.session;

import re.koda.easyarmorstands.modded.EasyArmorStandsModdedImpl;
import re.koda.easyarmorstands.platform.inventory.ItemStack;
import re.koda.easyarmorstands.platform.modded.inventory.ModdedItemStack;
import re.koda.easyarmorstands.session.SessionToolProvider;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.component.CustomData;

import java.util.Locale;

public class ModdedSessionToolProvider implements SessionToolProvider {
    private static final String KEY = "easyarmorstands:tool";
    private final EasyArmorStandsModdedImpl eas;

    public ModdedSessionToolProvider(EasyArmorStandsModdedImpl eas) {
        this.eas = eas;
    }

    @Override
    public ItemStack createTool(Locale locale) {
        net.minecraft.world.item.ItemStack item = ModdedItemStack.toNative(eas.getConfiguration().editor.tool.render(locale));
        CustomData data = item.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag tag = data.copyTag();
        tag.putBoolean(KEY, true);
        item.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        item.remove(DataComponents.COOKING_FUEL);
        return ModdedItemStack.fromNative(eas.platform(), item);
    }

    @Override
    public boolean isTool(ItemStack item) {
        return isTool(ModdedItemStack.toNative(item));
    }

    public boolean isTool(DataComponentGetter item) {
        CustomData data = item.get(DataComponents.CUSTOM_DATA);
        if (data != null) {
            return data.copyTag().getBooleanOr(KEY, false);
        }
        return false;
    }
}
