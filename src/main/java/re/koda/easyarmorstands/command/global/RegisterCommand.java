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
package re.koda.easyarmorstands.command.global;

import re.koda.easyarmorstands.EasyArmorStandsCommon;
import re.koda.easyarmorstands.api.element.EntityElementProvider;
import re.koda.easyarmorstands.command.sender.EasCommandSender;
import re.koda.easyarmorstands.command.util.MultipleEntitySelector;
import re.koda.easyarmorstands.message.Message;
import re.koda.easyarmorstands.permission.Permissions;
import re.koda.easyarmorstands.platform.entity.Entity;
import net.kyori.adventure.text.Component;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.CommandDescription;
import org.incendo.cloud.annotations.Permission;
import org.incendo.cloud.annotations.processing.CommandContainer;

@CommandContainer
public class RegisterCommand {
    @Command("eas register <entity>")
    @Permission(Permissions.REGISTER)
    @CommandDescription("easyarmorstands.command.description.register")
    public void register(EasyArmorStandsCommon eas, EasCommandSender sender, @Argument("entity") MultipleEntitySelector selector) {
        int count = 0;
        for (Entity entity : selector.values()) {
            EntityElementProvider provider = eas.entityElementProviderRegistry().registerEntity(entity);
            if (provider != null) {
                count++;
            }
        }
        if (count > 1) {
            sender.sendMessage(Message.success("easyarmorstands.success.entity-registered.multiple", Component.text(count)));
        } else if (count == 1) {
            sender.sendMessage(Message.success("easyarmorstands.success.entity-registered"));
        } else {
            sender.sendMessage(Message.error("easyarmorstands.error.entity-not-found"));
        }
    }

    @Command("eas unregister <entity>")
    @Permission(Permissions.REGISTER)
    @CommandDescription("easyarmorstands.command.description.unregister")
    public void unregister(EasyArmorStandsCommon eas, EasCommandSender sender, @Argument("entity") MultipleEntitySelector selector) {
        int count = 0;
        for (Entity entity : selector.values()) {
            if (eas.getEntityElementProvider(entity) != null) {
                eas.setEntityElementProvider(entity, null);
                count++;
            }
        }
        if (count > 1) {
            sender.sendMessage(Message.success("easyarmorstands.success.entity-unregistered.multiple", Component.text(count)));
        } else if (count == 1) {
            sender.sendMessage(Message.success("easyarmorstands.success.entity-unregistered"));
        } else {
            sender.sendMessage(Message.error("easyarmorstands.error.entity-not-found"));
        }
    }
}
