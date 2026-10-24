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
package re.koda.easyarmorstands.property.entity;

import re.koda.easyarmorstands.EasyArmorStandsCommon;
import re.koda.easyarmorstands.api.property.Property;
import re.koda.easyarmorstands.api.property.type.PropertyType;
import re.koda.easyarmorstands.platform.entity.Entity;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

@NullMarked
public class EntityComponentProperty implements Property<Component> {
    private static final MiniMessage serializer = EasyArmorStandsCommon.miniMessage();
    private static final MiniMessage nonVirtualSerializer = MiniMessage.builder()
            .emitVirtuals(false)
            .build();
    private final PropertyType<Component> type;
    private final Key key;
    private final Entity entity;
    private final Supplier<Component> getter;
    private final Consumer<Component> setter;

    public EntityComponentProperty(PropertyType<Component> type, Entity entity, Supplier<Component> getter, Consumer<Component> setter) {
        this.type = type;
        this.key = type.key();
        this.entity = entity;
        this.getter = getter;
        this.setter = setter;
    }

    public static Component resolveValue(Entity entity, Key key, Component realText) {
        String miniMessageText = entity.getCustomDataString(key);
        if (miniMessageText != null) {
            if (nonVirtualSerializer.deserialize(miniMessageText).equals(realText.compact())) {
                return serializer.deserialize(miniMessageText);
            }
        }
        return realText;
    }

    public static void saveValue(Entity entity, Key key, @Nullable Component value) {
        if (value != null) {
            entity.setCustomDataString(key, serializer.serialize(value));
        } else {
            entity.removeCustomData(key);
        }
    }

    @Override
    public PropertyType<Component> getType() {
        return type;
    }

    @Override
    public Component getValue() {
        return resolveValue(entity, key, getter.get());
    }

    @Override
    public boolean setValue(Component value) {
        setter.accept(value);
        saveValue(entity, key, value);
        return true;
    }
}
