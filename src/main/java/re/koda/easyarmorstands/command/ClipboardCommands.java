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
package re.koda.easyarmorstands.command;

import re.koda.easyarmorstands.api.element.Element;
import re.koda.easyarmorstands.api.property.Property;
import re.koda.easyarmorstands.api.property.PropertyContainer;
import re.koda.easyarmorstands.api.property.type.PropertyType;
import re.koda.easyarmorstands.clipboard.Clipboard;
import re.koda.easyarmorstands.command.requirement.RequireElement;
import re.koda.easyarmorstands.command.requirement.RequireElementSelection;
import re.koda.easyarmorstands.command.sender.EasPlayer;
import re.koda.easyarmorstands.command.util.ElementSelection;
import re.koda.easyarmorstands.message.Message;
import re.koda.easyarmorstands.permission.Permissions;
import re.koda.easyarmorstands.util.PropertyCopier;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.CommandDescription;
import org.incendo.cloud.annotations.Permission;

@Command("eas")
public class ClipboardCommands {
    @Command("clipboard")
    @Permission(Permissions.CLIPBOARD)
    @CommandDescription("easyarmorstands.command.description.clipboard")
    public void clipboard(EasPlayer sender, Clipboard clipboard) {
        if (clipboard.getProperties().isEmpty()) {
            sender.sendMessage(Message.warning("easyarmorstands.warning.clipboard-empty"));
            return;
        }
        sender.sendMessage(Message.title("easyarmorstands.title.clipboard"));
        clipboard.getProperties().forEach(property -> sender.sendMessage(Component.text()
                .content("* ")
                .color(NamedTextColor.GRAY)
                .append(describeProperty(property))));
        sender.sendMessage(Message.hint("easyarmorstands.hint.paste-clipboard", Message.command("/eas paste")));
        sender.sendMessage(Message.hint("easyarmorstands.hint.clear-clipboard", Message.command("/eas clipboard clear")));
    }

    private <T> Component describeProperty(Property<T> property) {
        PropertyType<T> type = property.getType();
        return Component.text()
                .append(type.getName())
                .append(Component.text(": "))
                .append(type.getValueComponent(property.getValue()))
                .build();
    }

    @Command("clipboard clear")
    @Permission(Permissions.CLIPBOARD)
    @CommandDescription("easyarmorstands.command.description.clipboard.clear")
    public void clear(EasPlayer sender, Clipboard clipboard) {
        if (clipboard.getProperties().isEmpty()) {
            sender.sendMessage(Message.warning("easyarmorstands.warning.clipboard-empty"));
        } else {
            clipboard.getProperties().clear();
            sender.sendMessage(Message.success("easyarmorstands.success.clipboard-cleared"));
        }
    }

    @Command("copy")
    @Permission(Permissions.CLIPBOARD)
    @CommandDescription("easyarmorstands.command.description.copy")
    @RequireElement
    public void copy(EasPlayer sender, Clipboard clipboard, Element element) {
        element.getProperties().forEach(property -> {
            if (property.getType().canCopy(sender.get())) {
                copyProperty(clipboard, property);
            }
        });
        sender.sendMessage(Message.success("easyarmorstands.success.clipboard-copied"));
        sender.sendMessage(Message.hint("easyarmorstands.hint.show-clipboard", Message.command("/eas clipboard")));
        sender.sendMessage(Message.hint("easyarmorstands.hint.paste-clipboard", Message.command("/eas paste")));
    }

    private <T> void copyProperty(Clipboard clipboard, Property<T> property) {
        clipboard.getProperties().put(property.getType(), property.getValue());
    }

    @Command("paste")
    @Permission(Permissions.CLIPBOARD)
    @CommandDescription("easyarmorstands.command.description.paste")
    @RequireElementSelection
    public void paste(EasPlayer sender, Clipboard clipboard, ElementSelection selection) {
        if (clipboard.getProperties().isEmpty()) {
            sender.sendMessage(Message.error("easyarmorstands.error.clipboard-empty"));
            sender.sendMessage(Message.hint("easyarmorstands.hint.copy-property"));
            sender.sendMessage(Message.hint("easyarmorstands.hint.copy-all-properties", Message.command("/eas copy")));
            return;
        }

        PropertyCopier copier = new PropertyCopier();
        PropertyContainer properties = selection.properties(sender);
        copier.copyProperties(properties, clipboard.getProperties());
        properties.commit(Message.component("easyarmorstands.history.clipboard-pasted"));

        if (copier.getSuccessCount() > 0) {
            sender.sendMessage(Message.success("easyarmorstands.success.clipboard-pasted"));
            if (copier.getFailureCount() > 0) {
                sender.sendMessage(Message.warning("easyarmorstands.warning.clipboard-partial"));
            }
        } else if (copier.getFailureCount() > 0) {
            sender.sendMessage(Message.error("easyarmorstands.error.clipboard-failed"));
        }
    }
}
