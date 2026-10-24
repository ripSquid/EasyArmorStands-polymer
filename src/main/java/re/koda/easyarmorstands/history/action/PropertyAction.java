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
import re.koda.easyarmorstands.api.element.Element;
import re.koda.easyarmorstands.api.element.ElementReference;
import re.koda.easyarmorstands.api.property.Property;
import re.koda.easyarmorstands.api.property.PropertyContainer;
import re.koda.easyarmorstands.api.property.type.PropertyType;
import re.koda.easyarmorstands.command.sender.EasPlayer;
import re.koda.easyarmorstands.message.Message;
import re.koda.easyarmorstands.property.PermissionCheckedPropertyContainer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PropertyAction<T> implements Action {
    private final EasyArmorStandsCommon eas;
    private final ElementReference elementReference;
    private final PropertyType<T> propertyType;
    private final T oldValue;
    private final T newValue;

    public PropertyAction(EasyArmorStandsCommon eas, ElementReference elementReference, PropertyType<T> propertyType, T oldValue, T newValue) {
        this.eas = eas;
        this.elementReference = elementReference;
        this.propertyType = propertyType;
        this.oldValue = propertyType.cloneValue(oldValue);
        this.newValue = propertyType.cloneValue(newValue);
    }

    @Override
    public boolean execute(EasPlayer player) {
        return tryChange(newValue, player);
    }

    @Override
    public boolean undo(EasPlayer player) {
        return tryChange(oldValue, player);
    }

    private boolean tryChange(T value, EasPlayer player) {
        Element element = elementReference.getElement();
        if (element == null) {
            return false;
        }
        PropertyContainer properties = new PermissionCheckedPropertyContainer(eas, element, player);
        Property<T> property = properties.getOrNull(propertyType);
        if (property == null) {
            return false;
        }
        boolean result = property.setValue(value);
        properties.commit();
        return result;
    }

    @Override
    public Component describe() {
        return Message.component("easyarmorstands.history.changed-property",
                propertyType.getName().colorIfAbsent(NamedTextColor.WHITE),
                propertyType.getValueComponent(oldValue).colorIfAbsent(NamedTextColor.WHITE),
                propertyType.getValueComponent(newValue).colorIfAbsent(NamedTextColor.WHITE));
    }

    @Override
    public void onEntityReplaced(@NotNull UUID oldId, @NotNull UUID newId) {
        elementReference.onEntityReplaced(oldId, newId);
    }
}
