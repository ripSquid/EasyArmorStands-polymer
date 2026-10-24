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
package re.koda.easyarmorstands.particle;

import re.koda.easyarmorstands.api.Axis;
import re.koda.easyarmorstands.api.particle.ParticleColor;
import re.koda.easyarmorstands.gizmo.api.GizmoAxis;
import re.koda.easyarmorstands.gizmo.api.color.GizmoColor;

import java.util.EnumMap;
import java.util.Map;

public final class GizmoAdapter {
    private static final Map<Axis, GizmoAxis> AXIS_MAP = new EnumMap<>(Axis.class);
    private static final Map<ParticleColor, GizmoColor> COLOR_MAP = new EnumMap<>(ParticleColor.class);

    static {
        AXIS_MAP.put(Axis.X, GizmoAxis.X);
        AXIS_MAP.put(Axis.Y, GizmoAxis.Y);
        AXIS_MAP.put(Axis.Z, GizmoAxis.Z);
        COLOR_MAP.put(ParticleColor.WHITE, GizmoColor.WHITE);
        COLOR_MAP.put(ParticleColor.RED, GizmoColor.RED);
        COLOR_MAP.put(ParticleColor.GREEN, GizmoColor.GREEN);
        COLOR_MAP.put(ParticleColor.BLUE, GizmoColor.BLUE);
        COLOR_MAP.put(ParticleColor.YELLOW, GizmoColor.YELLOW);
        COLOR_MAP.put(ParticleColor.GRAY, GizmoColor.GRAY);
        COLOR_MAP.put(ParticleColor.AQUA, GizmoColor.AQUA);
    }

    private GizmoAdapter() {
    }

    public static GizmoAxis convert(Axis axis) {
        return AXIS_MAP.get(axis);
    }

    public static GizmoColor convert(ParticleColor color) {
        return COLOR_MAP.get(color);
    }
}
