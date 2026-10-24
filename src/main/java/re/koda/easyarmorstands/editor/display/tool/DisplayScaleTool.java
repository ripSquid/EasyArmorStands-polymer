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
package re.koda.easyarmorstands.editor.display.tool;

import re.koda.easyarmorstands.EasyArmorStandsCommon;
import re.koda.easyarmorstands.api.editor.Snapper;
import re.koda.easyarmorstands.api.editor.tool.ScaleTool;
import re.koda.easyarmorstands.api.editor.tool.ScaleToolSession;
import re.koda.easyarmorstands.api.editor.tool.ToolContext;
import re.koda.easyarmorstands.api.property.PropertyContainer;
import re.koda.easyarmorstands.util.Util;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class DisplayScaleTool extends AbstractDisplayScaleTool<ScaleToolSession> implements ScaleTool {
    public DisplayScaleTool(EasyArmorStandsCommon eas, ToolContext context, PropertyContainer properties) {
        super(eas, context, properties);
    }

    @Override
    public @NotNull ScaleToolSession start() {
        return new SessionImpl();
    }

    private class SessionImpl extends AbstractDisplayScaleToolSession implements ScaleToolSession {
        private final Vector3f currentScale;
        private final float originalAverage;

        public SessionImpl() {
            super(getProperties());
            Vector3fc originalScale = getOriginalScale();
            this.currentScale = new Vector3f(originalScale);
            this.originalAverage = (originalScale.x() + originalScale.y() + originalScale.z()) / 3;
        }

        @Override
        public void setChange(double change) {
            setScale(getOriginalScale().mul((float) change, currentScale));
        }

        @Override
        public double snapChange(double change, @NotNull Snapper context) {
            if (isCloseToZero(originalAverage)) {
                return context.snapOffset(change);
            }
            change *= originalAverage;
            change = context.snapOffset(change);
            change /= originalAverage;
            return change;
        }

        @Override
        public void setValue(double value) {
            currentScale.set(value);
            setScale(currentScale);
        }

        @Override
        public @Nullable Component getStatus() {
            return Util.formatScale(getScale());
        }

        @Override
        public @Nullable Component getDescription() {
            return null;
        }
    }
}
