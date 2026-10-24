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
import re.koda.easyarmorstands.api.editor.layer.Layer;
import re.koda.easyarmorstands.api.element.Element;
import re.koda.easyarmorstands.api.property.Property;
import re.koda.easyarmorstands.api.property.type.PropertyType;
import re.koda.easyarmorstands.command.sender.EasCommandSender;
import re.koda.easyarmorstands.command.sender.EasPlayer;
import re.koda.easyarmorstands.permission.Permissions;
import re.koda.easyarmorstands.platform.Platform;
import re.koda.easyarmorstands.session.SessionImpl;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.CommandDescription;
import org.incendo.cloud.annotations.Permission;
import org.incendo.cloud.annotations.processing.CommandContainer;

import java.util.Objects;

@CommandContainer
public class DebugCommand {
    @Command("eas debug")
    @Permission(Permissions.DEBUG)
    @CommandDescription("easyarmorstands.command.description.debug")
    public void debug(EasCommandSender sender, EasyArmorStandsCommon common, Platform platform) {
        String version = common.getVersion();
        sender.sendMessage(Component.text("EasyArmorStands v" + version, NamedTextColor.GOLD, TextDecoration.UNDERLINED));
        sender.sendMessage(debugLine(Component.text(platform.getName()), Component.text(platform.getVersion())));
        sender.sendMessage(debugLine(Component.text("Game"), Component.text(platform.getGameVersion())));

        if (sender instanceof EasPlayer) {
            SessionImpl session = ((EasPlayer) sender).session();
            Element element = null;
            if (session != null) {
                sender.sendMessage(Component.text("Current session:", NamedTextColor.GOLD));
                boolean first = true;
                for (Layer layer : session.getLayerStack()) {
                    sender.sendMessage(
                            Component.text("* ", first ? NamedTextColor.GREEN : NamedTextColor.GRAY)
                                    .append(Component.text(layer.getClass().getSimpleName())
                                            .hoverEvent(Component.text(layer.getClass().getName()))));
                    first = false;
                }
                element = session.getElement();
            }
            if (element != null) {
                sender.sendMessage(Component.text("Selected element: ", NamedTextColor.GOLD)
                        .append(Component.text(element.getClass().getSimpleName(), NamedTextColor.GREEN)
                                .hoverEvent(Component.text()
                                        .append(Component.text(element.getType().key().asString()))
                                        .appendNewline()
                                        .append(Component.text(element.getClass().getName()))
                                        .build())));
                TextComponent.Builder builder = Component.text();
                builder.color(NamedTextColor.GRAY);
                element.getProperties().forEach(property -> {
                    try {
                        appendPropertyDebug(builder, property);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to process property: " + property.getType().key(), e);
                    }
                });
                sender.sendMessage(builder);
            }
        }
    }

    private <T> void appendPropertyDebug(TextComponent.Builder builder, Property<T> property) {
        PropertyType<T> type = property.getType();
        T value = Objects.requireNonNull(property.getValue());

        TextComponent.Builder hover = Component.text();
        hover.append(Component.text(type.key().namespace() + ':', NamedTextColor.YELLOW)
                .append(Component.text(type.key().value(), NamedTextColor.GOLD)));
        hover.appendNewline();
        hover.append(debugLine(Component.text("Type"), Component.text(type.getClass().getName())));
        hover.appendNewline();
        hover.append(debugLine(Component.text("Value"), type.getValueComponent(value)));
        String permission = type.getPermission();
        if (permission != null) {
            hover.appendNewline();
            hover.append(debugLine(Component.text("Permission"), Component.text(permission)));
        }

        if (!builder.children().isEmpty()) {
            builder.append(Component.text(", "));
        }
        builder.append(type.getName().hoverEvent(hover.build()).clickEvent(ClickEvent.copyToClipboard(type.getValueString(value))));
    }

    private Component debugLine(Component key, Component value) {
        return Component.text()
                .append(key.applyFallbackStyle(NamedTextColor.GOLD))
                .append(Component.text(": ", NamedTextColor.GRAY))
                .append(value.applyFallbackStyle(NamedTextColor.GRAY))
                .build();
    }
}
