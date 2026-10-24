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
import re.koda.easyarmorstands.api.editor.button.Button;
import re.koda.easyarmorstands.api.editor.layer.Layer;
import re.koda.easyarmorstands.api.editor.tool.AxisRotateTool;
import re.koda.easyarmorstands.api.particle.ParticleColor;
import re.koda.easyarmorstands.editor.layer.AxisRotateToolLayer;
import re.koda.easyarmorstands.editor.tool.button.AxisRotateButton;
import net.kyori.adventure.text.Component;

public class AxisRotateToolNode extends AbstractToolNode<AxisRotateTool> {
    private final double radius;
    private final double length;

    public AxisRotateToolNode(Session session, AxisRotateTool tool, double radius, double length, Component name, ParticleColor color) {
        super(session, name, color, tool);
        this.radius = radius;
        this.length = length;
    }

    @Override
    public Button createButton() {
        return new AxisRotateButton(getSession(), getTool(), radius, getColor());
    }

    @Override
    public Layer createLayer() {
        return new AxisRotateToolLayer(getSession(), getTool().start(), radius, length, getName(), getColor(), getTool().getPosition(), getTool().getRotation(), getTool().getAxis());
    }
}
