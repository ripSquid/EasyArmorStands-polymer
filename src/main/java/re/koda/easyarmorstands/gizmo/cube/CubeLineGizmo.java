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
package re.koda.easyarmorstands.gizmo.cube;

import re.koda.easyarmorstands.gizmo.AbstractLineGizmo;
import re.koda.easyarmorstands.gizmo.api.GizmoAxis;
import re.koda.easyarmorstands.gizmo.api.color.GizmoColor;
import org.jetbrains.annotations.ApiStatus;
import org.joml.Quaterniondc;
import org.joml.Vector3d;
import org.joml.Vector3dc;

@ApiStatus.Internal
class CubeLineGizmo extends AbstractLineGizmo {
    private final CubeGizmo cube;

    public CubeLineGizmo(CubeGizmo cube) {
        this.cube = cube;
    }

    @Override
    public void show() {
        updateCube();
        cube.show();
    }

    @Override
    public void update() {
        updateCube();
        cube.update();
    }

    @Override
    public void hide() {
        cube.hide();
    }

    private void updateCube() {
        if (!checkAndClearDirty()) {
            return;
        }

        GizmoAxis axis = getAxis();
        double length = getLength();
        double width = getWidth();
        Vector3dc position = getPosition();
        Vector3dc offset = getOffset();
        Quaterniondc rotation = getRotation();
        GizmoColor color = getColor();

        Vector3d translation = new Vector3d(-width / 2);
        axis.setValue(translation, 0);
        translation.rotate(rotation);
        translation.add(offset);

        Vector3d scale = new Vector3d(width);
        axis.setValue(scale, length);

        cube.setPosition(position);
        cube.setOffset(translation);
        cube.setRotation(rotation);
        cube.setScale(scale);
        cube.setColor(color);
    }
}
