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
package re.koda.easyarmorstands.menu.button;

import re.koda.easyarmorstands.EasyArmorStandsCommon;
import re.koda.easyarmorstands.api.editor.Session;
import re.koda.easyarmorstands.api.editor.layer.ElementSelectionLayer;
import re.koda.easyarmorstands.api.element.Element;
import re.koda.easyarmorstands.api.element.ElementSpawnRequest;
import re.koda.easyarmorstands.api.element.ElementType;
import re.koda.easyarmorstands.api.element.SelectableElement;
import re.koda.easyarmorstands.api.menu.button.MenuButton;
import re.koda.easyarmorstands.api.menu.button.MenuIcon;
import re.koda.easyarmorstands.api.menu.click.MenuClickContext;
import re.koda.easyarmorstands.platform.entity.Player;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;

import java.util.List;

public class SpawnButton implements MenuButton {
    private final EasyArmorStandsCommon eas;
    private final Player player;
    private final ElementType type;
    private final MenuIcon icon;

    public SpawnButton(EasyArmorStandsCommon eas, Player player, ElementType type, MenuIcon icon) {
        this.eas = eas;
        this.player = player;
        this.type = type;
        this.icon = icon;
    }

    @Override
    public Key key() {
        return type.key();
    }

    @Override
    public MenuIcon icon() {
        return icon;
    }

    @Override
    public Component name() {
        return type.getDisplayName();
    }

    @Override
    public List<Component> description() {
        return List.of();
    }

    @Override
    public void onClick(MenuClickContext context) {
        if (context.isLeftClick()) {
            ElementSpawnRequest spawnRequest = eas.elementSpawnRequest(type);
            spawnRequest.setPlayer(player);
            Element element = spawnRequest.spawn();

            Session session = eas.sessionManager().getSession(player);
            if (session != null) {
                ElementSelectionLayer selectionLayer = session.findLayer(ElementSelectionLayer.class);
                if (selectionLayer != null) {
                    if (element instanceof SelectableElement) {
                        selectionLayer.selectElement((SelectableElement) element);
                    }
                }
            }

            context.closeMenu();
        }
    }
}
