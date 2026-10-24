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
package re.koda.easyarmorstands.editor.tool.button;

import re.koda.easyarmorstands.api.editor.Session;
import re.koda.easyarmorstands.api.editor.button.ButtonHandler;
import re.koda.easyarmorstands.api.editor.button.ButtonHandlerContext;
import re.koda.easyarmorstands.api.editor.node.tool.ToolNode;
import re.koda.easyarmorstands.editor.input.SelectLayerInput;
import net.kyori.adventure.text.Component;

public class ToolMenuButton implements ButtonHandler {
    private final Session session;
    private final ToolNode node;
    private final Component name;

    public ToolMenuButton(Session session, ToolNode node, Component name) {
        this.session = session;
        this.node = node;
        this.name = name;
    }

    @Override
    public Component getName() {
        return name;
    }

    @Override
    public void onUpdate(ButtonHandlerContext context) {
        context.addInput(new SelectLayerInput(session, node::createLayer, context.cursor()));
    }
}
