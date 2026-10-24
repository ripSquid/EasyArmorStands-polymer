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

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector3dc;

public enum GizmoAxis {
    X(new Vector3d(1, 0, 0)) {
        @Override
        public @NotNull GizmoAxis next() {
            return GizmoAxis.Y;
        }

        @Override
        public double getValue(@NotNull Vector3dc vector) {
            return vector.x();
        }

        @Override
        public void setValue(@NotNull Vector3d vector, double value) {
            vector.x = value;
        }
    },

    Y(new Vector3d(0, 1, 0)) {
        @Override
        public @NotNull GizmoAxis next() {
            return GizmoAxis.Z;
        }

        @Override
        public double getValue(@NotNull Vector3dc vector) {
            return vector.y();
        }

        @Override
        public void setValue(@NotNull Vector3d vector, double value) {
            vector.y = value;
        }
    },

    Z(new Vector3d(0, 0, 1)) {
        @Override
        public @NotNull GizmoAxis next() {
            return GizmoAxis.X;
        }

        @Override
        public double getValue(@NotNull Vector3dc vector) {
            return vector.z();
        }

        @Override
        public void setValue(@NotNull Vector3d vector, double value) {
            vector.z = value;
        }
    };

    private final Vector3dc direction;

    GizmoAxis(Vector3dc direction) {
        this.direction = direction;
    }

    public @NotNull Vector3dc direction() {
        return direction;
    }

    public abstract @NotNull GizmoAxis next();

    public abstract double getValue(@NotNull Vector3dc vector);

    public abstract void setValue(@NotNull Vector3d vector, double value);
}
