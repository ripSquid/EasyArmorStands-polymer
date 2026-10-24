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
package re.koda.easyarmorstands.element;

import re.koda.easyarmorstands.EasyArmorStandsCommon;
import re.koda.easyarmorstands.api.ArmorStandSize;
import re.koda.easyarmorstands.api.SkinPart;
import re.koda.easyarmorstands.api.editor.Session;
import re.koda.easyarmorstands.api.element.DestroyableElement;
import re.koda.easyarmorstands.api.element.Element;
import re.koda.easyarmorstands.api.element.EntityElement;
import re.koda.easyarmorstands.api.element.MenuElement;
import re.koda.easyarmorstands.api.menu.MenuBuilder;
import re.koda.easyarmorstands.api.menu.button.MenuButtonCategory;
import re.koda.easyarmorstands.api.menu.button.MenuIcon;
import re.koda.easyarmorstands.api.property.Property;
import re.koda.easyarmorstands.api.property.PropertyContainer;
import re.koda.easyarmorstands.api.property.type.ArmorStandPropertyTypes;
import re.koda.easyarmorstands.api.property.type.BlockDisplayPropertyTypes;
import re.koda.easyarmorstands.api.property.type.DisplayPropertyTypes;
import re.koda.easyarmorstands.api.property.type.EntityPropertyTypes;
import re.koda.easyarmorstands.api.property.type.InteractionPropertyTypes;
import re.koda.easyarmorstands.api.property.type.ItemDisplayPropertyTypes;
import re.koda.easyarmorstands.api.property.type.ItemFramePropertyTypes;
import re.koda.easyarmorstands.api.property.type.MannequinPropertyTypes;
import re.koda.easyarmorstands.api.property.type.PropertyType;
import re.koda.easyarmorstands.api.property.type.TextDisplayPropertyTypes;
import re.koda.easyarmorstands.editor.display.layer.DisplayBoxLayer;
import re.koda.easyarmorstands.menu.button.DestroyButton;
import re.koda.easyarmorstands.menu.button.DisplayBoxButton;
import re.koda.easyarmorstands.menu.button.MenuSlotButton;
import re.koda.easyarmorstands.menu.slot.ColorPickerSlot;
import re.koda.easyarmorstands.menu.slot.EntityCopyButton;
import re.koda.easyarmorstands.menu.slot.EquipmentPropertySlot;
import re.koda.easyarmorstands.menu.slot.ItemPropertySlot;
import re.koda.easyarmorstands.permission.Permissions;
import re.koda.easyarmorstands.platform.entity.Display;
import re.koda.easyarmorstands.platform.entity.Entity;
import re.koda.easyarmorstands.platform.entity.ItemDisplay;
import re.koda.easyarmorstands.platform.entity.Player;
import re.koda.easyarmorstands.platform.entity.TextDisplay;
import re.koda.easyarmorstands.platform.inventory.EquipmentSlot;
import re.koda.easyarmorstands.platform.inventory.ItemStack;
import re.koda.easyarmorstands.platform.util.MainHand;
import re.koda.easyarmorstands.property.button.BlockDataHandler;
import re.koda.easyarmorstands.property.button.BooleanHandler;
import re.koda.easyarmorstands.property.button.ButtonHandler;
import re.koda.easyarmorstands.property.button.ComponentHandler;
import re.koda.easyarmorstands.property.button.CustomNameHandler;
import re.koda.easyarmorstands.property.button.EnumHandler;
import re.koda.easyarmorstands.property.button.GravityHandler;
import re.koda.easyarmorstands.property.button.OptionalComponentHandler;
import re.koda.easyarmorstands.property.button.PropertyButtonBuilder;
import re.koda.easyarmorstands.property.button.ResolvableProfileHandler;
import re.koda.easyarmorstands.property.button.TextBackgroundHandler;
import re.koda.easyarmorstands.registry.ItemTypeKeys;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;

import java.util.function.Function;

public class ElementMenuPopulator {
    private final EasyArmorStandsCommon eas;

    public ElementMenuPopulator(EasyArmorStandsCommon eas) {
        this.eas = eas;
    }

