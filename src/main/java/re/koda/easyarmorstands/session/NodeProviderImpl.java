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
package re.koda.easyarmorstands.session;

import re.koda.easyarmorstands.api.editor.Session;
import re.koda.easyarmorstands.api.editor.node.Node;
import re.koda.easyarmorstands.api.editor.node.NodeProvider;
import re.koda.easyarmorstands.api.editor.node.tool.AxisMoveToolNodeBuilder;
import re.koda.easyarmorstands.api.editor.node.tool.AxisRotateToolNodeBuilder;
import re.koda.easyarmorstands.api.editor.node.tool.AxisScaleToolNodeBuilder;
import re.koda.easyarmorstands.api.editor.node.tool.MoveToolNodeBuilder;
import re.koda.easyarmorstands.api.editor.node.tool.ScaleToolNodeBuilder;
import re.koda.easyarmorstands.api.editor.tool.ToolProvider;
import re.koda.easyarmorstands.editor.node.SwitcherNode;
import re.koda.easyarmorstands.editor.tool.mode.ToolMode;
import re.koda.easyarmorstands.editor.tool.node.AxisMoveToolNodeBuilderImpl;
import re.koda.easyarmorstands.editor.tool.node.AxisRotateToolNodeBuilderImpl;
import re.koda.easyarmorstands.editor.tool.node.AxisScaleToolNodeBuilderImpl;
import re.koda.easyarmorstands.editor.tool.node.MoveToolNodeBuilderImpl;
import re.koda.easyarmorstands.editor.tool.node.ScaleToolNodeBuilderImpl;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

class NodeProviderImpl implements NodeProvider {
    private final Session session;

    NodeProviderImpl(Session session) {
        this.session = session;
    }

    @Override
    public @NotNull AxisMoveToolNodeBuilder axisMove() {
        return new AxisMoveToolNodeBuilderImpl(session);
    }

    @Override
    public @NotNull AxisScaleToolNodeBuilder axisScale() {
        return new AxisScaleToolNodeBuilderImpl(session);
    }

    @Override
    public @NotNull AxisRotateToolNodeBuilder axisRotate() {
        return new AxisRotateToolNodeBuilderImpl(session);
    }

    @Override
    public @NotNull MoveToolNodeBuilder move() {
        return new MoveToolNodeBuilderImpl(session);
    }

    @Override
    public @NotNull ScaleToolNodeBuilder scale() {
        return new ScaleToolNodeBuilderImpl(session);
    }

    @Override
    public @NotNull Node tools(@NotNull ToolProvider toolProvider) {
        List<SwitcherNode.Entry> entries = new ArrayList<>();
        for (ToolMode mode : List.of(ToolMode.LOCAL, ToolMode.GLOBAL, ToolMode.SCALE)) {
            Node node = mode.createNode(this, toolProvider);
            if (node != Node.empty()) {
                entries.add(new SwitcherNode.Entry(mode.name(), node));
            }
        }
        if (entries.size() == 1) {
            return entries.getFirst().node();
        }
        return new SwitcherNode(entries);
    }
}
