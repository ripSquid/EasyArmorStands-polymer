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
package re.koda.easyarmorstands.command.sender;

import re.koda.easyarmorstands.EasyArmorStandsCommon;
import re.koda.easyarmorstands.api.element.DestroyableElement;
import re.koda.easyarmorstands.api.element.EditableElement;
import re.koda.easyarmorstands.api.element.Element;
import re.koda.easyarmorstands.api.element.ElementType;
import re.koda.easyarmorstands.api.property.Property;
import re.koda.easyarmorstands.api.property.PropertyContainer;
import re.koda.easyarmorstands.clipboard.Clipboard;
import re.koda.easyarmorstands.event.EventDispatcher;
import re.koda.easyarmorstands.history.ChangeTracker;
import re.koda.easyarmorstands.history.History;
import re.koda.easyarmorstands.platform.entity.Player;
import re.koda.easyarmorstands.session.SessionImpl;
import net.kyori.adventure.identity.Identity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class EasPlayer extends EasCommandSender {
    private final @NotNull EasyArmorStandsCommon eas;
    private final @NotNull Player player;
    private @Nullable History history;
    private @Nullable Clipboard clipboard;

    public EasPlayer(@NotNull EasyArmorStandsCommon eas, @NotNull Player player) {
        super(player);
        this.eas = eas;
        this.player = player;
    }

    @Override
    public @NotNull Player get() {
        return player;
    }

    public @NotNull History history() {
        if (history == null) {
            eas.platform().checkMainThread();
            history = eas.getHistory(player);
        }
        return history;
    }

    public @NotNull Clipboard clipboard() {
        if (clipboard == null) {
            eas.platform().checkMainThread();
            clipboard = eas.getClipboard(player);
        }
        return clipboard;
    }

    public @NotNull ChangeTracker tracker() {
        return history().getTracker();
    }

    public @NotNull Locale locale() {
        return getOrDefault(Identity.LOCALE, Locale.US);
    }

    public @Nullable SessionImpl session() {
        return eas.sessionManager().getSession(player);
    }

    private EventDispatcher getEventDispatcher() {
        return eas.eventDispatcher();
    }

    public boolean canCreateElement(ElementType type, PropertyContainer properties) {
        if (!type.canSpawn(player)) {
            return false;
        }
        return getEventDispatcher().dispatchCreateElement(player, type, properties);
    }

    public boolean canDestroyElement(DestroyableElement element) {
        if (!element.canDestroy(player)) {
            return false;
        }
        return getEventDispatcher().dispatchDestroyElement(player, element);
    }

    public boolean canEditElement(EditableElement element) {
        if (!element.canEdit(player)) {
            return false;
        }
        return getEventDispatcher().dispatchEditElement(player, element);
    }

    public boolean canDiscoverElement(EditableElement element) {
        if (!element.canEdit(player)) {
            return false;
        }
        return getEventDispatcher().dispatchDiscoverElement(player, element);
    }

    public <T> boolean canChangeProperty(Element element, Property<T> property, T value) {
        if (!element.isValid()) {
            return false;
        }
        return getEventDispatcher().dispatchEditProperty(player, element, property, property.getValue(), value);
    }
}
