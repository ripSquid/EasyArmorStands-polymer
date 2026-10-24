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
import re.koda.easyarmorstands.api.Axis;
import re.koda.easyarmorstands.api.editor.Snapper;
import re.koda.easyarmorstands.api.editor.tool.AxisScaleTool;
import re.koda.easyarmorstands.api.editor.tool.AxisScaleToolSession;
import re.koda.easyarmorstands.api.editor.tool.ToolContext;
import re.koda.easyarmorstands.api.property.PropertyContainer;
import re.koda.easyarmorstands.message.Message;
import re.koda.easyarmorstands.util.Util;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class DisplayAxisScaleTool extends AbstractDisplayScaleTool<AxisScaleToolSession> implements AxisScaleTool {
    private final Axis axis;

    public DisplayAxisScaleTool(EasyArmorStandsCommon eas, ToolContext context, PropertyContainer properties, Axis axis) {
        super(eas, context, properties);
        this.axis = axis;
    }

    @Override
    public @NotNull Axis getAxis() {
        return axis;
    }

    @Override
    public @NotNull AxisScaleToolSession start() {
        return new SessionImpl();
    }

    private class SessionImpl extends AbstractDisplayScaleToolSession implements AxisScaleToolSession {
        private final Vector3f currentScale;

        public SessionImpl() {
            super(getProperties());
            this.currentScale = new Vector3f(getOriginalScale());
        }

        @Override
        public void setChange(double change) {
            float original = axis.getValue(getOriginalScale());
            setValue(original * change);
        }

        @Override
        public double snapChange(double change, @NotNull Snapper context) {
            float original = axis.getValue(getOriginalScale());
            if (isCloseToZero(original)) {
                return context.snapOffset(change);
            }
            change *= original;
            change = context.snapOffset(change);
            change /= original;
            return change;
        }

        @Override
        public void setValue(double value) {
            currentScale.set(getOriginalScale());
            axis.setValue(currentScale, (float) value);
            setScale(currentScale);
        }

        @Override
        public @Nullable Component getStatus() {
            return Component.text(Util.SCALE_FORMAT.format(axis.getValue(getScale())));
        }

        @Override
        public @Nullable Component getDescription() {
            Component axisName = Component.text(axis.getName(), TextColor.color(axis.getColor()));
            return Message.component("easyarmorstands.history.scale-axis", axisName);
        }
    }
}
