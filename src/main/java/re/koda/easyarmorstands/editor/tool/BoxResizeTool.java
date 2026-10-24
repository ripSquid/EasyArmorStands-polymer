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

import re.koda.easyarmorstands.api.Axis;
import re.koda.easyarmorstands.api.editor.Snapper;
import re.koda.easyarmorstands.api.editor.tool.AxisMoveTool;
import re.koda.easyarmorstands.api.editor.tool.AxisMoveToolSession;
import re.koda.easyarmorstands.api.util.PositionProvider;
import re.koda.easyarmorstands.api.util.RotationProvider;
import re.koda.easyarmorstands.editor.box.BoundingBoxEditor;
import re.koda.easyarmorstands.editor.box.BoundingBoxEditorSession;
import re.koda.easyarmorstands.message.Message;
import re.koda.easyarmorstands.permission.Permissions;
import re.koda.easyarmorstands.platform.entity.Player;
import re.koda.easyarmorstands.util.Util;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Math;
import org.joml.Quaterniondc;
import org.joml.Vector3d;
import org.joml.Vector3dc;

public class BoxResizeTool implements AxisMoveTool {
    private final BoundingBoxEditor editor;
    private final PositionProvider positionProvider;
    private final RotationProvider rotationProvider;
    private final Axis axis;
    private final boolean end;

    public BoxResizeTool(BoundingBoxEditor editor, PositionProvider positionProvider, RotationProvider rotationProvider, Axis axis, boolean end) {
        this.editor = editor;
        this.positionProvider = positionProvider;
        this.rotationProvider = rotationProvider;
        this.axis = axis;
        this.end = end;
    }

    @Override
    public @NotNull Vector3dc getPosition() {
        return positionProvider.getPosition();
    }

    @Override
    public @NotNull Quaterniondc getRotation() {
        return rotationProvider.getRotation();
    }

    @Override
    public @NotNull Axis getAxis() {
        return axis;
    }

    @Override
    public boolean canUse(@NotNull Player player) {
        return editor.canResize(player);
    }

    @Override
    public @NotNull AxisMoveToolSession start() {
        return new SessionImpl(editor.start());
    }

    private class SessionImpl implements AxisMoveToolSession {
        private final BoundingBoxEditorSession session;
        private final Vector3dc originalCenter;
        private final float originalWidth;
        private final float originalHeight;
        private final float originalValue;

        public SessionImpl(BoundingBoxEditorSession session) {
            this.session = session;
            this.originalCenter = session.getBoundingBox().getCenter(new Vector3d());
            this.originalWidth = session.getWidth();
            this.originalHeight = session.getHeight();
            if (axis == Axis.Y) {
                this.originalValue = this.originalHeight;
            } else {
                this.originalValue = this.originalWidth;
            }
        }

        @Override
        public void setChange(double change) {
            if (!end) {
                change = -change;
            }

            if (axis == Axis.Y) {
                double offset = change / 2;
                if (!end) {
                    offset = -offset;
                }
                float height = originalHeight + (float) change;
                Vector3dc center = originalCenter.add(0, offset, 0, new Vector3d());
                if (session.setHeight(Math.abs(height))) {
                    session.setCenter(center);
                }
            } else {
                Vector3d offset = new Vector3d();
                axis.setValue(offset, change / 2);
                if (!end) {
                    offset.negate();
                }
                float width = originalWidth + (float) change;
                Vector3dc center = originalCenter.add(offset, new Vector3d());
                if (session.setWidth(Math.abs(width))) {
                    session.setCenter(center);
                }
            }
        }

        @Override
        public double snapChange(double change, @NotNull Snapper context) {
            if (!end) {
                change = -change;
            }
            change += originalValue;
            change = context.snapPosition(change);
            change -= originalValue;
            if (!end) {
                change = -change;
            }
            return change;
        }

        @Override
        public void setValue(double value) {
            double change = value - originalValue;
            if (!end) {
                change = -change;
            }
            setChange(change);
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
            float value;
            if (axis == Axis.Y) {
                value = session.getHeight();
            } else {
                value = session.getWidth();
            }
            return Component.text(Util.SCALE_FORMAT.format(value));
        }

        @Override
        public @Nullable Component getDescription() {
            return Message.component("easyarmorstands.history.resize-box");
        }

        @Override
        public boolean canSetValue(Player player) {
            return player.hasPermission(Permissions.POSITION);
        }
    }
}
