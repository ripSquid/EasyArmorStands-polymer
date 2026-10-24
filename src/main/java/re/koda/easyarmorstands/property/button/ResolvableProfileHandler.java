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
import re.koda.easyarmorstands.api.menu.button.MenuIcon;
import re.koda.easyarmorstands.api.menu.click.MenuClickContext;
import re.koda.easyarmorstands.api.property.Property;
import re.koda.easyarmorstands.message.Message;
import re.koda.easyarmorstands.platform.entity.Player;
import re.koda.easyarmorstands.platform.inventory.ItemStack;
import re.koda.easyarmorstands.platform.profile.Profile;
import re.koda.easyarmorstands.registry.ItemTypeKeys;

import java.util.function.Function;

public class ResolvableProfileHandler implements ButtonHandler {
    private final EasyArmorStandsCommon eas;
    private final Property<Profile> property;

    public ResolvableProfileHandler(EasyArmorStandsCommon eas, Property<Profile> property) {
        this.eas = eas;
        this.property = property;
    }

    public static Function<Property<Profile>, ResolvableProfileHandler> provider(EasyArmorStandsCommon eas) {
        return p -> new ResolvableProfileHandler(eas, p);
    }

    @Override
    public MenuIcon modifyIcon(MenuIcon icon) {
        return MenuIcon.of(icon.asItem().withProfile(property.getValue()));
    }

    @Override
    public void onClick(MenuClickContext context) {
        Player player = context.player();
        if (context.isShiftClick()) {
            eas.getClipboard(player).handlePropertyShiftClick(property);
        } else if (context.isLeftClick()) {
            ItemStack item = player.getItemOnCursor();
            Profile profile = item.getProfile();
            if (profile != null) {
                if (property.setValue(profile)) {
                    property.commit();
                } else {
                    player.sendMessage(Message.error("easyarmorstands.error.cannot-change"));
                }
            } else {
                eas.platform().getScheduler().runTask(() -> {
                    if (player.getItemOnCursor().isEmpty() && player.isCreativeMode()) {
                        player.setItemOnCursor(eas.platform().getItemType(ItemTypeKeys.PLAYER_HEAD).createItemStack()
                                .withProfile(property.getValue()));
                    }
                });
            }
        }
    }
}
