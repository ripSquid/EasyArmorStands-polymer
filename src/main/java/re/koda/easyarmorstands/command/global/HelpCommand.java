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

import re.koda.easyarmorstands.command.sender.EasCommandSender;
import re.koda.easyarmorstands.permission.Permissions;
import org.incendo.cloud.annotation.specifier.Greedy;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.CommandDescription;
import org.incendo.cloud.annotations.Permission;
import org.incendo.cloud.annotations.processing.CommandContainer;
import org.incendo.cloud.minecraft.extras.MinecraftHelp;
import org.jspecify.annotations.Nullable;

@CommandContainer
public class HelpCommand {
    @Command("eas help [query]")
    @Permission(Permissions.HELP)
    @CommandDescription("easyarmorstands.command.description.help")
    public void help(
            EasCommandSender sender,
            MinecraftHelp<EasCommandSender> help,
            @Argument(value = "query", suggestions = "help_queries") @Greedy @Nullable String query) {
        help.queryCommands(query != null ? query : "", sender);
    }
}
