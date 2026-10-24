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
package re.koda.easyarmorstands.message;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEventSource;
import net.kyori.adventure.text.format.TextDecoration;

public class Message {
    static MessageManager messageManager;

    private Message() {
    }

    public static Component format(MessageStyle style, Component component) {
        return messageManager.format(style, component);
    }

    public static Component format(MessageStyle style, String key) {
        return format(style, component(key));
    }

    public static Component format(MessageStyle style, String key, ComponentLike... args) {
        return format(style, component(key, args));
    }

    public static Component title(Component component) {
        return format(MessageStyle.TITLE, component);
    }

    public static Component title(String key) {
        return format(MessageStyle.TITLE, key);
    }

    public static Component title(String key, ComponentLike... args) {
        return format(MessageStyle.TITLE, key, args);
    }

    public static Component success(String key) {
        return format(MessageStyle.SUCCESS, key);
    }

    public static Component success(String key, ComponentLike... args) {
        return format(MessageStyle.SUCCESS, key, args);
    }

    public static Component warning(String key) {
        return format(MessageStyle.WARNING, key);
    }

    public static Component warning(String key, ComponentLike... args) {
        return format(MessageStyle.WARNING, key, args);
    }

    public static Component error(String key) {
        return format(MessageStyle.ERROR, key);
    }

    public static Component error(String key, ComponentLike... args) {
        return format(MessageStyle.ERROR, key, args);
    }

    public static Component hint(String key) {
        return format(MessageStyle.HINT, key);
    }

    public static Component hint(String key, ComponentLike... args) {
        return format(MessageStyle.HINT, key, args);
    }

    public static Component chatButton(String key) {
        return format(MessageStyle.CHAT_BUTTON, key);
    }

    public static Component chatButton(String key, ComponentLike... args) {
        return format(MessageStyle.CHAT_BUTTON, key, args);
    }

    public static HoverEventSource<Component> hover(String key) {
        return format(MessageStyle.HOVER, key);
    }

    public static HoverEventSource<Component> hover(String key, ComponentLike... args) {
        return format(MessageStyle.HOVER, key, args);
    }

    public static Component buttonName(Component component) {
        return format(MessageStyle.BUTTON_NAME, component);
    }

    public static Component buttonName(String key) {
        return format(MessageStyle.BUTTON_NAME, key);
    }

    public static Component buttonName(String key, ComponentLike... args) {
        return format(MessageStyle.BUTTON_NAME, key, args);
    }

    public static Component buttonDescription(Component component) {
        return format(MessageStyle.BUTTON_DESCRIPTION, component);
    }

    public static Component buttonDescription(String key) {
        return format(MessageStyle.BUTTON_DESCRIPTION, key);
    }

    public static Component buttonDescription(String key, ComponentLike... args) {
        return format(MessageStyle.BUTTON_DESCRIPTION, key, args);
    }

    public static Component component(String key) {
        return Component.translatable(key);
    }

    public static Component component(String key, ComponentLike... args) {
        return Component.translatable(key, args);
    }

    public static Component command(String command) {
        return Component.text()
                .content(command)
                .decorate(TextDecoration.UNDERLINED)
                .clickEvent(ClickEvent.runCommand(command))
                .hoverEvent(hover("easyarmorstands.click-to-run", Component.text(command)))
                .build();
    }
}