    public void populateMenu(Element element, MenuBuilder builder, Player player, PropertyContainer container) {
        MenuPopulator populator = new MenuPopulator(eas, builder, container, player);
        populator.addButton(ArmorStandPropertyTypes.ARMS, ItemTypeKeys.STICK);
        populator.addButton(ArmorStandPropertyTypes.BASE_PLATE, ItemTypeKeys.STONE_SLAB);
        populator.addButton(ArmorStandPropertyTypes.GRAVITY, property -> {
            Property<Boolean> canTickProperty = container.getOrNull(ArmorStandPropertyTypes.CAN_TICK);
            ButtonHandler handler = canTickProperty != null
                    ? new GravityHandler(eas, property, canTickProperty)
                    : new BooleanHandler(eas, property);
            return new PropertyButtonBuilder<>(property)
                    .icon(MenuIcon.of(eas.platform().getItemType(ItemTypeKeys.FEATHER)))
                    .handler(handler);
        });
        populator.addButton(ArmorStandPropertyTypes.INVULNERABLE, ItemTypeKeys.GOLDEN_APPLE);
        populator.addButton(ArmorStandPropertyTypes.LOCK, ItemTypeKeys.IRON_BARS, Component.translatable("easyarmorstands.property.armor-stand.lock.description"));
        populator.addButton(ArmorStandPropertyTypes.MARKER, ItemTypeKeys.SUNFLOWER, Component.translatable("easyarmorstands.property.armor-stand.marker.description"));
        populator.addButton(ArmorStandPropertyTypes.SIZE, EnumHandler.provider(eas, ArmorStandSize.class), ItemTypeKeys.BONE_MEAL);
        populator.addButton(BlockDisplayPropertyTypes.BLOCK, BlockDataHandler.provider(eas), ItemTypeKeys.OAK_STAIRS, Component.translatable("easyarmorstands.property.block-display.block.pick"));
        populator.addButton(DisplayPropertyTypes.BILLBOARD, EnumHandler.provider(eas, Display.Billboard.class), ItemTypeKeys.IRON_BARS, Component.translatable("easyarmorstands.property.display.billboard.description"));
        populator.addButton(EntityPropertyTypes.AI, ItemTypeKeys.REDSTONE_BLOCK, Component.translatable("easyarmorstands.property.ai.description"));
        populator.addButton(EntityPropertyTypes.CUSTOM_NAME, property -> {
            Property<Boolean> customNameVisibleProperty = container.getOrNull(EntityPropertyTypes.CUSTOM_NAME_VISIBLE);
            ButtonHandler handler = customNameVisibleProperty != null
                    ? new CustomNameHandler(eas, property, customNameVisibleProperty)
                    : new OptionalComponentHandler(eas, property);
            return new PropertyButtonBuilder<>(property)
                    .icon(MenuIcon.of(eas.platform().getItemType(ItemTypeKeys.NAME_TAG)))
                    .handler(handler);
        });
        populator.addButton(EntityPropertyTypes.GLOWING, ItemTypeKeys.GLOWSTONE_DUST, Component.translatable("easyarmorstands.property.glow.description"));
        populator.addButton(EntityPropertyTypes.SILENT, ItemTypeKeys.NOTE_BLOCK, Component.translatable("easyarmorstands.property.silent.description"));
        populator.addButton(EntityPropertyTypes.VISIBLE, ItemTypeKeys.POTION);
        populator.addButton(InteractionPropertyTypes.RESPONSIVE, ItemTypeKeys.OAK_PRESSURE_PLATE, Component.translatable("easyarmorstands.property.interaction.responsive.description"));
        populator.addButton(ItemDisplayPropertyTypes.TRANSFORM, EnumHandler.provider(eas, ItemDisplay.ItemDisplayTransform.class), ItemTypeKeys.STICK, Component.translatable("easyarmorstands.property.item-display.transform.description"));
        populator.addButton(MannequinPropertyTypes.DESCRIPTION, OptionalComponentHandler.provider(eas), ItemTypeKeys.NAME_TAG, Component.translatable("easyarmorstands.property.mannequin.description.description"));
        populator.addButton(MannequinPropertyTypes.IMMOVABLE, ItemTypeKeys.BEDROCK, Component.translatable("easyarmorstands.property.mannequin.immovable.description"));
        populator.addButton(MannequinPropertyTypes.MAIN_HAND, EnumHandler.provider(eas, MainHand.class), ItemTypeKeys.IRON_SWORD);
        populator.addButton(MannequinPropertyTypes.SKIN_PART_VISIBLE.get(SkinPart.CAPE), ItemTypeKeys.BLUE_BANNER);
        populator.addButton(MannequinPropertyTypes.SKIN_PART_VISIBLE.get(SkinPart.HAT), ItemTypeKeys.IRON_HELMET);
        populator.addButton(MannequinPropertyTypes.SKIN_PART_VISIBLE.get(SkinPart.JACKET), ItemTypeKeys.IRON_CHESTPLATE);
        populator.addButton(MannequinPropertyTypes.SKIN_PART_VISIBLE.get(SkinPart.LEFT_PANTS), ItemTypeKeys.LEVER);
        populator.addButton(MannequinPropertyTypes.SKIN_PART_VISIBLE.get(SkinPart.LEFT_SLEEVE), ItemTypeKeys.LEVER);
        populator.addButton(MannequinPropertyTypes.SKIN_PART_VISIBLE.get(SkinPart.RIGHT_PANTS), ItemTypeKeys.LEVER);
        populator.addButton(MannequinPropertyTypes.SKIN_PART_VISIBLE.get(SkinPart.RIGHT_SLEEVE), ItemTypeKeys.LEVER);
        populator.addButton(MannequinPropertyTypes.POSE, EnumHandler.provider(eas, eas.platform().getValidMannequinPoses()), ItemTypeKeys.ARMOR_STAND);
        populator.addButton(MannequinPropertyTypes.PROFILE, ResolvableProfileHandler.provider(eas), ItemTypeKeys.PLAYER_HEAD);
        populator.addButton(TextDisplayPropertyTypes.ALIGNMENT, EnumHandler.provider(eas, TextDisplay.TextAlignment.class), ItemTypeKeys.FEATHER);
        populator.addButton(TextDisplayPropertyTypes.BACKGROUND, TextBackgroundHandler.provider(eas), ItemTypeKeys.STONE_SLAB);
        populator.addButton(TextDisplayPropertyTypes.SEE_THROUGH, ItemTypeKeys.GLOWSTONE_DUST);
        populator.addButton(TextDisplayPropertyTypes.SHADOW, ItemTypeKeys.STONE);
        populator.addButton(TextDisplayPropertyTypes.TEXT, ComponentHandler.provider(eas), ItemTypeKeys.NAME_TAG);
        populator.addButton(ItemFramePropertyTypes.FIXED, ItemTypeKeys.BEDROCK);

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            Property<ItemStack> property = container.getOrNull(EntityPropertyTypes.EQUIPMENT.get(slot));
            if (property != null && property.canChange(player)) {
                builder.addButton(MenuSlotButton.toButton(property.getType().key(), new EquipmentPropertySlot(eas, property, slot)));
            }
        }

