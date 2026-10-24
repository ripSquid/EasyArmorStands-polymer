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
import re.koda.easyarmorstands.editor.OffsetProvider;
import re.koda.easyarmorstands.platform.util.Location;
import re.koda.easyarmorstands.util.EasMath;
import org.joml.Quaterniond;
import org.joml.Quaternionfc;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import org.joml.Vector3fc;

public class BlockDisplayOffsetProvider implements OffsetProvider {
    private final Property<Location> locationProperty;
    private final Property<Vector3fc> translationProperty;
    private final Property<Quaternionfc> leftRotationProperty;
    private final Property<Vector3fc> scaleProperty;
    private final Property<Quaternionfc> rightRotationProperty;
    private final Quaterniond currentRotation = new Quaterniond();
    private final Vector3d currentOffset = new Vector3d();

    public BlockDisplayOffsetProvider(PropertyContainer properties) {
        this.locationProperty = properties.get(EntityPropertyTypes.LOCATION);
        this.translationProperty = properties.get(DisplayPropertyTypes.TRANSLATION);
        this.leftRotationProperty = properties.get(DisplayPropertyTypes.LEFT_ROTATION);
        this.scaleProperty = properties.get(DisplayPropertyTypes.SCALE);
        this.rightRotationProperty = properties.get(DisplayPropertyTypes.RIGHT_ROTATION);
    }

    @Override
    public Vector3dc getOffset() {
        Location location = locationProperty.getValue();
        return currentOffset.set(0.5)
                .rotate(new Quaterniond(rightRotationProperty.getValue()))
                .mul(scaleProperty.getValue())
                .rotate(new Quaterniond(leftRotationProperty.getValue()))
                .add(translationProperty.getValue())
                .rotate(EasMath.getEntityRotation(location.yaw(), location.pitch(), currentRotation));
    }
}
