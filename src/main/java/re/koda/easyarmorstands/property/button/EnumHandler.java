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
package re.koda.easyarmorstands.property.button;

import re.koda.easyarmorstands.EasyArmorStandsCommon;
import re.koda.easyarmorstands.api.property.Property;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class EnumHandler<T> extends ToggleHandler<T> {
    private final List<T> values;

    public EnumHandler(EasyArmorStandsCommon eas, Property<T> property, List<T> values) {
        super(eas, property);
        this.values = values;
    }

    public static <T> Function<Property<T>, EnumHandler<T>> provider(EasyArmorStandsCommon eas, Collection<T> values) {
        return property -> new EnumHandler<>(eas, property, List.copyOf(values));
    }

    public static <T> Function<Property<T>, EnumHandler<T>> provider(EasyArmorStandsCommon eas, T[] values) {
        return EnumHandler.provider(eas, List.of(values));
    }

    public static <T extends Enum<T>> Function<Property<T>, EnumHandler<T>> provider(EasyArmorStandsCommon eas, Class<T> type) {
        return EnumHandler.provider(eas, type.getEnumConstants());
    }

    @Override
    protected boolean toggleNextValue() {
        return toggleValue(1);
    }

    @Override
    protected boolean togglePreviousValue() {
        return toggleValue(-1);
    }

    protected boolean toggleValue(int offset) {
        int index = values.indexOf(property.getValue());
        index += offset;
        if (index == values.size()) {
            index = 0;
        } else if (index == -1) {
            index += values.size();
        }
        return property.setValue(values.get(index));
    }
}
