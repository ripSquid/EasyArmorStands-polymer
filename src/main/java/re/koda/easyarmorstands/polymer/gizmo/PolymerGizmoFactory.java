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

import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.attachment.ManualAttachment;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import re.koda.easyarmorstands.gizmo.cube.CubeGizmo;
import re.koda.easyarmorstands.gizmo.cube.CubeGizmoFactory;

/**
 * Creates cube gizmos for a single player. All gizmos created by this factory share one
 * {@link ElementHolder} which is only watched by that player.
 */
class PolymerGizmoFactory implements CubeGizmoFactory {
    private final ServerPlayer owner;
    private ElementHolder holder;
    private ServerPlayer watchingPlayer;
    private ServerLevel watchingLevel;

    PolymerGizmoFactory(ServerPlayer owner) {
        this.owner = owner;
    }

    @Override
    public @NotNull CubeGizmo createCube() {
        return new PolymerCubeGizmo(this);
    }

    @Override
    public boolean isVisibleThroughWalls() {
        return true;
    }

    /**
     * Returns the element holder, making sure the player is (still) watching it.
     * <p>
     * When the player respawns or changes dimension, the client discards all entities,
     * so the holder is re-sent to the player.
     */
    ElementHolder holder() {
        ServerGamePacketListenerImpl connection = owner.connection;
        if (holder == null) {
            holder = new ElementHolder();
        }
        ServerPlayer currentPlayer = connection.player;
        ServerLevel currentLevel = currentPlayer.level();
        if (currentPlayer != watchingPlayer || currentLevel != watchingLevel) {
            holder.stopWatching(connection);
            // Polymer stops watching holders without an attachment on every tick.
            // Gizmos use absolute positions (override pos), so the attachment position is irrelevant.
            Vec3 anchor = currentPlayer.position();
            new ManualAttachment(holder, currentLevel, () -> anchor);
            holder.startWatching(connection);
            watchingPlayer = currentPlayer;
            watchingLevel = currentLevel;
        }
        return holder;
    }
}
