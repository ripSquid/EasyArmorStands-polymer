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
import re.koda.easyarmorstands.api.property.type.PropertyType;
import re.koda.easyarmorstands.command.processor.PropertyPermissionPredicate;
import re.koda.easyarmorstands.command.sender.EasCommandSender;
import re.koda.easyarmorstands.message.Message;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.incendo.cloud.Command;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.minecraft.extras.RichDescription;
import org.incendo.cloud.parser.ParserDescriptor;
import org.incendo.cloud.permission.Permission;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public class PropertyCommand<T> implements ValueCommand<T> {
    private final String name;
    private final PropertyType<T> type;
    private final ParserDescriptor<EasCommandSender, T> parser;

    public PropertyCommand(String name, PropertyType<T> type, ParserDescriptor<EasCommandSender, T> parser) {
        this.name = name;
        this.type = type;
        this.parser = parser;
    }

    public String getName() {
        return name;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return type.getName();
    }

    @Override
    public @NotNull Permission getPermission() {
        return PropertyPermissionPredicate.createPermission(type);
    }

    @Override
    public @NotNull ParserDescriptor<EasCommandSender, T> getParser() {
        return parser;
    }

    @Override
    public @NotNull Description getShowDescription() {
        return RichDescription.of(Message.component("easyarmorstands.command.description.show-property", type.getName()));
    }

    @Override
    public @NotNull Description getSetterDescription() {
        return RichDescription.of(Message.component("easyarmorstands.command.description.set-property", type.getName()));
    }

    @Override
    public boolean isSupported(@NotNull PropertyContainer properties) {
        return properties.getOrNull(type) != null;
    }

    @Override
    public @NotNull T getValue(@NotNull PropertyContainer properties) {
        return properties.get(type).getValue();
    }

    @Override
    public boolean setValue(@NotNull PropertyContainer properties, @NotNull T value) {
        return properties.get(type).setValue(value);
    }

    @Override
    public @NotNull Component formatValue(@NotNull T value) {
        return type.getValueComponent(value);
    }

    @Override
    public @NotNull String formatCommand(@NonNull T value) {
        return "/eas " + name + " " + type.getValueString(value);
    }

    @Override
    public void sendSuccess(@NotNull Audience audience, @NonNull T value) {
        audience.sendMessage(Message.success("easyarmorstands.success.changed-property",
                type.getName(),
                type.getValueComponent(value)));
    }

    @Override
    public Command.@NotNull Builder<EasCommandSender> applyToCommandBuilder(Command.@NotNull Builder<EasCommandSender> builder) {
        return builder.literal(name);
    }
}
