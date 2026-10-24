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
package re.koda.easyarmorstands.editor.node;

import re.koda.easyarmorstands.EasyArmorStandsCommon;
import re.koda.easyarmorstands.api.ArmorStandPart;
import re.koda.easyarmorstands.api.editor.Session;
import re.koda.easyarmorstands.api.editor.button.Button;
import re.koda.easyarmorstands.api.editor.button.PointButton;
import re.koda.easyarmorstands.api.editor.node.Node;
import re.koda.easyarmorstands.api.editor.node.NodeShowContext;
import re.koda.easyarmorstands.api.property.PropertyContainer;
import re.koda.easyarmorstands.editor.EntityPositionProvider;
import re.koda.easyarmorstands.editor.armorstand.ArmorStandOffsetProvider;
import re.koda.easyarmorstands.editor.armorstand.ArmorStandRotationProvider;
import re.koda.easyarmorstands.editor.armorstand.button.ArmorStandPartButton;
import re.koda.easyarmorstands.editor.armorstand.button.ArmorStandPartButtonHandler;
import re.koda.easyarmorstands.editor.armorstand.button.ArmorStandPositionButtonHandler;
import re.koda.easyarmorstands.element.ArmorStandElement;
import re.koda.easyarmorstands.message.Message;

public class ArmorStandNode implements Node {
    private final EasyArmorStandsCommon eas;
    private final Session session;
    private final ArmorStandElement element;
    private final PropertyContainer properties;

    public ArmorStandNode(EasyArmorStandsCommon eas, Session session, ArmorStandElement element) {
        this.eas = eas;
        this.session = session;
        this.element = element;
        this.properties = session.properties(element);
    }

    @Override
    public void onShow(NodeShowContext context) {
        context.addButton(createPositionButton(), new ArmorStandPositionButtonHandler(
                eas,
                session,
                Message.component("easyarmorstands.node.position"),
                properties,
                new ArmorStandOffsetProvider(element, properties),
                element));

        for (ArmorStandPart part : ArmorStandPart.values()) {
            context.addButton(createPartButton(part),
                    new ArmorStandPartButtonHandler(eas, session, element, properties, part));
        }
    }

    private Button createPositionButton() {
        PointButton button = new PointButton(session,
                new EntityPositionProvider(properties, new ArmorStandOffsetProvider(element, properties)),
                new ArmorStandRotationProvider(properties));
        button.setPriority(1);
        return button;
    }

    private Button createPartButton(ArmorStandPart part) {
        return new ArmorStandPartButton(session, properties, part, element);
    }
}
