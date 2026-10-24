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
package re.koda.easyarmorstands.command.requirement;

import re.koda.easyarmorstands.command.processor.GroupProcessor;
import re.koda.easyarmorstands.command.sender.EasCommandSender;
import re.koda.easyarmorstands.message.Message;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.context.CommandContext;

import java.util.Collections;
import java.util.List;

import static re.koda.easyarmorstands.command.processor.ElementProcessor.elementKey;

public class ElementRequirement implements CommandRequirement {
    @Override
    public boolean evaluateRequirement(@NonNull CommandContext<EasCommandSender> commandContext) {
        return commandContext.contains(elementKey());
    }

    @Override
    public void handle(CommandContext<EasCommandSender> context) {
        EasCommandSender sender = context.sender();
        if (context.contains(GroupProcessor.groupKey())) {
            sender.sendMessage(Message.error("easyarmorstands.error.group-selected"));
        } else {
            sender.sendMessage(Message.error("easyarmorstands.error.nothing-selected"));
        }
    }

    @Override
    public @NonNull List<@NonNull CommandRequirement> parents() {
        return Collections.singletonList(new SessionRequirement());
    }
}
