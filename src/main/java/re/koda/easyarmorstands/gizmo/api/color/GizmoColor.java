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
package re.koda.easyarmorstands.gizmo.api.color;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.NonExtendable
public interface GizmoColor {
    @NotNull GizmoColor WHITE = new GizmoColorImpl(0xFFFFFF);
    @NotNull GizmoColor RED = new GizmoColorImpl(0xFF5555);
    @NotNull GizmoColor GREEN = new GizmoColorImpl(0x55FF55);
    @NotNull GizmoColor BLUE = new GizmoColorImpl(0x5555FF);
    @NotNull GizmoColor YELLOW = new GizmoColorImpl(0xFFFF55);
    @NotNull GizmoColor GRAY = new GizmoColorImpl(0xAAAAAA);
    @NotNull GizmoColor AQUA = new GizmoColorImpl(0x55FFFF);

    int asRGB();
}
