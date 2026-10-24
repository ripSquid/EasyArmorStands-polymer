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
package re.koda.easyarmorstands.editor.layer;

import re.koda.easyarmorstands.api.Axis;
import re.koda.easyarmorstands.api.editor.Session;
import re.koda.easyarmorstands.api.editor.button.ButtonHandler;
import re.koda.easyarmorstands.api.editor.button.PointButton;
import re.koda.easyarmorstands.api.editor.layer.AbstractLayer;
import re.koda.easyarmorstands.api.editor.node.ButtonNode;
import re.koda.easyarmorstands.api.particle.ParticleColor;
import re.koda.easyarmorstands.api.util.RotationProvider;
import re.koda.easyarmorstands.editor.box.BoundingBoxEditor;
import re.koda.easyarmorstands.editor.box.BoxSidePositionProvider;
import re.koda.easyarmorstands.editor.button.BoxResizeButtonHandler;
import re.koda.easyarmorstands.editor.tool.BoxResizeTool;
import re.koda.easyarmorstands.message.Message;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class BoxResizeToolManager {
    private final Session session;
    private final AbstractLayer layer;
    private final BoundingBoxEditor editor;

    public BoxResizeToolManager(Session session, AbstractLayer layer, BoundingBoxEditor editor) {
        this.session = session;
        this.layer = layer;
        this.editor = editor;
        for (Axis axis : Axis.values()) {
            addAxisEnd(axis, false);
            addAxisEnd(axis, true);
        }
    }

    private void addAxisEnd(Axis axis, boolean end) {
        Component name;
        if (axis == Axis.Y) {
            name = Message.component("easyarmorstands.node.display.box.height");
        } else {
            name = Message.component("easyarmorstands.node.display.box.width");
        }
        BoxSidePositionProvider positionProvider = new BoxSidePositionProvider(editor, axis, end);
        RotationProvider rotationProvider = RotationProvider.identity();
        ParticleColor color = ParticleColor.AQUA;
        BoxResizeTool tool = new BoxResizeTool(editor, positionProvider, rotationProvider, axis, end);
        PointButton button = new PointButton(session, tool, tool);
        button.setPriority(2);
        button.setColor(color);
        ButtonHandler handler = new BoxResizeButtonHandler(session, name.color(NamedTextColor.AQUA), color, tool);
        layer.addNode(new ButtonNode(button, handler));
    }
}
