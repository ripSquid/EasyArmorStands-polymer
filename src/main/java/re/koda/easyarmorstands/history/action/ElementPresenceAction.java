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
package re.koda.easyarmorstands.history.action;

import re.koda.easyarmorstands.EasyArmorStandsCommon;
import re.koda.easyarmorstands.api.element.DestroyableElement;
import re.koda.easyarmorstands.api.element.Element;
import re.koda.easyarmorstands.api.element.ElementReference;
import re.koda.easyarmorstands.api.element.ElementType;
import re.koda.easyarmorstands.api.element.EntityElement;
import re.koda.easyarmorstands.api.element.EntityElementReference;
import re.koda.easyarmorstands.api.element.EntityElementType;
import re.koda.easyarmorstands.api.property.PropertyContainer;
import re.koda.easyarmorstands.api.property.PropertyMap;
import re.koda.easyarmorstands.api.property.type.EntityPropertyTypes;
import re.koda.easyarmorstands.command.sender.EasPlayer;
import re.koda.easyarmorstands.permission.Permissions;
import re.koda.easyarmorstands.platform.entity.Entity;
import re.koda.easyarmorstands.platform.entity.EntitySnapshot;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

abstract class ElementPresenceAction implements Action {
    private final EasyArmorStandsCommon eas;
    private final PropertyContainer properties;
    private final @Nullable EntitySnapshot snapshot;
    private ElementReference reference;

    public ElementPresenceAction(@NotNull EasyArmorStandsCommon eas, @NotNull Element element) {
        this.eas = eas;
        this.properties = new PropertyMap(element.getProperties());
        this.snapshot = getSnapshot(element);
        this.reference = element.getReference(eas.referenceProvider());
    }

    private static @Nullable EntitySnapshot getSnapshot(Element element) {
        if (element instanceof EntityElement<?> entityElement) {
            return entityElement.getEntity().createSnapshot();
        } else {
            return null;
        }
    }

    protected boolean create(EasPlayer player) {
        ElementType type = reference.getType();
        if (!player.canCreateElement(type, properties)) {
            return false;
        }

        Element element = null;
        if (type instanceof EntityElementType<?> entityElementType && player.get().hasPermission(Permissions.COPY_ENTITY)) {
            element = createEntity(entityElementType);
        }
        if (element == null) {
            element = type.createElement(properties);
        }
        if (element == null) {
            return false;
        }

        UUID oldId = getId(reference);
        reference = element.getReference(eas.referenceProvider());
        UUID newId = getId(reference);
        if (oldId != null && newId != null) {
            eas.historyManager().onEntityReplaced(oldId, newId);
        }

        return true;
    }

    protected boolean destroy(EasPlayer player) {
        if (reference == null) {
            return false;
        }

        Element element = reference.getElement();
        if (!(element instanceof DestroyableElement destroyableElement)) {
            return false;
        }

        if (!player.canDestroyElement(destroyableElement)) {
            return false;
        }

        destroyableElement.destroy();
        return true;
    }

    private <E extends Entity> @Nullable Element createEntity(EntityElementType<E> type) {
        if (snapshot == null) {
            return null;
        }
        E entity = type.getEntityClass().cast(snapshot.createEntity(properties.get(EntityPropertyTypes.LOCATION).getValue()));
        return type.getElement(entity);
    }

    @Override
    public void onEntityReplaced(@NotNull UUID oldId, @NotNull UUID newId) {
        if (reference != null) {
            reference.onEntityReplaced(oldId, newId);
        }
    }

    private UUID getId(ElementReference reference) {
        if (reference instanceof EntityElementReference) {
            return ((EntityElementReference<?>) reference).getId();
        }
        return null;
    }

    public ElementType getType() {
        return reference.getType();
    }
}
