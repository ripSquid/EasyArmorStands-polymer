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

import re.koda.easyarmorstands.api.Axis;
import re.koda.easyarmorstands.api.editor.Snapper;
import re.koda.easyarmorstands.api.editor.tool.AxisRotateTool;
import re.koda.easyarmorstands.api.editor.tool.AxisRotateToolSession;
import re.koda.easyarmorstands.api.editor.tool.ToolContext;
import re.koda.easyarmorstands.api.property.PendingChange;
import re.koda.easyarmorstands.api.property.Property;
import re.koda.easyarmorstands.api.property.PropertyContainer;
import re.koda.easyarmorstands.api.property.type.DisplayPropertyTypes;
import re.koda.easyarmorstands.api.property.type.EntityPropertyTypes;
import re.koda.easyarmorstands.api.property.type.PropertyType;
import re.koda.easyarmorstands.api.util.RotationProvider;
import re.koda.easyarmorstands.editor.tool.AbstractToolSession;
import re.koda.easyarmorstands.message.Message;
import re.koda.easyarmorstands.platform.entity.Player;
import re.koda.easyarmorstands.platform.util.Location;
import re.koda.easyarmorstands.util.Util;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaterniond;
import org.joml.Quaterniondc;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class DisplayAxisRotateTool implements AxisRotateTool {
    private final ToolContext context;
    private final PropertyContainer properties;
    private final Property<Location> locationProperty;
    private final Property<Vector3fc> translationProperty;
    private final Property<Quaternionfc> rotationProperty;
    private final Property<Vector3fc> scaleProperty;
    private final Property<Float> heightProperty;
    private final Axis axis;
    private final RotationProvider parentRotationProvider;

    public DisplayAxisRotateTool(ToolContext context, PropertyContainer properties, PropertyType<Quaternionfc> type, Axis axis, RotationProvider parentRotationProvider) {
        this.context = context;
        this.properties = properties;
        this.locationProperty = properties.get(EntityPropertyTypes.LOCATION);
        this.translationProperty = properties.get(DisplayPropertyTypes.TRANSLATION);
        this.heightProperty = properties.get(DisplayPropertyTypes.BOX_HEIGHT);
        this.rotationProperty = properties.get(type);
        this.scaleProperty = properties.get(DisplayPropertyTypes.SCALE);
        this.axis = axis;
        this.parentRotationProvider = parentRotationProvider;
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
    public @NotNull Axis getAxis() {
        return axis;
    }

    @Override
    public @NotNull AxisRotateToolSession start() {
        return new SessionImpl();
    }

    @Override
    public boolean canUse(@NotNull Player player) {
        return rotationProperty.canChange(player) &&
                locationProperty.canChange(player) &&
                translationProperty.canChange(player);
    }

    private class SessionImpl extends AbstractToolSession implements AxisRotateToolSession {
        private final Location originalLocation;
        private final Vector3fc originalTranslation;
        private final Quaternionfc originalRotation;
        private final Vector3dc originalOffset;
        private final Vector3dc translationOffset;
        private final Vector3dc direction;
        private final Vector3dc localDirection;
        private final Quaternionf currentRotation = new Quaternionf();
        private final Vector3f currentTranslation = new Vector3f();
        private final Vector3d offsetChange = new Vector3d();
        private double change;

        public SessionImpl() {
            super(properties);
            float height = heightProperty.getValue();
            Quaterniondc rotation = getRotation();
            Quaterniondc localRotation = parentRotationProvider.getRotation().conjugate(new Quaterniond())
                    .mul(rotation);
            this.originalLocation = locationProperty.getValue();
            this.originalTranslation = new Vector3f(translationProperty.getValue());
            this.originalRotation = new Quaternionf(rotationProperty.getValue());
            this.originalOffset = new Vector3d(originalLocation.position())
                    .add(0, height / 2, 0)
                    .sub(getPosition());
            this.translationOffset = new Vector3d(originalTranslation)
                    .sub(0, height / 2, 0);
            this.direction = axis.getDirection().rotate(rotation, new Vector3d());
            this.localDirection = axis.getDirection().rotate(localRotation, new Vector3d());
        }

        @Override
        public void setChange(double change) {
            this.change = change;

            originalOffset.rotateAxis(change,
                            direction.x(),
                            direction.y(),
                            direction.z(), offsetChange)
                    .sub(originalOffset);

            Location location = originalLocation
                    .withPosition(originalLocation.position().add(offsetChange, new Vector3d()));

            translationOffset.rotateAxis(change,
                            direction.x(),
                            direction.y(),
                            direction.z(), offsetChange)
                    .sub(translationOffset);
            originalTranslation.add(offsetChange.get(new Vector3f()), currentTranslation);

            currentRotation.setAngleAxis(
                            change,
                            localDirection.x(),
                            localDirection.y(),
                            localDirection.z())
                    .mul(originalRotation);

            PendingChange locationChange = locationProperty.prepareChange(location);
            if (locationChange != null) {
                PendingChange translationChange = translationProperty.prepareChange(currentTranslation);
                if (translationChange != null) {
                    PendingChange rotationChange = rotationProperty.prepareChange(currentRotation);
                    if (rotationChange != null) {
                        if (locationChange.execute()) {
                            if (translationChange.execute()) {
                                rotationChange.execute();
                            }
                        }
                    }
                }
            }
        }

        @Override
        public double snapChange(double change, @NotNull Snapper context) {
            return context.snapAngle(change);
        }

        @Override
        public void revert() {
            locationProperty.setValue(originalLocation);
            translationProperty.setValue(originalTranslation);
            rotationProperty.setValue(originalRotation);
        }

        @Override
        public @Nullable Component getStatus() {
            return Util.formatAngle(change);
        }

        @Override
        public @Nullable Component getDescription() {
            Component axisName = Component.text(axis.getName(), TextColor.color(axis.getColor()));
            return Message.component("easyarmorstands.history.rotate-axis", axisName);
        }
    }
}
