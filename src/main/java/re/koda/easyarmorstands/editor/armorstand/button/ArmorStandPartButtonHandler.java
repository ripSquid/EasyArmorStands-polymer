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
import re.koda.easyarmorstands.api.ArmorStandPart;
import re.koda.easyarmorstands.api.editor.Session;
import re.koda.easyarmorstands.api.editor.button.ButtonHandler;
import re.koda.easyarmorstands.api.editor.button.ButtonHandlerContext;
import re.koda.easyarmorstands.api.editor.layer.Layer;
import re.koda.easyarmorstands.api.property.PropertyContainer;
import re.koda.easyarmorstands.editor.armorstand.layer.ArmorStandPartLayer;
import re.koda.easyarmorstands.editor.input.selection.SelectElementInput;
import re.koda.easyarmorstands.element.ArmorStandElement;
import re.koda.easyarmorstands.util.ArmorStandPartInfo;
import net.kyori.adventure.text.Component;

public class ArmorStandPartButtonHandler implements ButtonHandler {
    private final EasyArmorStandsCommon eas;
    private final Session session;
    private final ArmorStandElement element;
    private final PropertyContainer container;
    private final ArmorStandPart part;
    private final ArmorStandPartInfo partInfo;

    public ArmorStandPartButtonHandler(EasyArmorStandsCommon eas, Session session, ArmorStandElement element, PropertyContainer container, ArmorStandPart part) {
        this.eas = eas;
        this.session = session;
        this.element = element;
        this.container = container;
        this.part = part;
        this.partInfo = ArmorStandPartInfo.of(part);
    }

    @Override
    public void onUpdate(ButtonHandlerContext context) {
        context.addInput(new SelectElementInput(eas, session, element, this::createLayer));
    }

    @Override
    public Component getName() {
        return partInfo.getDisplayName();
    }

    private Layer createLayer() {
        return new ArmorStandPartLayer(session, container, part, element);
    }
}
