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
package re.koda.easyarmorstands.api;

import re.koda.easyarmorstands.api.particle.ParticleColor;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public enum Axis {
    X("X", ParticleColor.RED, new Vector3d(1, 0, 0)) {
        @Override
        public float getValue(@NotNull Vector3fc source) {
            return source.x();
        }

        @Override
        public double getValue(@NotNull Vector3dc source) {
            return source.x();
        }

        @Override
        public void setValue(@NotNull Vector3f target, float value) {
            target.x = value;
        }

        @Override
        public void setValue(@NotNull Vector3d target, double value) {
            target.x = value;
        }
    },
    Y("Y", ParticleColor.GREEN, new Vector3d(0, 1, 0)) {
        @Override
        public float getValue(@NotNull Vector3fc source) {
            return source.y();
        }

        @Override
        public double getValue(@NotNull Vector3dc source) {
            return source.y();
        }

        @Override
        public void setValue(@NotNull Vector3f target, float value) {
            target.y = value;
        }

        @Override
        public void setValue(@NotNull Vector3d target, double value) {
            target.y = value;
        }
    },
    Z("Z", ParticleColor.BLUE, new Vector3d(0, 0, 1)) {
        @Override
        public float getValue(@NotNull Vector3fc source) {
            return source.z();
        }

        @Override
        public double getValue(@NotNull Vector3dc source) {
            return source.z();
        }

        @Override
        public void setValue(@NotNull Vector3f target, float value) {
            target.z = value;
        }

        @Override
        public void setValue(@NotNull Vector3d target, double value) {
            target.z = value;
        }
    };

    private final String name;
    private final ParticleColor color;
    private final Vector3dc direction;

    Axis(String name, ParticleColor color, Vector3dc direction) {
        this.name = name;
        this.color = color;
        this.direction = direction;
    }

    public @NotNull String getName() {
        return name;
    }

    public @NotNull ParticleColor getColor() {
        return color;
    }

    public @NotNull Vector3dc getDirection() {
        return direction;
    }

    public abstract float getValue(@NotNull Vector3fc source);

    public abstract double getValue(@NotNull Vector3dc source);

    public abstract void setValue(@NotNull Vector3f target, float value);

    public abstract void setValue(@NotNull Vector3d target, double value);
}
