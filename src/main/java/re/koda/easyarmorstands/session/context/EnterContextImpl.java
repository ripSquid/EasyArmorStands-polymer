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
package re.koda.easyarmorstands.session.context;

import re.koda.easyarmorstands.api.editor.EyeRay;
import re.koda.easyarmorstands.api.editor.context.EnterContext;
import re.koda.easyarmorstands.session.SessionImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2dc;
import org.joml.Vector3dc;

public class EnterContextImpl implements EnterContext {
    private final @NotNull SessionImpl session;
    private final @Nullable Vector3dc cursor;
    private EyeRay eyeRay;

    public EnterContextImpl(@NotNull SessionImpl session, @Nullable Vector3dc cursor) {
        this.session = session;
        this.cursor = cursor;
    }

    @Override
    public @NotNull EyeRay eyeRay() {
        if (eyeRay == null) {
            eyeRay = session.eyeRay();
        }
        return eyeRay;
    }

    @Override
    public @NotNull EyeRay eyeRay(@NotNull Vector2dc cursor) {
        return session.eyeRay(cursor);
    }

    @Override
    public @Nullable Vector3dc cursor() {
        return cursor;
    }
}
