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
package re.koda.easyarmorstands.editor.layer;

import re.koda.easyarmorstands.api.editor.Session;
import re.koda.easyarmorstands.api.editor.context.EnterContext;
import re.koda.easyarmorstands.api.editor.context.ExitContext;
import re.koda.easyarmorstands.api.editor.context.UpdateContext;
import re.koda.easyarmorstands.api.editor.tool.MoveToolSession;
import re.koda.easyarmorstands.util.Cursor3D;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector3dc;

public class MoveToolLayer extends ToolLayer {
    protected final Session session;
    protected final MoveToolSession toolSession;
    protected final Cursor3D cursor;
    private final Vector3dc initialPosition;
    private final Vector3d change = new Vector3d();

    public MoveToolLayer(Session session, MoveToolSession toolSession, Component name, Vector3dc position) {
        super(session, toolSession, name);
        this.session = session;
        this.toolSession = toolSession;
        this.cursor = new Cursor3D(session);
        this.initialPosition = new Vector3d(position);
    }

    @Override
    public void onEnter(@NotNull EnterContext context) {
        cursor.start(context, initialPosition);
    }

    @Override
    public void onUpdate(@NotNull UpdateContext context) {
        cursor.update(context);
        cursor.get().sub(initialPosition, change);
        toolSession.snapChange(change, session.snapper());
        toolSession.setChange(change);
        super.onUpdate(context);
    }

    @Override
    public void onExit(@NotNull ExitContext context) {
        super.onExit(context);
        cursor.stop();
    }
}
