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
import re.koda.easyarmorstands.api.ArmorStandSize;
import re.koda.easyarmorstands.api.editor.Session;
import re.koda.easyarmorstands.api.editor.button.BoundingBoxButton;
import re.koda.easyarmorstands.api.editor.button.Button;
import re.koda.easyarmorstands.api.editor.layer.Layer;
import re.koda.easyarmorstands.api.editor.node.Node;
import re.koda.easyarmorstands.api.editor.tool.ToolProvider;
import re.koda.easyarmorstands.api.property.PropertyContainer;
import re.koda.easyarmorstands.api.property.PropertyRegistry;
import re.koda.easyarmorstands.api.util.BoundingBox;
import re.koda.easyarmorstands.editor.EntityPositionProvider;
import re.koda.easyarmorstands.editor.armorstand.ArmorStandOffsetProvider;
import re.koda.easyarmorstands.editor.armorstand.ArmorStandRotationProvider;
import re.koda.easyarmorstands.editor.armorstand.layer.ArmorStandRootLayer;
import re.koda.easyarmorstands.editor.node.ArmorStandNode;
import re.koda.easyarmorstands.platform.entity.ArmorStand;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3dc;

public class ArmorStandElement extends SimpleEntityElement<ArmorStand> {
    private final ArmorStand entity;

    public ArmorStandElement(EasyArmorStandsCommon eas, ArmorStand entity, SimpleEntityElementType<ArmorStand> type) {
        super(eas, entity, type);
        this.entity = entity;
    }

    @Override
    public @NotNull Button createButton(@NotNull Session session) {
        PropertyRegistry properties = getProperties();
        return new BoundingBoxButton(session, this,
                new EntityPositionProvider(properties, new ArmorStandOffsetProvider(this, properties)),
                new ArmorStandRotationProvider(properties));
    }

    @Override
    public @NotNull Node createNode(@NotNull Session session) {
        if (eas.getConfiguration().editor.flattenArmorStands) {
            return new ArmorStandNode(eas, session, this);
        }
        return super.createNode(session);
    }

    @Override
    public @NotNull Layer createLayer(@NotNull Session session) {
        return new ArmorStandRootLayer(eas, session, entity, this);
    }

    @Override
    public @NotNull ToolProvider getTools(@NotNull PropertyContainer properties) {
        return new ArmorStandToolProvider(eas, properties);
    }

    @Override
    public @NotNull BoundingBox getBoundingBox() {
        ArmorStandSize size = ArmorStandSize.get(entity);
        Vector3dc position = entity.location().position();
        double scale = getScale();
        double width = size.getWidth() * scale;
        double height = size.getHeight() * scale;
        return BoundingBox.of(position, width, height);
    }
}
