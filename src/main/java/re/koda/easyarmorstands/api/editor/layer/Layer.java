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
package re.koda.easyarmorstands.api.editor.layer;

import re.koda.easyarmorstands.api.editor.context.AddContext;
import re.koda.easyarmorstands.api.editor.context.ClickContext;
import re.koda.easyarmorstands.api.editor.context.EnterContext;
import re.koda.easyarmorstands.api.editor.context.ExitContext;
import re.koda.easyarmorstands.api.editor.context.LateUpdateContext;
import re.koda.easyarmorstands.api.editor.context.RemoveContext;
import re.koda.easyarmorstands.api.editor.context.UpdateContext;
import org.jetbrains.annotations.NotNull;

public interface Layer {
    /**
     * Called when the layer is entered (activated).
     * This layer is now on the top of the stack.
     * A layer can only be entered after it was {@link #onAdd(AddContext) added} to the stack.
     *
     * @param context Contains accessors for relevant data.
     */
    void onEnter(@NotNull EnterContext context);

    /**
     * Called when the layer is exited (deactivated).
     * This layer is no longer on the top of the stack.
     * The layer may be {@link #onRemove(RemoveContext) removed} from the stack after being exited.
     *
     * @param context Contains accessors for relevant data.
     */
    void onExit(@NotNull ExitContext context);

    /**
     * Called when the layer is added to the stack.
     *
     * @param context Contains accessors for relevant data.
     */
    default void onAdd(@NotNull AddContext context) {
    }

    /**
     * Called when the layer is removed from the stack.
     *
     * @param context Contains accessors for relevant data.
     */
    default void onRemove(@NotNull RemoveContext context) {
    }

    /**
     * Called every tick while this layer is active (on the top of the stack).
     *
     * @param context Contains accessors for relevant data.
     */
    void onUpdate(@NotNull UpdateContext context);

    /**
     * Called after {@link #onUpdate}.
     *
     * @param context Contains accessors for relevant data.
     */
    default void onLateUpdate(@NotNull LateUpdateContext context) {
    }

    /**
     * Called every tick while this layer is inactive (not on the top of the stack).
     *
     * @param context Contains accessors for relevant data.
     */
    default void onInactiveUpdate(@NotNull UpdateContext context) {
    }

    /**
     * Called when the user clicks.
     *
     * @param context Contains accessors for relevant data.
     * @return Whether the click was handled.
     */
    default boolean onClick(@NotNull ClickContext context) {
        return false;
    }

    boolean isValid();
}
