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

import re.koda.easyarmorstands.EasyArmorStandsCommon;
import re.koda.easyarmorstands.config.EasConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.HashMap;
import java.util.Map;

public class MessageManager {
    private final MiniMessage miniMessage = EasyArmorStandsCommon.miniMessage();
    private final Map<MessageStyle, String> styleTemplates = new HashMap<>();

    public MessageManager() {
        Message.messageManager = this;
    }

    public void load(EasConfig config) {
        styleTemplates.putAll(config.message.format);
    }

    public Component format(MessageStyle style, Component message) {
        return format(style, message, TagResolver.empty());
    }

    public Component format(MessageStyle style, Component message, TagResolver resolver) {
        return miniMessage.deserialize(
                styleTemplates.get(style),
                TagResolver.builder()
                        .tag("message", Tag.selfClosingInserting(message))
                        .resolver(resolver)
                        .build());
    }
}
