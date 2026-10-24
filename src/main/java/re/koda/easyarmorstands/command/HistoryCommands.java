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

import re.koda.easyarmorstands.command.sender.EasPlayer;
import re.koda.easyarmorstands.history.History;
import re.koda.easyarmorstands.history.action.Action;
import re.koda.easyarmorstands.message.Message;
import re.koda.easyarmorstands.permission.Permissions;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.incendo.cloud.annotation.specifier.Range;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.CommandDescription;
import org.incendo.cloud.annotations.Default;
import org.incendo.cloud.annotations.Permission;

import java.util.Iterator;

@Command("eas")
public class HistoryCommands {
    @Command("history")
    @Permission(Permissions.HISTORY)
    @CommandDescription("easyarmorstands.command.description.history")
    public void history(EasPlayer sender) {
        History history = sender.history();
        if (history.getPast().isEmpty()) {
            sender.sendMessage(Message.warning("easyarmorstands.warning.history-empty"));
            return;
        }
        sender.sendMessage(Message.title("easyarmorstands.title.history"));
        for (Iterator<Action> it = history.getPast().descendingIterator(); it.hasNext(); ) {
            Action action = it.next();
            sender.sendMessage(Component.text()
                    .content("* ")
                    .color(NamedTextColor.GRAY)
                    .append(action.describe()));
        }
    }

    @Command("redo [count]")
    @Permission(Permissions.REDO)
    @CommandDescription("easyarmorstands.command.description.redo")
    public void redo(EasPlayer sender,
                     @Range(min = "1", max = "10") @Argument(value = "count") @Default("1") int count) {
        History history = sender.history();
        for (int i = 0; i < count; i++) {
            Action action = history.takeRedoAction();
            if (action != null) {
                if (!action.execute(sender)) {
                    sender.sendMessage(Message.error("easyarmorstands.error.cannot-redo", action.describe()));
                    break;
                }
                sender.sendMessage(Message.success("easyarmorstands.success.redone-change", action.describe()));
            } else {
                sender.sendMessage(Message.error("easyarmorstands.error.nothing-to-redo"));
                break;
            }
        }
    }

    @Command("undo [count]")
    @Permission(Permissions.UNDO)
    @CommandDescription("easyarmorstands.command.description.undo")
    public void undo(EasPlayer sender,
                     @Range(min = "1", max = "10") @Argument(value = "count") @Default("1") int count) {
        History history = sender.history();
        for (int i = 0; i < count; i++) {
            Action action = history.takeUndoAction();
            if (action != null) {
                if (!action.undo(sender)) {
                    sender.sendMessage(Message.error("easyarmorstands.error.cannot-undo", action.describe()));
                    break;
                }
                sender.sendMessage(Message.success("easyarmorstands.success.undone-change", action.describe()));
            } else {
                sender.sendMessage(Message.error("easyarmorstands.error.nothing-to-undo"));
                break;
            }
        }
    }
}
