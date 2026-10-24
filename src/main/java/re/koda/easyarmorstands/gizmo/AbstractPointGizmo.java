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

import re.koda.easyarmorstands.gizmo.api.PointGizmo;
import re.koda.easyarmorstands.gizmo.api.cursor.Cursor;
import re.koda.easyarmorstands.gizmo.api.cursor.Intersection;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import java.util.Objects;

@ApiStatus.Internal
public abstract class AbstractPointGizmo extends AbstractGizmo implements PointGizmo {
    private double size = DEFAULT_SIZE;
    private boolean billboard = DEFAULT_BILLBOARD;

    @Override
    public double getSize() {
        return size;
    }

    @Override
    public void setSize(double size) {
        if (!Objects.equals(this.size, size)) {
            this.size = size;
            markDirty();
        }
    }

    @Override
    public boolean isBillboard() {
        return billboard;
    }

    @Override
    public void setBillboard(boolean billboard) {
        if (!Objects.equals(this.billboard, billboard)) {
            this.billboard = billboard;
            markDirty();
        }
    }

    @Override
    public @Nullable Intersection intersect(@NotNull Cursor cursor, double threshold) {
        Vector3d position = getPosition().add(getOffset(), new Vector3d());
        return cursor.intersectPoint(position, threshold);
    }
}
