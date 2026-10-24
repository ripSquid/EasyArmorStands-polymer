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

import re.koda.easyarmorstands.gizmo.AbstractCircleGizmo;
import org.joml.Math;
import org.joml.Quaterniond;
import org.joml.Vector3d;

import java.util.List;

class LineCircleGizmo extends AbstractCircleGizmo implements CircleGizmo {
    private final List<LineGizmo> pieces;

    LineCircleGizmo(List<LineGizmo> pieces) {
        this.pieces = pieces;
    }

    @Override
    public void show() {
        updatePieces();
        for (LineGizmo piece : pieces) {
            piece.show();
        }
    }

    @Override
    public void update() {
        updatePieces();
        for (LineGizmo piece : pieces) {
            piece.update();
        }
    }

    @Override
    public void hide() {
        for (LineGizmo piece : pieces) {
            piece.hide();
        }
    }

    private void updatePieces() {
        if (!checkAndClearDirty()) {
            return;
        }

        double radius = getRadius();
        double width = getWidth();
        double pieceAngle = 2 * Math.PI / pieces.size();
        double pieceLength = 2 * Math.tan(pieceAngle / 2) * (radius + width / 2);

        GizmoAxis rotationAxis = getAxis();
        GizmoAxis pieceAxis = rotationAxis.next();
        GizmoAxis offsetAxis = pieceAxis.next();

        Vector3d pieceOffset = new Vector3d();
        pieceAxis.setValue(pieceOffset, -pieceLength / 2);
        offsetAxis.setValue(pieceOffset, radius);

        double currentAngle = 0;
        Quaterniond currentRotation = new Quaterniond();
        Vector3d currentOffset = new Vector3d();
        for (LineGizmo piece : pieces) {
            piece.setPosition(getPosition());
            piece.setRotation(getRotation().rotateAxis(currentAngle, rotationAxis.direction(), currentRotation));
            piece.setOffset(pieceOffset.rotate(currentRotation, currentOffset).add(getOffset()));
            piece.setAxis(pieceAxis);
            piece.setWidth(width);
            piece.setLength(pieceLength);
            piece.setColor(getColor());
            currentAngle += pieceAngle;
        }
    }
}
