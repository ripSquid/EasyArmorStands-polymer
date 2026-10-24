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
package re.koda.easyarmorstands.element;

import re.koda.easyarmorstands.EasyArmorStandsCommon;
import re.koda.easyarmorstands.api.editor.Session;
import re.koda.easyarmorstands.api.editor.button.BoundingBoxButton;
import re.koda.easyarmorstands.api.editor.button.Button;
import re.koda.easyarmorstands.api.editor.layer.Layer;
import re.koda.easyarmorstands.api.property.PropertyContainer;
import re.koda.easyarmorstands.api.util.BoundingBox;
import re.koda.easyarmorstands.editor.EntityPositionProvider;
import re.koda.easyarmorstands.editor.OffsetProvider;
import re.koda.easyarmorstands.editor.display.BlockDisplayOffsetProvider;
import re.koda.easyarmorstands.editor.display.DisplayOffsetProvider;
import re.koda.easyarmorstands.editor.display.DisplayRotationProvider;
import re.koda.easyarmorstands.editor.display.layer.DisplayRootLayer;
import re.koda.easyarmorstands.platform.entity.BlockDisplay;
import re.koda.easyarmorstands.platform.entity.Display;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3dc;

public class DisplayElement<T extends Display> extends SimpleEntityElement<T> {
    private final T entity;

    public DisplayElement(EasyArmorStandsCommon eas, T entity, SimpleEntityElementType<T> type) {
        super(eas, entity, type);
        this.entity = entity;
    }

    @Override
    public @NotNull Button createButton(@NotNull Session session) {
        PropertyContainer properties = getProperties();
        return new BoundingBoxButton(session, this,
                new EntityPositionProvider(properties, getOffsetProvider(properties)),
                new DisplayRotationProvider(properties));
    }

    @Override
    public @NotNull Layer createLayer(@NotNull Session session) {
        return new DisplayRootLayer(eas, session, this);
    }

    @Override
    public @NotNull DisplayToolProvider getTools(@NotNull PropertyContainer properties) {
        return new DisplayToolProvider(eas, properties, getOffsetProvider(properties));
    }

    public DisplayShearToolProvider getShearTools(@NotNull PropertyContainer properties) {
        return new DisplayShearToolProvider(properties);
    }

    private OffsetProvider getOffsetProvider(PropertyContainer properties) {
        if (entity instanceof BlockDisplay && eas.getConfiguration().editor.centeredPivot) {
            return new BlockDisplayOffsetProvider(properties);
        } else {
            return new DisplayOffsetProvider(properties);
        }
    }

    @Override
    public @NotNull BoundingBox getBoundingBox() {
        Vector3dc position = entity.location().position();
        double width = entity.getDisplayWidth();
        double height = entity.getDisplayHeight();
        return BoundingBox.of(position, width, height);
    }
}
