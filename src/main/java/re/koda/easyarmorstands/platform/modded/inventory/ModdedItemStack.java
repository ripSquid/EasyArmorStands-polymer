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
package re.koda.easyarmorstands.platform.modded.inventory;

import it.unimi.dsi.fastutil.objects.ReferenceLinkedOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceSortedSets;
import re.koda.easyarmorstands.platform.inventory.ItemStack;
import re.koda.easyarmorstands.platform.inventory.ItemType;
import re.koda.easyarmorstands.platform.modded.ModdedPlatform;
import re.koda.easyarmorstands.platform.modded.ModdedPlatformHolder;
import re.koda.easyarmorstands.platform.modded.profile.ModdedProfile;
import re.koda.easyarmorstands.platform.profile.Profile;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.platform.modcommon.MinecraftAudiences;
import net.kyori.adventure.text.Component;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.SequencedSet;

public interface ModdedItemStack extends ItemStack, ModdedPlatformHolder {
    net.minecraft.world.item.ItemStack getNative();

    static ModdedItemStack empty(ModdedPlatform platform) {
        return ModdedItemStack.fromNative(platform, net.minecraft.world.item.ItemStack.EMPTY);
    }

    static ModdedItemStack fromNative(ModdedPlatform platform, net.minecraft.world.item.@Nullable ItemStack stack) {
        if (stack == null) {
            return empty(platform);
        }
        return new ModdedItemStackImpl(platform, stack.copy());
    }

    static net.minecraft.world.item.ItemStack toNative(@Nullable ItemStack item) {
        if (item == null) {
            return net.minecraft.world.item.ItemStack.EMPTY;
        }
        return ((ModdedItemStack) item).getNative();
    }

    @Override
    default Component displayName() {
        return getPlatform().getAdventure().asAdventure(getNative().getDisplayName());
    }

    @Override
    default Component effectiveName() {
        return getPlatform().getAdventure().asAdventure(getNative().getStyledHoverName());
    }

    @Override
    default boolean isEmpty() {
        return getNative().isEmpty();
    }

    @Override
    default @Nullable Profile getProfile() {
        ResolvableProfile profile = getNative().get(DataComponents.PROFILE);
        if (profile == null) {
            return null;
        }
        return ModdedProfile.fromNative(getPlatform(), profile);
    }

    @Override
    default ItemStack withType(ItemType type) {
        if (isEmpty()) {
            return this;
        }
        net.minecraft.world.item.ItemStack oldItem = getNative();
        net.minecraft.world.item.ItemStack newItem = new net.minecraft.world.item.ItemStack(ModdedItemType.toNative(type), oldItem.getCount());
        newItem.applyComponents(oldItem.getComponentsPatch());
        return ModdedItemStack.fromNative(getPlatform(), newItem);
    }

    default <T> ItemStack withData(DataComponentType<T> type, T value) {
        net.minecraft.world.item.ItemStack item = getNative();
        item.set(type, value);
        return ModdedItemStack.fromNative(getPlatform(), item);
    }

    default <T> ItemStack withResetData(DataComponentType<T> type) {
        net.minecraft.world.item.ItemStack item = getNative();
        item.set(type, item.getItem().components().get(type));
        return ModdedItemStack.fromNative(getPlatform(), item);
    }

    default <T> ItemStack withDataOrReset(DataComponentType<T> type, @Nullable T value) {
        if (value != null) {
            return withData(type, value);
        } else {
            return withResetData(type);
        }
    }

    @Override
    default ItemStack withCustomName(Component customName) {
        return withData(DataComponents.CUSTOM_NAME, getPlatform().getAdventure().asNative(customName));
    }

    @Override
    default ItemStack withItemName(Component itemName) {
        return withData(DataComponents.ITEM_NAME, getPlatform().getAdventure().asNative(itemName));
    }

    @Override
    default ItemStack withHideTooltip(boolean hideTooltip) {
        return withData(DataComponents.TOOLTIP_DISPLAY, new TooltipDisplay(hideTooltip, ReferenceSortedSets.emptySet()));
    }

    @Override
    default ItemStack withCustomModelData(@Nullable Integer data) {
        return withDataOrReset(DataComponents.CUSTOM_MODEL_DATA,
                data != null ? new CustomModelData(List.of((float) data), List.of(), List.of(), List.of()) : null);
    }

    @Override
    default ItemStack withItemModel(@Nullable Key itemModel) {
        return withDataOrReset(DataComponents.ITEM_MODEL, MinecraftAudiences.asNative(itemModel));
    }

    @Override
    default ItemStack withEnchantmentGlintOverride(boolean value) {
        return withData(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, value);
    }

    @Override
    default ItemStack withLore(List<Component> lore) {
        return withData(DataComponents.LORE, new ItemLore(lore.stream()
                .map(getPlatform().getAdventure()::asNative)
                .toList()));
    }

    @Override
    default ItemStack withProfile(Profile profile) {
        return withData(DataComponents.PROFILE, ModdedProfile.toNative(profile));
    }

    @Override
    default ItemStack withHiddenComponents() {
        SequencedSet<DataComponentType<?>> hiddenComponents = new ReferenceLinkedOpenHashSet<>(getNative().getComponents().keySet());
        hiddenComponents.remove(DataComponents.CUSTOM_NAME);
        hiddenComponents.remove(DataComponents.LORE);
        return withData(DataComponents.TOOLTIP_DISPLAY, new TooltipDisplay(false, hiddenComponents));
    }
}
