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
package re.koda.easyarmorstands.config.serializer;

import re.koda.easyarmorstands.item.ItemRenderer;
import re.koda.easyarmorstands.item.SimpleItemTemplate;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;
import re.koda.easyarmorstands.platform.Platform;
import re.koda.easyarmorstands.platform.inventory.ItemStack;
import re.koda.easyarmorstands.platform.inventory.ItemType;
import re.koda.easyarmorstands.registry.ItemTypeKeys;
import re.koda.easyarmorstands.util.ItemTemplate;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Supplier;

public class ItemTemplateSerializer implements TypeSerializer<ItemTemplate> {
    private final Platform platform;

    public ItemTemplateSerializer(Platform platform) {
        this.platform = platform;
    }

    @Override
    public ItemTemplate deserialize(Type type, ConfigurationNode node) throws SerializationException {
        ItemType itemType = node.node("type").get(ItemType.class, (Supplier<ItemType>) () -> platform.getItemType(ItemTypeKeys.AIR));
        ItemStack template = itemType.createItemStack(node.node("amount").getInt(1))
                .withHideTooltip(node.node("hide-tooltip").getBoolean())
                .withCustomModelData(node.node("custom-model-data").get(Integer.class))
                .withItemModel(node.node("item-model").get(Key.class));
        String name = node.node("name").getString("");
        List<String> description = node.node("description").getList(String.class);
        return new SimpleItemTemplate(template, name, description, TagResolver.empty(), ItemRenderer.button());
    }

    @Override
    public void serialize(Type type, @Nullable ItemTemplate obj, ConfigurationNode node) throws SerializationException {
        throw new SerializationException(new UnsupportedOperationException());
    }
}
