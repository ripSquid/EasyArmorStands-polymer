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
package re.koda.easyarmorstands.editor.armorstand;

import re.koda.easyarmorstands.api.property.Property;
import re.koda.easyarmorstands.api.property.PropertyContainer;
import re.koda.easyarmorstands.api.property.type.EntityPropertyTypes;
import re.koda.easyarmorstands.api.util.RotationProvider;
import re.koda.easyarmorstands.platform.util.Location;
import re.koda.easyarmorstands.util.EasMath;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaterniond;
import org.joml.Quaterniondc;

public class ArmorStandRotationProvider implements RotationProvider {
    private final Property<Location> locationProperty;

    public ArmorStandRotationProvider(PropertyContainer properties) {
        this.locationProperty = properties.get(EntityPropertyTypes.LOCATION);
    }

    @Override
    public @NotNull Quaterniondc getRotation() {
        return EasMath.getEntityYawRotation(locationProperty.getValue().yaw(), new Quaterniond());
    }
}
