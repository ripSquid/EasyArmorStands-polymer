/*
 * EasyArmorStands-Polymer - a Polymer based Fabric port of EasyArmorStands
 * Copyright (C) 56738 and EasyArmorStands contributors (original work)
 * Copyright (C) 2026 ripsquid
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
package re.koda.easyarmorstands.polymer.gizmo;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import re.koda.easyarmorstands.gizmo.api.GizmoFactory;

/**
 * Renders editor gizmos using Polymer virtual entities.
 * <p>
 * Replaces the {@code gizmo-fabric} backend used by upstream EasyArmorStands. Instead of
 * creating real (but unregistered) display entities, every gizmo is a Polymer
 * {@link eu.pb4.polymer.virtualentity.api.elements.BlockDisplayElement} which only exists
 * on the client of the player that is using the editor.
 */
public final class PolymerGizmos {
    private PolymerGizmos() {
    }

    public static @NotNull PolymerGizmos create() {
        return new PolymerGizmos();
    }

    public @NotNull GizmoFactory player(@NotNull ServerPlayer player) {
        return new PolymerGizmoFactory(player);
    }

    /**
     * Virtual elements never exist as server-side entities, so no real entity is ever a gizmo.
     */
    public boolean isGizmo(Entity entity) {
        return false;
    }

    public void close() {
    }
}
