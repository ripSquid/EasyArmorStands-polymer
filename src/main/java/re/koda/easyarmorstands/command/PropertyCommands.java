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

import re.koda.easyarmorstands.EasyArmorStandsCommon;
import re.koda.easyarmorstands.api.Axis;
import re.koda.easyarmorstands.api.property.PropertyContainer;
import re.koda.easyarmorstands.api.property.type.BlockDisplayPropertyTypes;
import re.koda.easyarmorstands.command.parser.ArgumentParserProvider;
import re.koda.easyarmorstands.command.parser.BlockDataArgumentParser;
import re.koda.easyarmorstands.command.processor.ElementSelectionProcessor;
import re.koda.easyarmorstands.command.requirement.ElementSelectionRequirement;
import re.koda.easyarmorstands.command.sender.EasCommandSender;
import re.koda.easyarmorstands.command.sender.EasPlayer;
import re.koda.easyarmorstands.command.util.ElementSelection;
import re.koda.easyarmorstands.command.value.DisplayScaleAxisCommand;
import re.koda.easyarmorstands.command.value.PitchCommand;
import re.koda.easyarmorstands.command.value.PositionCommand;
import re.koda.easyarmorstands.command.value.PropertyCommand;
import re.koda.easyarmorstands.command.value.ValueCommand;
import re.koda.easyarmorstands.command.value.YawCommand;
import re.koda.easyarmorstands.message.Message;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.incendo.cloud.Command;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.permission.Permission;

public final class PropertyCommands {
    private PropertyCommands() {
    }

    public static void register(CommandManager<EasCommandSender> commandManager, ArgumentParserProvider parserProvider) {
        register(commandManager, new PositionCommand(parserProvider));
        register(commandManager, new YawCommand());
        register(commandManager, new PitchCommand());
        for (Axis axis : Axis.values()) {
            register(commandManager, new DisplayScaleAxisCommand(axis));
        }

        register(commandManager, new PropertyCommand<>("block", BlockDisplayPropertyTypes.BLOCK, BlockDataArgumentParser.blockDataParser()));
    }

    public static <T> void register(CommandManager<EasCommandSender> commandManager, ValueCommand<T> valueCommand) {
        Permission permission = valueCommand.getPermission();
        Command.Builder<EasCommandSender> builder = commandManager.commandBuilder("eas").apply(valueCommand);

        commandManager.command(builder
                .permission(permission)
                .commandDescription(valueCommand.getShowDescription())
                .apply(new ElementSelectionRequirement())
                .senderType(EasPlayer.class)
                .handler(context -> {
                    ElementSelection selection = context.get(ElementSelectionProcessor.elementSelectionKey());
                    PropertyContainer properties = selection.properties(context.sender());
                    if (!valueCommand.isSupported(properties)) {
                        valueCommand.sendNotSupported(context.sender());
                        return;
                    }
                    T value = valueCommand.getValue(properties);
                    context.sender().sendMessage(Component.text()
                            .append(Message.title(valueCommand.getDisplayName()))
                            .append(Component.space())
                            .append(valueCommand.formatValue(value))
                            .append(Component.space())
                            .append(Message.chatButton("easyarmorstands.button.edit")
                                    .hoverEvent(Message.hover("easyarmorstands.click-to-edit"))
                                    .clickEvent(ClickEvent.suggestCommand(valueCommand.formatCommand(value)))));
                }));

        commandManager.command(builder
                .permission(permission)
                .commandDescription(valueCommand.getSetterDescription())
                .apply(new ElementSelectionRequirement())
                .required("value", valueCommand.getParser())
                .senderType(EasPlayer.class)
                .handler(context -> {
                    ElementSelection selection = context.get(ElementSelectionProcessor.elementSelectionKey());
                    PropertyContainer properties = selection.properties(context.sender());
                    if (!valueCommand.isSupported(properties)) {
                        valueCommand.sendNotSupported(context.sender());
                        return;
                    }
                    T value = context.get("value");
                    if (valueCommand.setValue(properties, value)) {
                        valueCommand.sendSuccess(context.sender(), value);
                    } else {
                        valueCommand.sendFailure(context.sender(), value);
                    }
                    properties.commit();
                }));
    }
}
