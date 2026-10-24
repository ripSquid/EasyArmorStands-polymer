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
package re.koda.easyarmorstands.editor.armorstand.button;

import re.koda.easyarmorstands.EasyArmorStandsCommon;
import re.koda.easyarmorstands.api.editor.Session;
import re.koda.easyarmorstands.api.editor.button.ButtonHandler;
import re.koda.easyarmorstands.api.editor.button.ButtonHandlerContext;
import re.koda.easyarmorstands.api.editor.layer.Layer;
import re.koda.easyarmorstands.api.property.PropertyContainer;
import re.koda.easyarmorstands.editor.OffsetProvider;
import re.koda.easyarmorstands.editor.armorstand.layer.ArmorStandPositionLayer;
import re.koda.easyarmorstands.editor.input.selection.SelectElementInput;
import re.koda.easyarmorstands.element.ArmorStandElement;
import net.kyori.adventure.text.Component;

public class ArmorStandPositionButtonHandler implements ButtonHandler {
    private final EasyArmorStandsCommon eas;
    private final Session session;
    private final Component name;
    private final PropertyContainer container;
    private final OffsetProvider offsetProvider;
    private final ArmorStandElement element;

    public ArmorStandPositionButtonHandler(EasyArmorStandsCommon eas, Session session, Component name, PropertyContainer container, OffsetProvider offsetProvider, ArmorStandElement element) {
        this.eas = eas;
        this.session = session;
        this.name = name;
        this.container = container;
        this.offsetProvider = offsetProvider;
        this.element = element;
    }

    @Override
    public void onUpdate(ButtonHandlerContext context) {
        context.addInput(new SelectElementInput(eas, session, element, this::createLayer));
    }

    @Override
    public Component getName() {
        return name;
    }

    private Layer createLayer() {
        return new ArmorStandPositionLayer(session, container, offsetProvider, element);
    }
}
