/*
 * EasyArmorStands-Polymer - a Polymer based Fabric port of EasyArmorStands
 * Copyright (C) 56738 and EasyArmorStands contributors (original work)
 * Copyright (C) 2026 ripsquid (modifications)
 *
 * This file is derived from gizmo (https://github.com/56738/gizmo) by 56738,
 * a GPL-3.0 licensed library used by EasyArmorStands, and was modified on 2026-10-22
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
package re.koda.easyarmorstands.gizmo.api;

import re.koda.easyarmorstands.gizmo.AbstractBoxGizmo;
import re.koda.easyarmorstands.gizmo.api.color.GizmoColor;
import org.joml.Quaterniondc;
import org.joml.Vector3d;
import org.joml.Vector3dc;

class LineBoxGizmo extends AbstractBoxGizmo {
    static final LineInfo[] LINE_INFO = new LineInfo[]{
            new LineInfo(0, 0, 0, GizmoAxis.X),
            new LineInfo(0, 0, 1, GizmoAxis.X),
            new LineInfo(0, 1, 0, GizmoAxis.X),
            new LineInfo(0, 1, 1, GizmoAxis.X),
            new LineInfo(0, 0, 0, GizmoAxis.Y),
            new LineInfo(0, 0, 1, GizmoAxis.Y),
            new LineInfo(1, 0, 0, GizmoAxis.Y),
            new LineInfo(1, 0, 1, GizmoAxis.Y),
            new LineInfo(0, 0, 0, GizmoAxis.Z),
            new LineInfo(1, 0, 0, GizmoAxis.Z),
            new LineInfo(0, 1, 0, GizmoAxis.Z),
            new LineInfo(1, 1, 0, GizmoAxis.Z)
    };

    private final LineGizmo[] lines;

    LineBoxGizmo(LineGizmo[] lines) {
        if (lines.length != LINE_INFO.length) {
            throw new IllegalArgumentException();
        }
        this.lines = lines;
        for (int i = 0; i < LINE_INFO.length; i++) {
            lines[i].setAxis(LINE_INFO[i].axis);
        }
    }

    @Override
    public void show() {
        updateLines();
        for (LineGizmo line : lines) {
            line.show();
        }
    }

    @Override
    public void update() {
        updateLines();
        for (LineGizmo line : lines) {
            line.update();
        }
    }

    @Override
    public void hide() {
        for (LineGizmo line : lines) {
            line.hide();
        }
    }

    private void updateLines() {
        if (!checkAndClearDirty()) {
            return;
        }

        Vector3dc position = getPosition();
        Vector3dc offset = getOffset();
        Quaterniondc rotation = getRotation();
        GizmoColor color = getColor();
        Vector3dc size = getSize();
        double width = getWidth();

        Vector3d currentOffset = new Vector3d();
        for (int i = 0; i < LINE_INFO.length; i++) {
            LineGizmo line = lines[i];
            LineInfo info = LINE_INFO[i];
            line.setPosition(position);
            line.setOffset(info.offset
                    .mul(size, currentOffset)
                    .fma(-width / 2, info.axis.direction())
                    .rotate(rotation)
                    .add(offset));
            line.setRotation(rotation);
            line.setColor(color);
            line.setWidth(width);
            line.setLength(info.axis.direction().dot(size) + width);
        }
    }

    static class LineInfo {
        private final Vector3dc offset;
        private final GizmoAxis axis;

        private LineInfo(double x, double y, double z, GizmoAxis axis) {
            this.offset = new Vector3d(x - 0.5, y - 0.5, z - 0.5);
            this.axis = axis;
        }
    }
}
