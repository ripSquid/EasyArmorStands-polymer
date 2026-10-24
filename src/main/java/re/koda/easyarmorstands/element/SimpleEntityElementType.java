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
import re.koda.easyarmorstands.api.element.EntityElementType;
import re.koda.easyarmorstands.api.property.Property;
import re.koda.easyarmorstands.api.property.PropertyContainer;
import re.koda.easyarmorstands.api.property.type.EntityPropertyTypes;
import re.koda.easyarmorstands.api.property.type.PropertyType;
import re.koda.easyarmorstands.permission.Permissions;
import re.koda.easyarmorstands.platform.entity.Entity;
import re.koda.easyarmorstands.platform.entity.EntityType;
import re.koda.easyarmorstands.platform.entity.Player;
import re.koda.easyarmorstands.platform.util.Location;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public abstract class SimpleEntityElementType<E extends Entity> implements EntityElementType<E> {
    protected final EasyArmorStandsCommon eas;
    private final EntityType entityType;
    private final Class<E> entityClass;
    private final Component displayName;

    public SimpleEntityElementType(EasyArmorStandsCommon eas, EntityType entityType, Class<E> entityClass) {
        this.eas = eas;
        this.entityType = entityType;
        this.entityClass = entityClass;
        this.displayName = Component.translatable(entityType);
    }

    @Override
    public @NotNull Key key() {
        return entityType.key();
    }

    @Override
    public @NotNull EntityType getEntityType() {
        return entityType;
    }

    @Override
    public @NotNull Class<E> getEntityClass() {
        return entityClass;
    }

    protected SimpleEntityElement<E> createInstance(E entity) {
        return new SimpleEntityElement<>(eas, entity, this);
    }

    @Override
    public SimpleEntityElement<E> getElement(@NotNull E entity) {
        SimpleEntityElement<E> element = createInstance(entity);
        new EntityElementPopulator(eas.platform()).registerProperties(element);
        eas.eventDispatcher().dispatchEntityElementInitialize(element);
        return element;
    }

    @Override
    public @Nullable SimpleEntityElement<E> createElement(@NotNull PropertyContainer properties) {
        Property<Location> locationProperty = properties.getOrNull(EntityPropertyTypes.LOCATION);
        if (locationProperty == null) {
            return null;
        }
        Location location = locationProperty.getValue();
        SpawnedEntityConfigurator configurator = new SpawnedEntityConfigurator(properties);
        @SuppressWarnings("unchecked")
        E entity = (E) location.world().spawn(location, entityType, e -> configurator.accept((E) e));
        SimpleEntityElement<E> element = configurator.getElement();
        if (element != null) {
            entity.teleport(location);
        } else {
            entity.remove();
        }
        return element;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return displayName;
    }

    @Override
    public boolean canSpawn(@NotNull Player player) {
        return player.hasPermission(Permissions.entityType(Permissions.SPAWN, entityType));
    }

    private class SpawnedEntityConfigurator implements Consumer<E> {
        private final PropertyContainer properties;
        private SimpleEntityElement<E> element;

        private SpawnedEntityConfigurator(PropertyContainer properties) {
            this.properties = properties;
        }

        @Override
        public void accept(E entity) {
            eas.setEntityElementType(entity, SimpleEntityElementType.this.key());
            element = SimpleEntityElementType.this.getElement(entity);
            if (element != null) {
                copyProperties(properties, element.getProperties());
            }
        }

        private void copyProperties(PropertyContainer source, PropertyContainer target) {
            source.forEach(p -> copyProperty(p, target));
        }

        private <T> void copyProperty(Property<T> sourceProperty, PropertyContainer target) {
            PropertyType<T> type = sourceProperty.getType();
            if (type == EntityPropertyTypes.LOCATION) {
                // Ignore the location property, we spawned the entity at that location
                return;
            }
            Property<T> targetProperty = target.getOrNull(type);
            if (targetProperty != null) {
                targetProperty.setValue(sourceProperty.getValue());
            }
        }

        public @Nullable SimpleEntityElement<E> getElement() {
            return element;
        }
    }
}
