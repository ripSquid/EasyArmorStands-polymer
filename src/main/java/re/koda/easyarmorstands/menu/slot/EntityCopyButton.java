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
import re.koda.easyarmorstands.api.EasyArmorStands;
import re.koda.easyarmorstands.api.element.ElementType;
import re.koda.easyarmorstands.api.menu.button.MenuButton;
import re.koda.easyarmorstands.api.menu.button.MenuButtonCategory;
import re.koda.easyarmorstands.api.menu.button.MenuIcon;
import re.koda.easyarmorstands.api.menu.click.MenuClickContext;
import re.koda.easyarmorstands.platform.entity.Entity;
import re.koda.easyarmorstands.platform.entity.Player;
import re.koda.easyarmorstands.platform.inventory.ItemStack;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.translation.GlobalTranslator;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public class EntityCopyButton implements MenuButton {
    private static final Key KEY = EasyArmorStands.key("copy");
    private final EasyArmorStandsCommon eas;
    private final Entity entity;
    private final ElementType type;
    private final ItemStack item;

    public EntityCopyButton(EasyArmorStandsCommon eas, Entity entity, ElementType type, ItemStack item) {
        this.eas = eas;
        this.entity = entity;
        this.type = type;
        this.item = item;
    }

    @Override
    public Key key() {
        return KEY;
    }

    @Override
    public MenuIcon icon() {
        return MenuIcon.of(item);
    }

    @Override
    public MenuButtonCategory category() {
        return MenuButtonCategory.FOOTER;
    }

    @Override
    public Component name() {
        return Component.translatable("easyarmorstands.menu.copy");
    }

    @Override
    public List<Component> description() {
        return List.of(Component.translatable("easyarmorstands.menu.copy.description"));
    }

    @Override
    public void onClick(MenuClickContext context) {
        Player player = context.player();
        ItemStack cursor = player.getItemOnCursor();
        if (cursor.isEmpty()) {
            eas.platform().getScheduler().runTask(() -> {
                ItemStack item = eas.createEntitySpawnEgg(entity);
                if (!item.isEmpty()) {
                    item = item.withItemName(GlobalTranslator.render(Component.translatable("easyarmorstands.copy.item.name",
                            NamedTextColor.GOLD,
                            type.getDisplayName()), player.locale()));
                    player.setItemOnCursor(item);
                }
            });
        } else {
            eas.platform().getScheduler().runTask(() -> player.setItemOnCursor(null));
        }
    }
}
