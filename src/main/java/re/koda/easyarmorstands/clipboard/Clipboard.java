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
package re.koda.easyarmorstands.clipboard;

import re.koda.easyarmorstands.EasyArmorStandsCommon;
import re.koda.easyarmorstands.api.element.Element;
import re.koda.easyarmorstands.api.property.Property;
import re.koda.easyarmorstands.api.property.PropertyMap;
import re.koda.easyarmorstands.api.property.type.PropertyType;
import re.koda.easyarmorstands.command.sender.EasPlayer;
import re.koda.easyarmorstands.message.Message;
import re.koda.easyarmorstands.permission.Permissions;
import re.koda.easyarmorstands.platform.entity.Player;
import re.koda.easyarmorstands.property.TrackedPropertyContainer;
import re.koda.easyarmorstands.util.PropertyCopier;

import java.util.ArrayList;
import java.util.List;

public class Clipboard {
    private final EasyArmorStandsCommon eas;
    private final PropertyMap properties = new PropertyMap();
    private Player player;

    Clipboard(EasyArmorStandsCommon eas, Player player) {
        this.eas = eas;
        this.player = player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public PropertyMap getProperties() {
        removeDisallowed();
        return properties;
    }

    public void handleAutoApply(Element element) {
        removeDisallowed();
        if (this.properties.isEmpty()) {
            return;
        }

        PropertyCopier copier = new PropertyCopier();
        TrackedPropertyContainer properties = new TrackedPropertyContainer(eas, element, new EasPlayer(eas, player));
        copier.copyProperties(properties, this.properties);
        properties.commit(Message.component("easyarmorstands.history.clipboard-pasted-automatically"));

        if (copier.getSuccessCount() > 0) {
            player.sendMessage(Message.hint("easyarmorstands.hint.clipboard-auto-applied"));
            player.sendMessage(Message.hint("easyarmorstands.hint.clear-clipboard", Message.command("/eas clipboard clear")));
        }
    }

    public <T> void handlePropertyShiftClick(Property<T> property) {
        PropertyType<T> type = property.getType();
        if (player.hasPermission(Permissions.CLIPBOARD) && type.canCopy(player)) {
            properties.put(type, property.getValue());
            player.sendMessage(Message.success("easyarmorstands.success.property-copied", type.getName()));
        }
    }

    private void removeDisallowed() {
        List<PropertyType<?>> types = new ArrayList<>();
        properties.forEach(property -> types.add(property.getType()));

        for (PropertyType<?> type : types) {
            if (!type.canCopy(player)) {
                properties.remove(type);
            }
        }
    }
}
