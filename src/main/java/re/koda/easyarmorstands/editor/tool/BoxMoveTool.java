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
package re.koda.easyarmorstands.editor.tool;

import re.koda.easyarmorstands.api.editor.Snapper;
import re.koda.easyarmorstands.api.editor.tool.MoveTool;
import re.koda.easyarmorstands.api.editor.tool.MoveToolSession;
import re.koda.easyarmorstands.api.editor.tool.ToolContext;
import re.koda.easyarmorstands.editor.box.BoundingBoxEditor;
import re.koda.easyarmorstands.editor.box.BoundingBoxEditorSession;
import re.koda.easyarmorstands.message.Message;
import re.koda.easyarmorstands.permission.Permissions;
import re.koda.easyarmorstands.platform.entity.Player;
import re.koda.easyarmorstands.util.Util;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaterniondc;
import org.joml.Vector3d;
import org.joml.Vector3dc;

public class BoxMoveTool implements MoveTool {
    private final BoundingBoxEditor editor;
    private final ToolContext context;

    public BoxMoveTool(BoundingBoxEditor editor, ToolContext context) {
        this.editor = editor;
        this.context = context;
    }

    @Override
    public @NotNull Vector3dc getPosition() {
        return context.position().getPosition();
    }

    @Override
    public @NotNull Quaterniondc getRotation() {
        return context.rotation().getRotation();
    }

    @Override
    public @NotNull MoveToolSession start() {
        return new SessionImpl(editor.start());
    }

    @Override
    public boolean canUse(@NotNull Player player) {
        return editor.canMove(player);
    }

    private static class SessionImpl implements MoveToolSession {
        private final BoundingBoxEditorSession session;
        private final Vector3dc center;
        private final Vector3d offset = new Vector3d();

        public SessionImpl(BoundingBoxEditorSession session) {
            this.session = session;
            this.center = session.getBoundingBox().getCenter(new Vector3d());
        }

        @Override
        public void setChange(@NotNull Vector3dc change) {
            this.offset.set(change);
            session.setCenter(center.add(change, new Vector3d(offset)));
        }

        @Override
        public void snapChange(@NotNull Vector3d change, @NotNull Snapper context) {
            change.add(center);
            context.snapPosition(change);
            change.sub(center);
        }

        @Override
        public @NotNull Vector3dc getPosition() {
            return session.getBoundingBox().getCenter(new Vector3d());
        }

        @Override
        public void setPosition(@NotNull Vector3dc position) {
            setChange(position.sub(center, offset));
        }

        @Override
        public void revert() {
            session.revert();
        }

        @Override
        public void commit(@Nullable Component description) {
            session.commit(description);
        }

        @Override
        public boolean isValid() {
            return session.isValid();
        }

        @Override
        public @Nullable Component getStatus() {
            return Util.formatOffset(offset);
        }

        @Override
        public @Nullable Component getDescription() {
            return Message.component("easyarmorstands.history.move-box", Util.formatOffset(offset));
        }

        @Override
        public boolean canSetPosition(Player player) {
            return player.hasPermission(Permissions.POSITION);
        }
    }
}
