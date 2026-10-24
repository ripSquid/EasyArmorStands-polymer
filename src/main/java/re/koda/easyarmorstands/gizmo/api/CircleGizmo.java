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

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@ApiStatus.NonExtendable
public interface CircleGizmo extends Gizmo {
    @NotNull GizmoAxis DEFAULT_AXIS = GizmoAxis.Y;
    double DEFAULT_RADIUS = 1.0;
    double DEFAULT_WIDTH = LineGizmo.DEFAULT_WIDTH;

    static @NotNull CircleGizmo of(@NotNull List<@NotNull LineGizmo> lines) {
        return new LineCircleGizmo(new ArrayList<>(lines));
    }

    static @NotNull CircleGizmo of(int count, @NotNull Supplier<@NotNull LineGizmo> supplier) {
        ArrayList<LineGizmo> lines = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            lines.add(supplier.get());
        }
        return new LineCircleGizmo(lines);
    }

    @NotNull GizmoAxis getAxis();

    void setAxis(@NotNull GizmoAxis axis);

    double getRadius();

    void setRadius(double radius);

    double getWidth();

    void setWidth(double width);
}
