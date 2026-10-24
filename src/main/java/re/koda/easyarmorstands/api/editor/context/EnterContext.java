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
package re.koda.easyarmorstands.api.editor.context;

import re.koda.easyarmorstands.api.editor.EyeRay;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2dc;
import org.joml.Vector3dc;

import java.util.Objects;
import java.util.function.Supplier;

@ApiStatus.NonExtendable
public interface EnterContext {
    @Contract(pure = true)
    @NotNull EyeRay eyeRay();

    @Contract(pure = true)
    @NotNull EyeRay eyeRay(@NotNull Vector2dc cursor);

    @Contract(pure = true)
    @Nullable Vector3dc cursor();

    @Contract(pure = true)
    default @NotNull Vector3dc cursorOrDefault(@NotNull Vector3dc defaultCursor) {
        Vector3dc result = cursor();
        if (result == null) {
            result = Objects.requireNonNull(defaultCursor);
        }
        return result;
    }

    @Contract(pure = true)
    default @NotNull Vector3dc cursorOrDefault(@NotNull Supplier<@NotNull Vector3dc> defaultCursorSupplier) {
        Vector3dc result = cursor();
        if (result == null) {
            result = Objects.requireNonNull(defaultCursorSupplier.get());
        }
        return result;
    }
}
