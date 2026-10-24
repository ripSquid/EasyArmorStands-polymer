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
package re.koda.easyarmorstands.editor.display.layer;

import re.koda.easyarmorstands.api.editor.Session;
import re.koda.easyarmorstands.api.editor.context.UpdateContext;
import re.koda.easyarmorstands.api.property.PropertyContainer;
import re.koda.easyarmorstands.editor.display.DisplayBoxEditor;
import re.koda.easyarmorstands.editor.input.ReturnInput;
import re.koda.easyarmorstands.editor.layer.BoxResizeToolManager;
import re.koda.easyarmorstands.editor.tool.BoxToolProvider;
import re.koda.easyarmorstands.message.Message;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

public class DisplayBoxLayer extends DisplayLayer {
    private final Session session;
    private final Component name;

    public DisplayBoxLayer(Session session, PropertyContainer properties) {
        super(session, properties);
        this.session = session;
        this.name = Message.component("easyarmorstands.node.display.box").color(NamedTextColor.GOLD);
        setShowBoundingBoxIfInactive(true); // bounding box should remain visible while a tool layer is active

        DisplayBoxEditor boxEditor = new DisplayBoxEditor(properties);
        addNode(session.nodeProvider().tools(new BoxToolProvider(boxEditor)));
        new BoxResizeToolManager(session, this, boxEditor);
    }

    @Override
    public void onUpdate(@NotNull UpdateContext context) {
        super.onUpdate(context);
        context.setActionBar(name);
        context.addInput(new ReturnInput(session));
    }
}
