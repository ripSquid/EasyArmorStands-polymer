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
import re.koda.easyarmorstands.api.editor.tool.ToolProvider;
import re.koda.easyarmorstands.api.element.ConfigurableEntityElement;
import re.koda.easyarmorstands.api.element.DestroyableElement;
import re.koda.easyarmorstands.api.element.EditableElement;
import re.koda.easyarmorstands.api.element.MenuElement;
import re.koda.easyarmorstands.api.element.SelectableElement;
import re.koda.easyarmorstands.api.property.PropertyContainer;
import re.koda.easyarmorstands.api.property.PropertyRegistry;
import re.koda.easyarmorstands.api.util.BoundingBox;
import re.koda.easyarmorstands.api.util.RotationProvider;
import re.koda.easyarmorstands.editor.EntityPositionProvider;
import re.koda.easyarmorstands.editor.layer.SimpleEntityLayer;
import re.koda.easyarmorstands.permission.Permissions;
import re.koda.easyarmorstands.platform.entity.Entity;
import re.koda.easyarmorstands.platform.entity.LivingEntity;
import re.koda.easyarmorstands.platform.entity.Player;
import re.koda.easyarmorstands.util.Util;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3dc;

import java.util.Objects;

public class SimpleEntityElement<E extends Entity> implements ConfigurableEntityElement<E>, SelectableElement, MenuElement, DestroyableElement, EditableElement {
    protected final EasyArmorStandsCommon eas;
    private final E entity;
    private final SimpleEntityElementType<E> type;
    private final PropertyRegistry properties = PropertyRegistry.create(this);

    public SimpleEntityElement(EasyArmorStandsCommon eas, E entity, SimpleEntityElementType<E> type) {
        this.eas = eas;
        this.entity = entity;
        this.type = type;
    }

    @Override
    public @NotNull E getEntity() {
        return entity;
    }

    @Override
    public @NotNull SimpleEntityElementType<E> getType() {
        return type;
    }

    @Override
    public boolean isValid() {
        return entity.isValid();
    }

    @Override
    public @NotNull PropertyRegistry getProperties() {
        return properties;
    }

    @Override
    public @NotNull ToolProvider getTools(@NotNull PropertyContainer properties) {
        return new SimpleEntityToolProvider(eas, properties);
    }

    @Override
    public @NotNull BoundingBox getBoundingBox() {
        Vector3dc position = entity.location().position();
        double width = entity.width();
        double height = entity.height();
        return BoundingBox.of(position, width, height);
    }

    public double getScale() {
        if (entity instanceof LivingEntity livingEntity) {
            if (livingEntity.hasScaleAttribute()) {
                return livingEntity.getScaleAttribute();
            }
        }
        return 1;
    }

    @Override
    public @NotNull Button createButton(@NotNull Session session) {
        return new BoundingBoxButton(session, this,
                new EntityPositionProvider(properties),
                RotationProvider.identity());
    }

    @Override
    public @NotNull Layer createLayer(@NotNull Session session) {
        return new SimpleEntityLayer(session, this);
    }

    @Override
    public void openMenu(@NotNull Player player) {
        eas.openElementMenu(player, this);
    }

    @Override
    public void destroy() {
        entity.remove();
    }

    @Override
    public boolean canEdit(@NotNull Player player) {
        return player.hasPermission(Permissions.entityType(Permissions.EDIT, type.getEntityType()));
    }

    @Override
    public boolean canDestroy(@NotNull Player player) {
        if (entity instanceof Player) {
            return false;
        }
        return player.hasPermission(Permissions.entityType(Permissions.DESTROY, type.getEntityType()));
    }

    @Override
    public @NotNull Component getName() {
        return Component.text(Util.getId(entity.uniqueId()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleEntityElement<?> that = (SimpleEntityElement<?>) o;
        return Objects.equals(entity, that.entity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entity);
    }
}
