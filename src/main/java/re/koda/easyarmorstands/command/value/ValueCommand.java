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
package re.koda.easyarmorstands.command.value;

import re.koda.easyarmorstands.api.property.PropertyContainer;
import re.koda.easyarmorstands.command.sender.EasCommandSender;
import re.koda.easyarmorstands.message.Message;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.incendo.cloud.Command;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.parser.ParserDescriptor;
import org.incendo.cloud.permission.Permission;
import org.jetbrains.annotations.NotNull;

public interface ValueCommand<T> extends Command.Builder.Applicable<EasCommandSender> {
    @NotNull Component getDisplayName();

    @NotNull Permission getPermission();

    @NotNull ParserDescriptor<EasCommandSender, T> getParser();

    @NotNull Description getShowDescription();

    @NotNull Description getSetterDescription();

    default boolean isSupported(@NotNull PropertyContainer properties) {
        return true;
    }

    @NotNull T getValue(@NotNull PropertyContainer properties);

    boolean setValue(@NotNull PropertyContainer properties, @NotNull T value);

    @NotNull Component formatValue(@NotNull T value);

    @NotNull String formatCommand(@NotNull T value);

    void sendSuccess(@NotNull Audience audience, @NotNull T value);

    default void sendFailure(@NotNull Audience audience, @NotNull T value) {
        audience.sendMessage(Message.error("easyarmorstands.error.cannot-change"));
    }

    default void sendNotSupported(@NotNull Audience audience) {
        audience.sendMessage(Message.error("easyarmorstands.error.property-not-supported"));
    }
}
