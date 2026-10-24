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
import re.koda.easyarmorstands.api.property.type.DisplayPropertyTypes;
import re.koda.easyarmorstands.command.sender.EasCommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.Command;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class DisplayScaleAxisCommand extends ScaleAxisCommand {
    private final String axisName;

    public DisplayScaleAxisCommand(Axis axis) {
        super(DisplayPropertyTypes.SCALE, axis);
        this.axisName = axis.getName().toLowerCase(Locale.ROOT);
    }

    @Override
    public @NotNull String formatCommand(@NotNull Float value) {
        return "/eas scale " + axisName + " " + value;
    }

    @Override
    public Command.@NonNull Builder<EasCommandSender> applyToCommandBuilder(Command.@NonNull Builder<EasCommandSender> builder) {
        return builder.literal("scale").literal(axisName);
    }
}
