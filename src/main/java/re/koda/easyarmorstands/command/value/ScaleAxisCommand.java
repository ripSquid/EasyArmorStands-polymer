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

import re.koda.easyarmorstands.api.Axis;
import re.koda.easyarmorstands.api.property.Property;
import re.koda.easyarmorstands.api.property.PropertyContainer;
import re.koda.easyarmorstands.api.property.type.PropertyType;
import re.koda.easyarmorstands.command.processor.PropertyPermissionPredicate;
import re.koda.easyarmorstands.command.sender.EasCommandSender;
import re.koda.easyarmorstands.message.Message;
import re.koda.easyarmorstands.util.Util;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.minecraft.extras.RichDescription;
import org.incendo.cloud.parser.ParserDescriptor;
import org.incendo.cloud.parser.standard.FloatParser;
import org.incendo.cloud.permission.Permission;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public abstract class ScaleAxisCommand implements ValueCommand<Float> {
    private final PropertyType<Vector3fc> type;
    private final Axis axis;

    public ScaleAxisCommand(PropertyType<Vector3fc> type, Axis axis) {
        this.type = type;
        this.axis = axis;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return type.getName()
                .append(Component.space())
                .append(Component.text(axis.getName(), TextColor.color(axis.getColor())));
    }

    @Override
    public @NotNull Permission getPermission() {
        return PropertyPermissionPredicate.createPermission(type);
    }

    @Override
    public @NotNull ParserDescriptor<EasCommandSender, Float> getParser() {
        return FloatParser.floatParser(0);
    }

    @Override
    public @NotNull Description getShowDescription() {
        return RichDescription.translatable("easyarmorstands.command.description.scale.show");
    }

    @Override
    public @NotNull Description getSetterDescription() {
        return RichDescription.translatable("easyarmorstands.command.description.scale.set");
    }

    @Override
    public boolean isSupported(@NotNull PropertyContainer properties) {
        return properties.getOrNull(type) != null;
    }

    @Override
    public @NotNull Float getValue(@NotNull PropertyContainer properties) {
        return axis.getValue(properties.get(type).getValue());
    }

    @Override
    public boolean setValue(@NotNull PropertyContainer properties, @NotNull Float value) {
        Property<Vector3fc> property = properties.get(type);
        Vector3f scale = new Vector3f(property.getValue());
        axis.setValue(scale, value);
        return property.setValue(scale);
    }

    @Override
    public @NotNull Component formatValue(@NotNull Float value) {
        return Util.formatScale(value);
    }

    @Override
    public void sendSuccess(@NotNull Audience audience, @NotNull Float value) {
        audience.sendMessage(Message.success("easyarmorstands.success.changed-scale.axis",
                Component.text(axis.getName()), Util.formatScale(value)));
    }

    @Override
    public void sendNotSupported(@NotNull Audience audience) {
        audience.sendMessage(Message.error("easyarmorstands.error.scale-unsupported"));
    }
}
