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
package re.koda.easyarmorstands.fabric.event;

import re.koda.easyarmorstands.api.editor.Session;
import re.koda.easyarmorstands.api.element.ConfigurableEntityElement;
import re.koda.easyarmorstands.api.element.DestroyableElement;
import re.koda.easyarmorstands.api.element.EditableElement;
import re.koda.easyarmorstands.api.element.Element;
import re.koda.easyarmorstands.api.element.ElementType;
import re.koda.easyarmorstands.api.menu.MenuBuilder;
import re.koda.easyarmorstands.api.property.Property;
import re.koda.easyarmorstands.api.property.PropertyContainer;
import re.koda.easyarmorstands.event.EventDispatcher;
import re.koda.easyarmorstands.fabric.api.event.EasyArmorStandsEvents;
import re.koda.easyarmorstands.platform.entity.Player;
import org.jspecify.annotations.NullMarked;

import static re.koda.easyarmorstands.platform.modded.entity.ModdedEntity.toNative;
import static re.koda.easyarmorstands.platform.modded.entity.ModdedPlayer.toNative;

@NullMarked
public class FabricEventDispatcher implements EventDispatcher {
    @Override
    public boolean dispatchCreateElement(Player player, ElementType type, PropertyContainer properties) {
        return EasyArmorStandsEvents.CREATE_ELEMENT.invoker().onCreateElement(toNative(player), type, properties);
    }

    @Override
    public boolean dispatchDestroyElement(Player player, DestroyableElement element) {
        return EasyArmorStandsEvents.DESTROY_ELEMENT.invoker().onDestroyElement(toNative(player), element);
    }

    @Override
    public boolean dispatchEditElement(Player player, EditableElement element) {
        return EasyArmorStandsEvents.EDIT_ELEMENT.invoker().onEditElement(toNative(player), element);
    }

    @Override
    public boolean dispatchDiscoverElement(Player player, EditableElement element) {
        return EasyArmorStandsEvents.DISCOVER_ELEMENT.invoker().onDiscoverElement(toNative(player), element);
    }

    @Override
    public <T> boolean dispatchEditProperty(Player player, Element element, Property<T> property, T oldValue, T newValue) {
        return EasyArmorStandsEvents.EDIT_PROPERTY.invoker().onEditProperty(toNative(player), element, property, oldValue, newValue);
    }

    @Override
    public void dispatchCommitElement(Player player, Element element) {
        EasyArmorStandsEvents.COMMIT_ELEMENT.invoker().onCommitElement(toNative(player), element);
    }

    @Override
    public void dispatchSessionStop(Session session) {
        EasyArmorStandsEvents.SESSION_STOP.invoker().onSessionStop(toNative(session.player()), session);
    }

    @Override
    public void dispatchSessionStart(Session session) {
        EasyArmorStandsEvents.SESSION_START.invoker().onSessionStart(toNative(session.player()), session);
    }

    @Override
    public void dispatchEntityElementInitialize(ConfigurableEntityElement<?> element) {
        EasyArmorStandsEvents.ENTITY_ELEMENT_INITIALIZE.invoker().onEntityElementInitialize(toNative(element.getEntity()), element);
    }

    @Override
    public void dispatchSpawnMenuOpen(Player player, MenuBuilder builder) {
        EasyArmorStandsEvents.SPAWN_MENU_OPEN.invoker().onSpawnMenuOpen(toNative(player), builder);
    }

    @Override
    public void dispatchElementMenuOpen(Player player, Element element, MenuBuilder builder, PropertyContainer properties) {
        EasyArmorStandsEvents.ELEMENT_MENU_OPEN.invoker().onElementMenuOpen(toNative(player), element, builder, properties);
    }
}
