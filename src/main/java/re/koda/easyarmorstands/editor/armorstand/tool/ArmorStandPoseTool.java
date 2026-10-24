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
package re.koda.easyarmorstands.editor.armorstand.tool;

import re.koda.easyarmorstands.api.ArmorStandPart;
import re.koda.easyarmorstands.api.Axis;
import re.koda.easyarmorstands.api.editor.Snapper;
import re.koda.easyarmorstands.api.editor.tool.AxisRotateTool;
import re.koda.easyarmorstands.api.editor.tool.AxisRotateToolSession;
import re.koda.easyarmorstands.api.editor.tool.ToolContext;
import re.koda.easyarmorstands.api.property.Property;
import re.koda.easyarmorstands.api.property.PropertyContainer;
import re.koda.easyarmorstands.api.property.type.ArmorStandPropertyTypes;
import re.koda.easyarmorstands.api.property.type.EntityPropertyTypes;
import re.koda.easyarmorstands.editor.tool.AbstractToolSession;
import re.koda.easyarmorstands.platform.entity.Player;
import re.koda.easyarmorstands.platform.util.Location;
import re.koda.easyarmorstands.platform.util.Rotations;
import re.koda.easyarmorstands.util.EasMath;
import re.koda.easyarmorstands.util.Util;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaterniond;
import org.joml.Quaterniondc;
import org.joml.Vector3d;
import org.joml.Vector3dc;

public class ArmorStandPoseTool implements AxisRotateTool {
    private final ToolContext context;
    private final PropertyContainer properties;
    private final Property<Location> locationProperty;
    private final Property<Rotations> poseProperty;
    private final Axis axis;

    public ArmorStandPoseTool(ToolContext context, PropertyContainer properties, ArmorStandPart part, Axis axis) {
        this.context = context;
        this.properties = properties;
        this.locationProperty = properties.get(EntityPropertyTypes.LOCATION);
        this.poseProperty = properties.get(ArmorStandPropertyTypes.POSE.get(part));
        this.axis = axis;
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
        return poseProperty.canChange(player);
    }

    private class SessionImpl extends AbstractToolSession implements AxisRotateToolSession {
        private final Vector3dc direction;
        private final Rotations originalAngle;
        private final Quaterniondc originalRotation;
        private final Quaterniond currentRotation = new Quaterniond();
        private double change;

        public SessionImpl() {
            super(properties);
            originalAngle = poseProperty.getValue();
            originalRotation = Util.fromEuler(originalAngle, new Quaterniond());
            Location location = locationProperty.getValue();
            direction = axis.getDirection().rotate(
                    EasMath.getInverseEntityYawRotation(location.yaw(), new Quaterniond())
                            .mul(getRotation()),
                    new Vector3d());
        }

        @Override
        public void setChange(double change) {
            this.change = change;
            currentRotation.setAngleAxis(change, direction).mul(originalRotation);
            poseProperty.setValue(Util.toEuler(currentRotation));
        }

        @Override
        public double snapChange(double change, @NotNull Snapper context) {
            return context.snapAngle(change);
        }

        @Override
        public void revert() {
            poseProperty.setValue(originalAngle);
        }

        @Override
        public @Nullable Component getStatus() {
            return Util.formatAngle(change);
        }

        @Override
        public @Nullable Component getDescription() {
            return null;
        }
    }
}
