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

import re.koda.easyarmorstands.api.property.Property;
import re.koda.easyarmorstands.api.property.PropertyContainer;
import re.koda.easyarmorstands.api.property.type.EntityPropertyTypes;
import re.koda.easyarmorstands.command.processor.PropertyPermissionPredicate;
import re.koda.easyarmorstands.command.sender.EasCommandSender;
import re.koda.easyarmorstands.message.Message;
import re.koda.easyarmorstands.platform.util.Location;
import re.koda.easyarmorstands.util.Util;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.Command;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.minecraft.extras.RichDescription;
import org.incendo.cloud.parser.ParserDescriptor;
import org.incendo.cloud.parser.standard.FloatParser;
import org.incendo.cloud.permission.Permission;
import org.jetbrains.annotations.NotNull;

public class YawCommand implements ValueCommand<Float> {
    @Override
    public Command.@NonNull Builder<EasCommandSender> applyToCommandBuilder(Command.@NonNull Builder<EasCommandSender> builder) {
        return builder.literal("yaw");
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Message.component("easyarmorstands.command.value.yaw");
    }

    @Override
    public @NotNull Permission getPermission() {
        return PropertyPermissionPredicate.createPermission(EntityPropertyTypes.LOCATION);
    }

    @Override
    public @NotNull ParserDescriptor<EasCommandSender, Float> getParser() {
        return FloatParser.floatParser();
    }

    @Override
    public @NotNull Description getShowDescription() {
        return RichDescription.translatable("easyarmorstands.command.description.yaw.show");
    }

    @Override
    public @NotNull Description getSetterDescription() {
        return RichDescription.translatable("easyarmorstands.command.description.yaw.set");
    }

    @Override
    public @NotNull Float getValue(@NotNull PropertyContainer properties) {
        return properties.get(EntityPropertyTypes.LOCATION).getValue().yaw();
    }

    @Override
    public boolean setValue(@NotNull PropertyContainer properties, @NotNull Float value) {
        Property<Location> property = properties.get(EntityPropertyTypes.LOCATION);
        return property.setValue(property.getValue().withYaw(value));
    }

    @Override
    public @NotNull Component formatValue(@NotNull Float value) {
        return Util.formatDegrees(value);
    }

    @Override
    public @NotNull String formatCommand(@NotNull Float value) {
        return "/eas yaw " + value;
    }

    @Override
    public void sendSuccess(@NotNull Audience audience, @NotNull Float value) {
        audience.sendMessage(Message.success("easyarmorstands.success.changed-yaw", Util.formatDegrees(value)));
    }
}
