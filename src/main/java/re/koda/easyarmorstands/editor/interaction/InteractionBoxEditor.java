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
package re.koda.easyarmorstands.editor.interaction;

import re.koda.easyarmorstands.api.property.Property;
import re.koda.easyarmorstands.api.property.PropertyContainer;
import re.koda.easyarmorstands.api.property.type.DisplayPropertyTypes;
import re.koda.easyarmorstands.api.property.type.EntityPropertyTypes;
import re.koda.easyarmorstands.api.util.BoundingBox;
import re.koda.easyarmorstands.editor.box.AbstractBoundingBoxEditorSession;
import re.koda.easyarmorstands.editor.box.BoundingBoxEditor;
import re.koda.easyarmorstands.editor.box.BoundingBoxEditorSession;
import re.koda.easyarmorstands.platform.entity.Player;
import re.koda.easyarmorstands.platform.util.Location;
import org.joml.Vector3d;
import org.joml.Vector3dc;

public class InteractionBoxEditor implements BoundingBoxEditor {
    private final PropertyContainer properties;
    private final Property<Location> locationProperty;
    private final Property<Float> widthProperty;
    private final Property<Float> heightProperty;

    public InteractionBoxEditor(PropertyContainer properties) {
        this.properties = properties;
        this.locationProperty = properties.get(EntityPropertyTypes.LOCATION);
        this.widthProperty = properties.get(DisplayPropertyTypes.BOX_WIDTH);
        this.heightProperty = properties.get(DisplayPropertyTypes.BOX_HEIGHT);
    }

    @Override
    public BoundingBox getBoundingBox() {
        return BoundingBox.of(
                locationProperty.getValue().position(),
                (double) widthProperty.getValue(),
                (double) heightProperty.getValue());
    }

    @Override
    public boolean canMove(Player player) {
        return locationProperty.canChange(player);
    }

    @Override
    public boolean canResize(Player player) {
        return widthProperty.canChange(player) && heightProperty.canChange(player);
    }

    @Override
    public BoundingBoxEditorSession start() {
        return new SessionImpl();
    }

    private class SessionImpl extends AbstractBoundingBoxEditorSession {
        private final Location originalLocation;
        private final float originalWidth;
        private final float originalHeight;

        private SessionImpl() {
            super(properties);
            this.originalLocation = locationProperty.getValue();
            this.originalWidth = widthProperty.getValue();
            this.originalHeight = heightProperty.getValue();
        }

        @Override
        public BoundingBox getBoundingBox() {
            return InteractionBoxEditor.this.getBoundingBox();
        }

        @Override
        public boolean setCenter(Vector3dc center) {
            Vector3d delta = center.sub(0, getHeight() / 2, 0, new Vector3d()).sub(originalLocation.position());
            Location location = originalLocation.withPosition(originalLocation.position().add(delta, new Vector3d()));
            return locationProperty.setValue(location);
        }

        @Override
        public float getWidth() {
            return widthProperty.getValue();
        }

        @Override
        public boolean setWidth(float width) {
            return widthProperty.setValue(width);
        }

        @Override
        public float getHeight() {
            return heightProperty.getValue();
        }

        @Override
        public boolean setHeight(float height) {
            return heightProperty.setValue(height);
        }

        @Override
        public void revert() {
            locationProperty.setValue(originalLocation);
            widthProperty.setValue(originalWidth);
            heightProperty.setValue(originalHeight);
        }
    }
}
