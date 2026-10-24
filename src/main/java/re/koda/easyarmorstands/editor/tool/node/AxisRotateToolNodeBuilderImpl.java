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
import re.koda.easyarmorstands.api.editor.node.tool.AxisRotateToolNodeBuilder;
import re.koda.easyarmorstands.api.editor.node.tool.ToolNode;
import re.koda.easyarmorstands.api.editor.tool.AxisRotateTool;
import re.koda.easyarmorstands.api.particle.ParticleColor;
import re.koda.easyarmorstands.message.Message;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.jspecify.annotations.Nullable;

public class AxisRotateToolNodeBuilderImpl implements AxisRotateToolNodeBuilder {
    private final Session session;
    private @Nullable AxisRotateTool tool;
    private double radius = 1;
    private double length = 3;
    private @Nullable ParticleColor color;
    private @Nullable Component name;

    public AxisRotateToolNodeBuilderImpl(Session session) {
        this.session = session;
    }

    @Override
    public AxisRotateToolNodeBuilder setTool(AxisRotateTool tool) {
        this.tool = tool;
        return this;
    }

    @Override
    public AxisRotateToolNodeBuilder setLength(double length) {
        this.length = length;
        return this;
    }

    @Override
    public AxisRotateToolNodeBuilder setRadius(double radius) {
        this.radius = radius;
        return this;
    }

    @Override
    public AxisRotateToolNodeBuilder setName(Component name) {
        this.name = name;
        return this;
    }

    @Override
    public AxisRotateToolNodeBuilder setColor(ParticleColor color) {
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
                    "easyarmorstands.node.rotate-around-axis",
                    Component.text(axis.getName())
            ).color(textColor);
        }
        ParticleColor color = this.color;
        if (color == null) {
            color = axis.getColor();
        }
        return new AxisRotateToolNode(session, tool, radius, length, name, color);
    }
}
