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
import re.koda.easyarmorstands.api.editor.node.tool.ScaleToolNodeBuilder;
import re.koda.easyarmorstands.api.editor.node.tool.ToolNode;
import re.koda.easyarmorstands.api.editor.tool.ScaleTool;
import re.koda.easyarmorstands.api.particle.ParticleColor;
import re.koda.easyarmorstands.message.Message;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.jspecify.annotations.Nullable;

public class ScaleToolNodeBuilderImpl implements ScaleToolNodeBuilder {
    private final Session session;
    private @Nullable ScaleTool tool;
    private @Nullable Component name;
    private ParticleColor color = ParticleColor.AQUA;
    private int priority;

    public ScaleToolNodeBuilderImpl(Session session) {
        this.session = session;
    }

    @Override
    public ScaleToolNodeBuilder setTool(ScaleTool tool) {
        this.tool = tool;
        return this;
    }

    @Override
    public ScaleToolNodeBuilder setName(Component name) {
        this.name = name;
        return this;
    }

    @Override
    public ScaleToolNodeBuilder setColor(ParticleColor color) {
        this.color = color;
        return this;
    }

    @Override
    public ScaleToolNodeBuilder setPriority(int priority) {
        this.priority = priority;
        return this;
    }

    @Override
    public ToolNode build() {
        if (tool == null) {
            throw new IllegalStateException("Tool not set");
        }
        Component name = this.name;
        if (name == null) {
            name = Message.component("easyarmorstands.node.scale").color(TextColor.color(color));
        }
        return new ScaleToolNode(session, tool, name, color, priority);
    }
}
