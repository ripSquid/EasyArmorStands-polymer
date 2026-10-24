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
package re.koda.easyarmorstands.command.util;

import re.koda.easyarmorstands.EasyArmorStandsCommon;
import re.koda.easyarmorstands.api.element.Element;
import re.koda.easyarmorstands.api.property.PropertyContainer;
import re.koda.easyarmorstands.command.sender.EasPlayer;
import re.koda.easyarmorstands.group.property.GroupPropertyContainer;
import re.koda.easyarmorstands.property.TrackedPropertyContainer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ElementSelection {
    private final EasyArmorStandsCommon eas;
    private final Collection<Element> elements;

    public ElementSelection(EasyArmorStandsCommon eas, Collection<Element> elements) {
        this.eas = eas;
        this.elements = Collections.unmodifiableCollection(elements);
    }

    public @NotNull Collection<Element> elements() {
        return elements;
    }

    public @NotNull PropertyContainer properties(EasPlayer player) {
        if (elements.size() == 1) {
            return new TrackedPropertyContainer(eas, elements.iterator().next(), player);
        }
        List<PropertyContainer> containers = new ArrayList<>(elements.size());
        for (Element element : elements) {
            containers.add(new TrackedPropertyContainer(eas, element, player));
        }
        return new GroupPropertyContainer(containers);
    }
}
