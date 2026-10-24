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
package re.koda.easyarmorstands.history;

import re.koda.easyarmorstands.EasyArmorStandsCommon;
import re.koda.easyarmorstands.api.element.Element;
import re.koda.easyarmorstands.api.property.type.PropertyType;
import re.koda.easyarmorstands.history.action.Action;
import re.koda.easyarmorstands.history.action.PropertyAction;

import java.util.Objects;

public class ChangeKey<T> {
    private final EasyArmorStandsCommon eas;
    private final Element element;
    private final PropertyType<T> propertyType;

    public ChangeKey(EasyArmorStandsCommon eas, Element element, PropertyType<T> propertyType) {
        this.eas = eas;
        this.element = element;
        this.propertyType = propertyType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChangeKey<?> changeKey = (ChangeKey<?>) o;
        return Objects.equals(element, changeKey.element) && Objects.equals(propertyType, changeKey.propertyType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(element, propertyType);
    }

    public Action createChangeAction(T oldValue, T value) {
        return new PropertyAction<>(eas, element.getReference(eas.referenceProvider()), propertyType, oldValue, value);
    }
}
