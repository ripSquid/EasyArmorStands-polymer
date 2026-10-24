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
package re.koda.easyarmorstands.api.editor.tool;

import re.koda.easyarmorstands.api.editor.Snapper;
import re.koda.easyarmorstands.api.property.Property;
import re.koda.easyarmorstands.api.property.PropertyContainer;
import re.koda.easyarmorstands.api.util.EasFormat;
import re.koda.easyarmorstands.platform.entity.Player;
import re.koda.easyarmorstands.platform.util.Location;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Math;
import org.joml.Quaterniondc;
import org.joml.Vector3d;
import org.joml.Vector3dc;

class ScalarScaleTool implements ScaleTool {
    private final ToolContext context;
    private final PropertyContainer properties;
    private final Property<Location> positionProperty;
    private final Property<Double> scaleProperty;
    private final double minScale;
    private final double maxScale;

    ScalarScaleTool(ToolContext context, PropertyContainer properties, Property<Location> positionProperty, Property<Double> scaleProperty, double minScale, double maxScale) {
        this.context = context;
        this.properties = properties;
        this.positionProperty = positionProperty;
        this.scaleProperty = scaleProperty;
        this.minScale = minScale;
        this.maxScale = maxScale;
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
    public @NotNull ScaleToolSession start() {
        return new SessionImpl();
    }

    @Override
    public boolean canUse(@NotNull Player player) {
        return scaleProperty.canChange(player);
    }

    private class SessionImpl implements ScaleToolSession {
        private final Location originalLocation;
        private final double originalScale;
        private final double originalEffectiveScale;
        private final Vector3dc offset;
        private double scale;

        public SessionImpl() {
            this.originalLocation = positionProperty.getValue();
            this.originalScale = scaleProperty.getValue();
            this.originalEffectiveScale = getEffectiveScale(originalScale);
            this.offset = originalLocation.position().sub(getPosition(), new Vector3d());
            this.scale = originalScale;
        }

        private double getEffectiveScale(double scale) {
            return Math.clamp(0.0625, 16.0, scale);
        }

        private void updatePosition() {
            double delta = getEffectiveScale(scale) / originalEffectiveScale - 1;
            Vector3d position = new Vector3d(originalLocation.position());
            position.add(offset.x() * delta, offset.y() * delta, offset.z() * delta);
            positionProperty.setValue(originalLocation.withPosition(position));
        }

        @Override
        public void setChange(double change) {
            scale = Math.clamp(minScale, maxScale, originalEffectiveScale * change);
            scaleProperty.setValue(scale);
            updatePosition();
        }

        @Override
        public double snapChange(double change, @NotNull Snapper context) {
            change *= originalEffectiveScale;
            change = context.snapOffset(change);
            change /= originalEffectiveScale;
            return change;
        }

        @Override
        public void setValue(double value) {
            scale = Math.clamp(minScale, maxScale, value);
            scaleProperty.setValue(scale);
            updatePosition();
        }

        @Override
        public void revert() {
            positionProperty.setValue(originalLocation);
            scaleProperty.setValue(originalScale);
        }

        @Override
        public void commit(@Nullable Component description) {
            properties.commit(description);
        }

        @Override
        public boolean isValid() {
            return properties.isValid();
        }

        @Override
        public @Nullable Component getStatus() {
            return EasFormat.formatScale(scale);
        }

        @Override
        public @Nullable Component getDescription() {
            return null;
        }
    }
}
