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

import re.koda.easyarmorstands.api.property.Property;
import re.koda.easyarmorstands.api.property.type.PropertyType;
import re.koda.easyarmorstands.platform.entity.Entity;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

@NullMarked
public class EntityOptionalComponentProperty implements Property<Optional<Component>> {
    private final PropertyType<Optional<Component>> type;
    private final Entity entity;
    private final Supplier<@Nullable Component> getter;
    private final Consumer<@Nullable Component> setter;

    public EntityOptionalComponentProperty(PropertyType<Optional<Component>> type, Entity entity, Supplier<@Nullable Component> getter, Consumer<@Nullable Component> setter) {
        this.type = type;
        this.entity = entity;
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public PropertyType<Optional<Component>> getType() {
        return type;
    }

    @Override
    public Optional<Component> getValue() {
        Component realText = getter.get();
        if (realText == null) {
            return Optional.empty();
        }
        return Optional.of(EntityComponentProperty.resolveValue(entity, type.key(), realText));
    }

    @Override
    public boolean setValue(Optional<Component> value) {
        setter.accept(value.orElse(null));
        EntityComponentProperty.saveValue(entity, type.key(), value.orElse(null));
        return true;
    }
}
