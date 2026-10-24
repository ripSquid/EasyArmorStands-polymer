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

import re.koda.easyarmorstands.gizmo.api.cursor.Cursor;
import re.koda.easyarmorstands.gizmo.api.cursor.Intersection;
import re.koda.easyarmorstands.gizmo.cube.CubeGizmo;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import org.joml.Vector3dc;

import java.util.Objects;

@ApiStatus.Internal
public abstract class AbstractCubeGizmo extends AbstractGizmo implements CubeGizmo {
    private final Vector3d scale = new Vector3d(DEFAULT_SCALE);
    private boolean billboard = DEFAULT_BILLBOARD;

    @Override
    public @NotNull Vector3dc getScale() {
        return scale;
    }

    @Override
    public void setScale(@NotNull Vector3dc scale) {
        if (!Objects.equals(this.scale, scale)) {
            this.scale.set(scale);
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
        return null;
    }
}
