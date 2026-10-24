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

import re.koda.easyarmorstands.api.ArmorStandSize;
import re.koda.easyarmorstands.api.property.Property;
import re.koda.easyarmorstands.api.property.PropertyContainer;
import re.koda.easyarmorstands.api.property.type.ArmorStandPropertyTypes;
import re.koda.easyarmorstands.editor.OffsetProvider;
import re.koda.easyarmorstands.element.ArmorStandElement;
import org.joml.Vector3d;
import org.joml.Vector3dc;

public class ArmorStandOffsetProvider implements OffsetProvider {
    private final ArmorStandElement element;
    private final Property<ArmorStandSize> sizeProperty;

    public ArmorStandOffsetProvider(ArmorStandElement element, PropertyContainer container) {
        this.element = element;
        this.sizeProperty = container.get(ArmorStandPropertyTypes.SIZE);
    }

    @Override
    public Vector3dc getOffset() {
        double offset = 1.25 * element.getScale();
        if (sizeProperty.getValue().isSmall()) {
            offset /= 2;
        }
        return new Vector3d(0, offset, 0);
    }
}
