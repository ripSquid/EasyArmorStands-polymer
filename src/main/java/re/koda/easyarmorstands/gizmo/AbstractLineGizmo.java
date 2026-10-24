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
package re.koda.easyarmorstands.gizmo;

import re.koda.easyarmorstands.gizmo.api.GizmoAxis;
import re.koda.easyarmorstands.gizmo.api.LineGizmo;
import re.koda.easyarmorstands.gizmo.api.cursor.Cursor;
import re.koda.easyarmorstands.gizmo.api.cursor.Intersection;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import java.util.Objects;

@ApiStatus.Internal
public abstract class AbstractLineGizmo extends AbstractGizmo implements LineGizmo {
    private GizmoAxis axis = DEFAULT_AXIS;
    private double length = DEFAULT_LENGTH;
    private double width = DEFAULT_WIDTH;

    @Override
    public @NotNull GizmoAxis getAxis() {
        return axis;
    }

    @Override
    public void setAxis(@NotNull GizmoAxis axis) {
        if (!Objects.equals(this.axis, axis)) {
            this.axis = axis;
            markDirty();
        }
    }

    @Override
    public double getLength() {
        return length;
    }

    @Override
    public void setLength(double length) {
        if (!Objects.equals(this.length, length)) {
            this.length = length;
            markDirty();
        }
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public void setWidth(double width) {
        if (!Objects.equals(this.width, width)) {
            this.width = width;
            markDirty();
        }
    }

    @Override
    public @Nullable Intersection intersect(@NotNull Cursor cursor, double threshold) {
        Vector3d start = getPosition().add(getOffset(), new Vector3d());
        Vector3d end = getAxis().direction().mul(getLength(), new Vector3d()).rotate(getRotation()).add(start);
        return cursor.intersectLine(start, end, threshold);
    }
}
