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
import re.koda.easyarmorstands.api.property.Property;
import re.koda.easyarmorstands.api.property.PropertyMap;
import re.koda.easyarmorstands.api.property.type.DisplayPropertyTypes;
import re.koda.easyarmorstands.api.property.type.EntityPropertyTypes;
import re.koda.easyarmorstands.platform.entity.Display;
import re.koda.easyarmorstands.platform.entity.EntityType;
import re.koda.easyarmorstands.platform.util.Location;
import re.koda.easyarmorstands.registry.EntityTypeKeys;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class DisplayElementType<E extends Display> extends SimpleEntityElementType<E> {
    public DisplayElementType(EasyArmorStandsCommon eas, EntityType entityType, Class<E> entityClass) {
        super(eas, entityType, entityClass);
    }

    @Override
    protected SimpleEntityElement<E> createInstance(E entity) {
        return new DisplayElement<>(eas, entity, this);
    }

    @Override
    public void applyDefaultProperties(@NotNull PropertyMap properties) {
        super.applyDefaultProperties(properties);
        Property<Location> locationProperty = properties.get(EntityPropertyTypes.LOCATION);
        Location location = locationProperty.getValue().withYaw(0).withPitch(0);
        locationProperty.setValue(location);
        properties.put(DisplayPropertyTypes.INTERPOLATION_DURATION, 3);
        properties.put(DisplayPropertyTypes.TELEPORT_DURATION, 3);
        if (EntityTypeKeys.BLOCK_DISPLAY.equals(getEntityType().key()) && eas.getConfiguration().editor.centeredPivot) {
            properties.put(DisplayPropertyTypes.TRANSLATION, new Vector3f(-0.5f));
        }
    }
}
