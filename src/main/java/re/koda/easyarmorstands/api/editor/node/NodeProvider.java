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
package re.koda.easyarmorstands.api.editor.node;

import re.koda.easyarmorstands.api.Axis;
import re.koda.easyarmorstands.api.editor.node.tool.AxisMoveToolNodeBuilder;
import re.koda.easyarmorstands.api.editor.node.tool.AxisRotateToolNodeBuilder;
import re.koda.easyarmorstands.api.editor.node.tool.AxisScaleToolNodeBuilder;
import re.koda.easyarmorstands.api.editor.node.tool.MoveToolNodeBuilder;
import re.koda.easyarmorstands.api.editor.node.tool.ScaleToolNodeBuilder;
import re.koda.easyarmorstands.api.editor.tool.AxisMoveTool;
import re.koda.easyarmorstands.api.editor.tool.AxisRotateTool;
import re.koda.easyarmorstands.api.editor.tool.AxisScaleTool;
import re.koda.easyarmorstands.api.editor.tool.MoveTool;
import re.koda.easyarmorstands.api.editor.tool.ScaleTool;
import re.koda.easyarmorstands.api.editor.tool.ToolContext;
import re.koda.easyarmorstands.api.editor.tool.ToolProvider;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;

@ApiStatus.NonExtendable
public interface NodeProvider {
    @Contract(pure = true)
    MoveToolNodeBuilder move();

    @Contract(pure = true)
    AxisMoveToolNodeBuilder axisMove();

    @Contract(pure = true)
    ScaleToolNodeBuilder scale();

    @Contract(pure = true)
    AxisScaleToolNodeBuilder axisScale();

    @Contract(pure = true)
    AxisRotateToolNodeBuilder axisRotate();

    @Contract(pure = true)
    Node tools(ToolProvider toolProvider);

    @Contract(pure = true)
    default Node move(ToolProvider toolProvider, ToolContext toolContext) {
        List<Node> nodes = new ArrayList<>();
        for (Axis axis : Axis.values()) {
            AxisMoveTool axisMoveTool = toolProvider.move(toolContext, axis);
            if (axisMoveTool != null) {
                nodes.add(axisMove()
                        .setTool(axisMoveTool)
                        .build());
            }
        }
        MoveTool moveTool = toolProvider.move(toolContext);
        if (moveTool != null) {
            nodes.add(move()
                    .setTool(moveTool)
                    .setPriority(1)
                    .build());
        }
        return Node.composite(nodes);
    }

    @Contract(pure = true)
    default Node rotate(ToolProvider toolProvider, ToolContext toolContext) {
        List<Node> nodes = new ArrayList<>();
        for (Axis axis : Axis.values()) {
            AxisRotateTool axisRotateTool = toolProvider.rotate(toolContext, axis);
            if (axisRotateTool != null) {
                nodes.add(axisRotate()
                        .setTool(axisRotateTool)
                        .build());
            }
        }
        return Node.composite(nodes);
    }

    @Contract(pure = true)
    default Node scale(ToolProvider toolProvider, ToolContext toolContext) {
        List<Node> nodes = new ArrayList<>();
        for (Axis axis : Axis.values()) {
            AxisScaleTool axisScaleTool = toolProvider.scale(toolContext, axis);
            if (axisScaleTool != null) {
                nodes.add(axisScale()
                        .setTool(axisScaleTool)
                        .build());
            }
        }
        ScaleTool scaleTool = toolProvider.scale(toolContext);
        if (scaleTool != null) {
            nodes.add(scale()
                    .setTool(scaleTool)
                    .setPriority(1)
                    .build());
        }
        return Node.composite(nodes);
    }
}
