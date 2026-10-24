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
package re.koda.easyarmorstands.editor.tool.node;

import re.koda.easyarmorstands.api.editor.Session;
import re.koda.easyarmorstands.api.editor.node.tool.MoveToolNodeBuilder;
import re.koda.easyarmorstands.api.editor.node.tool.ToolNode;
import re.koda.easyarmorstands.api.editor.tool.MoveTool;
import re.koda.easyarmorstands.api.particle.ParticleColor;
import re.koda.easyarmorstands.message.Message;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.Nullable;

public class MoveToolNodeBuilderImpl implements MoveToolNodeBuilder {
    private final Session session;
    private @Nullable MoveTool tool;
    private Component name = Message.component("easyarmorstands.node.move");
    private ParticleColor color = ParticleColor.WHITE;
    private int priority;

    public MoveToolNodeBuilderImpl(Session session) {
        this.session = session;
    }

    @Override
    public MoveToolNodeBuilder setTool(MoveTool tool) {
        this.tool = tool;
        return this;
    }

    @Override
    public MoveToolNodeBuilder setName(Component name) {
        this.name = name;
        return this;
    }

    @Override
    public MoveToolNodeBuilder setColor(ParticleColor color) {
        this.color = color;
        return this;
    }

    @Override
    public MoveToolNodeBuilder setPriority(int priority) {
        this.priority = priority;
        return this;
    }

    @Override
    public ToolNode build() {
        if (tool == null) {
            throw new IllegalStateException("Tool not set");
        }
        return new MoveToolNode(session, tool, name, color, priority);
    }
}
