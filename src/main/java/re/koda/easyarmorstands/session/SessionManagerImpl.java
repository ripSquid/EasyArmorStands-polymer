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
package re.koda.easyarmorstands.session;

import re.koda.easyarmorstands.EasyArmorStandsCommon;
import re.koda.easyarmorstands.api.editor.Session;
import re.koda.easyarmorstands.api.editor.SessionManager;
import re.koda.easyarmorstands.api.editor.layer.ElementSelectionLayer;
import re.koda.easyarmorstands.command.sender.EasPlayer;
import re.koda.easyarmorstands.editor.layer.ElementSelectionLayerImpl;
import re.koda.easyarmorstands.editor.layer.EntityElementDiscoverySource;
import re.koda.easyarmorstands.event.EventDispatcher;
import re.koda.easyarmorstands.permission.Permissions;
import re.koda.easyarmorstands.platform.entity.Player;
import re.koda.easyarmorstands.platform.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class SessionManagerImpl implements SessionManager {
    private final EventDispatcher eventDispatcher;
    private final SessionToolProvider toolProvider;
    private final EasyArmorStandsCommon eas;
    private final HashMap<UUID, SessionImpl> sessions = new HashMap<>();

    public SessionManagerImpl(EventDispatcher eventDispatcher, SessionToolProvider toolProvider, EasyArmorStandsCommon eas) {
        this.eventDispatcher = eventDispatcher;
        this.toolProvider = toolProvider;
        this.eas = eas;
    }

    public void startSession(SessionImpl session) {
        final SessionImpl old = sessions.put(session.player().uniqueId(), session);
        if (old != null) {
            old.stop();
            eventDispatcher.dispatchSessionStop(old);
        }
        eventDispatcher.dispatchSessionStart(session);
    }

    @Override
    public @NotNull SessionImpl startSession(@NotNull Player player) {
        SessionImpl session = new SessionImpl(eas, new EasPlayer(eas, player));
        ElementSelectionLayer layer = new ElementSelectionLayerImpl(eas, session);
        layer.addSource(new EntityElementDiscoverySource(eas, player));
        session.pushLayer(layer);
        startSession(session);
        return session;
    }

    @Override
    public void stopSession(@NotNull Session session) {
        SessionImpl s = (SessionImpl) session;
        if (sessions.remove(session.player().uniqueId(), s)) {
            s.stop();
            eventDispatcher.dispatchSessionStop(session);
        }
    }

    public boolean stopSession(Player player) {
        SessionImpl session = sessions.remove(player.uniqueId());
        if (session != null) {
            session.stop();
            eventDispatcher.dispatchSessionStop(session);
            return true;
        }
        return false;
    }

    public void update() {
        for (Iterator<SessionImpl> iterator = sessions.values().iterator(); iterator.hasNext(); ) {
            SessionImpl session = iterator.next();
            boolean valid = session.update();
            if (session.isToolRequired() && !isHoldingTool(session.player())) {
                valid = false;
            }
            if (!valid) {
                iterator.remove();
                session.stop();
                eventDispatcher.dispatchSessionStop(session);
            }
        }
    }

    public void stopAllSessions() {
        List<SessionImpl> sessions = new ArrayList<>(this.sessions.values());
        this.sessions.clear();
        for (SessionImpl session : sessions) {
            session.stop();
        }
        for (SessionImpl session : sessions) {
            eventDispatcher.dispatchSessionStop(session);
        }
    }

    public Collection<SessionImpl> getAllSessions() {
        return Collections.unmodifiableCollection(sessions.values());
    }

    @Override
    public @Nullable SessionImpl getSession(@NotNull Player player) {
        return sessions.get(player.uniqueId());
    }

    public boolean isHoldingTool(Player player) {
        if (!player.hasPermission(Permissions.EDIT)) {
            return false;
        }
        return toolProvider.isTool(player.getEquipment(EquipmentSlot.HAND))
                || toolProvider.isTool(player.getEquipment(EquipmentSlot.OFF_HAND));
    }

    public void updateHeldItem(Player player) {
        SessionImpl session = getSession(player);
        if (session != null) {
            updateHeldItem(session);
        } else if (isHoldingTool(player)) {
            session = startSession(player);
            session.setToolRequired(true);
        }
    }

    public void updateHeldItem(SessionImpl session) {
        Player player = session.player();
        if (session.isToolRequired() && !isHoldingTool(player)) {
            stopSession(player);
        }
    }

    public void replacePlayer(Player newPlayer) {
        SessionImpl session = sessions.get(newPlayer.uniqueId());
        if (session != null) {
            session.setPlayer(newPlayer);
        }
    }
}
