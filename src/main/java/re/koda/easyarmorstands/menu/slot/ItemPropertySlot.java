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
package re.koda.easyarmorstands.menu.slot;

import re.koda.easyarmorstands.EasyArmorStandsCommon;
import re.koda.easyarmorstands.api.menu.button.MenuIcon;
import re.koda.easyarmorstands.api.property.Property;
import re.koda.easyarmorstands.menu.click.MenuClick;
import re.koda.easyarmorstands.platform.inventory.ItemStack;
import re.koda.easyarmorstands.registry.ItemTypeKeys;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

public class ItemPropertySlot implements MenuSlot {
    private final EasyArmorStandsCommon eas;
    private final Property<ItemStack> property;

    public ItemPropertySlot(EasyArmorStandsCommon eas, Property<ItemStack> property) {
        this.eas = eas;
        this.property = property;
    }

    public Property<ItemStack> getProperty() {
        return property;
    }

    @Override
    public ItemStack getItem(Locale locale) {
        ItemStack item = property.getValue();
        if (item.isEmpty()) {
            return MenuButtonSlot.createItem(
                    MenuIcon.of(eas.platform().getItemType(ItemTypeKeys.GLASS_PANE)),
                    property.getType().getName(),
                    Component.empty(),
                    List.of(),
                    locale);
        } else {
            return item.withCustomName(MenuButtonSlot.formatCustomName(property.getType().getName(), item.effectiveName(), locale));
        }
    }

    @Override
    public void onClick(@NotNull MenuClick click) {
        if (click.isShiftClick()) {
            eas.getClipboard(click.player())
                    .handlePropertyShiftClick(property);
            return;
        }

        if (!property.canChange(click.player())) {
            return;
        }

        if (!click.isLeftClick()) {
            return;
        }

        click.queueTask(() -> {
            // Event is still cancelled, swap the items ourselves to prevent duplication
            ItemStack itemInCursor = click.cursor();
            ItemStack itemInProperty = property.getValue();
            if (property.setValue(itemInCursor)) {
                click.player().setItemOnCursor(itemInProperty);
                property.commit();
                click.menu().updateItem(click.index());
            }
        });
    }
}
