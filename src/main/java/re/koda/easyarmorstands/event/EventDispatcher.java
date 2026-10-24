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
package re.koda.easyarmorstands.event;

import re.koda.easyarmorstands.api.editor.Session;
import re.koda.easyarmorstands.api.element.ConfigurableEntityElement;
import re.koda.easyarmorstands.api.element.DestroyableElement;
import re.koda.easyarmorstands.api.element.EditableElement;
import re.koda.easyarmorstands.api.element.Element;
import re.koda.easyarmorstands.api.element.ElementType;
import re.koda.easyarmorstands.api.menu.MenuBuilder;
import re.koda.easyarmorstands.api.property.Property;
import re.koda.easyarmorstands.api.property.PropertyContainer;
import re.koda.easyarmorstands.platform.entity.Player;

public interface EventDispatcher {
    boolean dispatchCreateElement(Player player, ElementType type, PropertyContainer properties);

    boolean dispatchDestroyElement(Player player, DestroyableElement element);

    boolean dispatchEditElement(Player player, EditableElement element);

    boolean dispatchDiscoverElement(Player player, EditableElement element);

    <T> boolean dispatchEditProperty(Player player, Element element, Property<T> property, T oldValue, T newValue);

    void dispatchCommitElement(Player player, Element element);

    void dispatchSessionStop(Session session);

    void dispatchSessionStart(Session session);

    void dispatchEntityElementInitialize(ConfigurableEntityElement<?> element);

    void dispatchSpawnMenuOpen(Player player, MenuBuilder builder);

    void dispatchElementMenuOpen(Player player, Element element, MenuBuilder builder, PropertyContainer properties);
}
