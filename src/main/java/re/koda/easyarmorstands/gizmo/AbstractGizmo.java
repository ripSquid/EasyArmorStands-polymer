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

import re.koda.easyarmorstands.gizmo.api.Gizmo;
import re.koda.easyarmorstands.gizmo.api.color.GizmoColor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaterniond;
import org.joml.Quaterniondc;
import org.joml.Vector3d;
import org.joml.Vector3dc;

import java.util.Objects;

@ApiStatus.Internal
public abstract class AbstractGizmo implements Gizmo {
    private final Vector3d position = new Vector3d();
    private final Vector3d offset = new Vector3d();
    private final Quaterniond rotation = new Quaterniond();
    private GizmoColor color = DEFAULT_COLOR;
    private boolean dirty = true;

    @Override
    public @NotNull Vector3dc getPosition() {
        return position;
    }

    @Override
    public void setPosition(@NotNull Vector3dc position) {
        if (!Objects.equals(this.position, position)) {
            this.position.set(position);
            markDirty();
        }
    }

    @Override
    public @NotNull Vector3dc getOffset() {
        return offset;
    }

    @Override
    public void setOffset(@NotNull Vector3dc offset) {
        if (!Objects.equals(this.offset, offset)) {
            this.offset.set(offset);
            markDirty();
        }
    }

    @Override
    public @NotNull Quaterniondc getRotation() {
        return rotation;
    }

    @Override
    public void setRotation(@NotNull Quaterniondc rotation) {
        if (!Objects.equals(this.rotation, rotation)) {
            this.rotation.set(rotation);
            markDirty();
        }
    }

    @Override
    public @NotNull GizmoColor getColor() {
        return color;
    }

    @Override
    public void setColor(@NotNull GizmoColor color) {
        if (!Objects.equals(this.color, color)) {
            this.color = color;
            markDirty();
        }
    }

    protected void markDirty() {
        dirty = true;
    }

    protected boolean checkAndClearDirty() {
        boolean dirty = this.dirty;
        this.dirty = false;
        return dirty;
    }
}
