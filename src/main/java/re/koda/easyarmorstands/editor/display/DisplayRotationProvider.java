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
package re.koda.easyarmorstands.editor.display;

import re.koda.easyarmorstands.api.property.Property;
import re.koda.easyarmorstands.api.property.PropertyContainer;
import re.koda.easyarmorstands.api.property.type.DisplayPropertyTypes;
import re.koda.easyarmorstands.api.property.type.EntityPropertyTypes;
import re.koda.easyarmorstands.api.util.RotationProvider;
import re.koda.easyarmorstands.platform.util.Location;
import re.koda.easyarmorstands.util.EasMath;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaterniond;
import org.joml.Quaterniondc;
import org.joml.Quaternionfc;

public class DisplayRotationProvider implements RotationProvider {
    private final Property<Location> locationProperty;
    private final Property<Quaternionfc> leftRotationProperty;

    public DisplayRotationProvider(PropertyContainer properties) {
        this.locationProperty = properties.get(EntityPropertyTypes.LOCATION);
        this.leftRotationProperty = properties.get(DisplayPropertyTypes.LEFT_ROTATION);
    }

    @Override
    public @NotNull Quaterniondc getRotation() {
        Location location = locationProperty.getValue();
        return EasMath.getEntityRotation(location.yaw(), location.pitch(), new Quaterniond())
                .mul(new Quaterniond(leftRotationProperty.getValue()));
    }
}