        Property<ItemStack> itemDisplayItemProperty = container.getOrNull(ItemDisplayPropertyTypes.ITEM);
        if (itemDisplayItemProperty != null && itemDisplayItemProperty.canChange(player)) {
            builder.addButton(MenuSlotButton.toButton(itemDisplayItemProperty.getType().key(),
                    new ItemPropertySlot(eas, itemDisplayItemProperty)));
        }

        Property<ItemStack> itemFrameItemProperty = container.getOrNull(ItemFramePropertyTypes.ITEM);
        if (itemFrameItemProperty != null && itemFrameItemProperty.canChange(player)) {
            builder.addButton(MenuSlotButton.toButton(itemFrameItemProperty.getType().key(),
                    new ItemPropertySlot(eas, itemFrameItemProperty)));
        }

        if (element instanceof EntityElement<?> entityElement && player.hasPermission(Permissions.COPY_ENTITY)) {
            Entity entity = entityElement.getEntity();
            ItemStack item = eas.createEntitySpawnEgg(entity);
            if (!item.isEmpty()) {
                builder.addButton(new EntityCopyButton(eas, entity, element.getType(), item));
            }
        }

        if (element instanceof DisplayElement<?>) {
            Session session = eas.sessionManager().getSession(player);
            if (session != null) {
                builder.addButton(new DisplayBoxButton(eas, session, new DisplayBoxLayer(session, container)));
            }
        }

        if (element instanceof DestroyableElement destroyableElement && player.hasPermission(Permissions.DESTROY)) {
            builder.addButton(new DestroyButton(eas, destroyableElement));
        }

        if (element instanceof MenuElement menuElement && player.hasPermission(Permissions.COLOR)) {
            builder.addButton(MenuSlotButton.toButton(ColorPickerSlot.KEY, new ColorPickerSlot(eas, menuElement), MenuButtonCategory.HEADER));
        }
    }

    private record MenuPopulator(EasyArmorStandsCommon eas, MenuBuilder builder, PropertyContainer container,
                                 Player player) {
        private <T> void addButton(PropertyType<T> type, Function<Property<T>, PropertyButtonBuilder<T>> provider) {
            Property<T> property = container.getOrNull(type);
            if (property != null && property.canChange(player)) {
                builder.addButton(provider.apply(property).build());
            }
        }

        private void addButton(PropertyType<Boolean> type, Key itemTypeKey, Component... description) {
            addButton(type, BooleanHandler.provider(eas), itemTypeKey, description);
        }

        private <T> void addButton(PropertyType<T> type, Function<Property<T>, ? extends ButtonHandler> handlerProvider, Key itemTypeKey, Component... description) {
            addButton(type, handlerProvider, MenuIcon.of(eas.platform().getItemType(itemTypeKey)), description);
        }

        private <T> void addButton(PropertyType<T> type, Function<Property<T>, ? extends ButtonHandler> handlerProvider, MenuIcon icon, Component... description) {
            addButton(type, property -> new PropertyButtonBuilder<>(property)
                    .icon(icon)
                    .description(description)
                    .handler(handlerProvider.apply(property)));
        }
    }
}
