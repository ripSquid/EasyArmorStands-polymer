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
package re.koda.easyarmorstands.property.button;

import re.koda.easyarmorstands.EasyArmorStandsCommon;
import re.koda.easyarmorstands.api.menu.click.MenuClickContext;
import re.koda.easyarmorstands.api.property.Property;
import re.koda.easyarmorstands.clipboard.Clipboard;
import re.koda.easyarmorstands.dialog.DialogBuilder;
import re.koda.easyarmorstands.dialog.OptionalTextDialogEntry;
import re.koda.easyarmorstands.platform.entity.Player;
import net.kyori.adventure.text.Component;

import java.util.Optional;
import java.util.function.Function;

public class OptionalComponentHandler implements ButtonHandler {
    private final EasyArmorStandsCommon eas;
    private final Property<Optional<Component>> property;

    public OptionalComponentHandler(EasyArmorStandsCommon eas, Property<Optional<Component>> property) {
        this.eas = eas;
        this.property = property;
    }

    public static Function<Property<Optional<Component>>, OptionalComponentHandler> provider(EasyArmorStandsCommon eas) {
        return p -> new OptionalComponentHandler(eas, p);
    }

    @Override
    public void onClick(MenuClickContext context) {
        Player player = context.player();
        if (context.isShiftClick()) {
            Clipboard clipboard = eas.getClipboard(player);
            clipboard.handlePropertyShiftClick(property);
        } else if (context.isLeftClick()) {
            context.closeMenu();
            eas.platform().getScheduler().runTask(() -> {
                DialogBuilder builder = eas.createDialogBuilder(player);
                builder.setTitle(property.getType().getName());
                builder.addEntry(new OptionalTextDialogEntry("text", property));
                player.showDialog(builder.build());
            });
        }
    }
}
