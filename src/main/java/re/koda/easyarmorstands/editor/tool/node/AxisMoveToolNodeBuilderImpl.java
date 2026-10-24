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

import re.koda.easyarmorstands.api.Axis;
import re.koda.easyarmorstands.api.editor.Session;
import re.koda.easyarmorstands.api.editor.node.tool.AxisMoveToolNodeBuilder;
import re.koda.easyarmorstands.api.editor.node.tool.ToolNode;
import re.koda.easyarmorstands.api.editor.tool.AxisMoveTool;
import re.koda.easyarmorstands.api.particle.ParticleColor;
import re.koda.easyarmorstands.message.Message;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.jspecify.annotations.Nullable;

public class AxisMoveToolNodeBuilderImpl implements AxisMoveToolNodeBuilder {
    private final Session session;
    private @Nullable AxisMoveTool tool;
    private double length = 3;
    private @Nullable Component name;
    private @Nullable ParticleColor color;

    public AxisMoveToolNodeBuilderImpl(Session session) {
        this.session = session;
    }

    @Override
    public AxisMoveToolNodeBuilder setTool(AxisMoveTool tool) {
        this.tool = tool;
        return this;
    }

    @Override
    public AxisMoveToolNodeBuilder setLength(double length) {
        this.length = length;
        return this;
    }

    @Override
    public AxisMoveToolNodeBuilder setName(Component name) {
        this.name = name;
        return this;
    }

    @Override
    public AxisMoveToolNodeBuilder setColor(ParticleColor color) {
        this.color = color;
        return this;
    }

    @Override
    public ToolNode build() {
        if (tool == null) {
            throw new IllegalStateException("Tool not set");
        }
        Axis axis = tool.getAxis();
        Component name = this.name;
        if (name == null) {
            TextColor textColor = TextColor.color(axis.getColor());
            name = Message.component(
                    "easyarmorstands.node.move-along-axis",
                    Component.text(axis.getName())
            ).color(textColor);
        }
        ParticleColor color = this.color;
        if (color == null) {
            color = axis.getColor();
        }
        return new AxisMoveToolNode(session, tool, length, name, color);
    }
}
