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
import org.joml.Vector3d;
import org.joml.Vector3dc;

import java.util.List;
import java.util.function.Supplier;

@ApiStatus.NonExtendable
public interface BoxGizmo extends Gizmo {
    @NotNull Vector3dc DEFAULT_SIZE = new Vector3d();
    double DEFAULT_WIDTH = LineGizmo.DEFAULT_WIDTH;

    static @NotNull BoxGizmo of(@NotNull List<@NotNull LineGizmo> lines) {
        return new LineBoxGizmo(lines.toArray(new LineGizmo[LineBoxGizmo.LINE_INFO.length]));
    }

    static @NotNull BoxGizmo of(@NotNull Supplier<@NotNull LineGizmo> supplier) {
        LineGizmo[] lines = new LineGizmo[LineBoxGizmo.LINE_INFO.length];
        for (int i = 0; i < lines.length; i++) {
            lines[i] = supplier.get();
        }
        return new LineBoxGizmo(lines);
    }

    @NotNull Vector3dc getSize();

    void setSize(@NotNull Vector3dc size);

    double getWidth();

    void setWidth(double width);
}
