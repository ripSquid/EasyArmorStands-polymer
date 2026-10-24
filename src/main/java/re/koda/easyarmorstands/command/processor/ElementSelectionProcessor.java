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
package re.koda.easyarmorstands.command.processor;

import re.koda.easyarmorstands.EasyArmorStandsCommon;
import re.koda.easyarmorstands.api.element.Element;
import re.koda.easyarmorstands.command.sender.EasCommandSender;
import re.koda.easyarmorstands.command.util.ElementSelection;
import re.koda.easyarmorstands.group.Group;
import re.koda.easyarmorstands.group.GroupMember;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.execution.preprocessor.CommandPreprocessingContext;
import org.incendo.cloud.execution.preprocessor.CommandPreprocessor;
import org.incendo.cloud.key.CloudKey;

import java.util.Collections;
import java.util.stream.Collectors;

import static re.koda.easyarmorstands.command.processor.ElementProcessor.elementKey;
import static re.koda.easyarmorstands.command.processor.GroupProcessor.groupKey;
import static org.incendo.cloud.key.CloudKey.cloudKey;

public class ElementSelectionProcessor implements CommandPreprocessor<EasCommandSender> {
    private static final CloudKey<ElementSelection> KEY = cloudKey("selection", ElementSelection.class);

    private final EasyArmorStandsCommon eas;

    public ElementSelectionProcessor(EasyArmorStandsCommon eas) {
        this.eas = eas;
    }

    public static CloudKey<ElementSelection> elementSelectionKey() {
        return KEY;
    }

    @Override
    public void accept(@NonNull CommandPreprocessingContext<EasCommandSender> context) {
        CommandContext<EasCommandSender> commandContext = context.commandContext();

        Element element = commandContext.getOrDefault(elementKey(), null);
        if (element != null) {
            commandContext.set(KEY, new ElementSelection(eas, Collections.singleton(element)));
            return;
        }

        Group group = commandContext.getOrDefault(groupKey(), null);
        if (group != null) {
            commandContext.set(KEY, new ElementSelection(eas, group.getMembers().stream()
                    .map(GroupMember::getElement)
                    .collect(Collectors.toList())));
        }
    }
}
