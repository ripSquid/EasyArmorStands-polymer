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
package re.koda.easyarmorstands.api.element;

import re.koda.easyarmorstands.platform.entity.Entity;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A provider which can turn an entity into an element.
 *
 * @see EntityElementProviderRegistry
 */
public interface EntityElementProvider extends Keyed {
    /**
     * Returns whether {@link #getElement} should be called for the passed entity when attempting to find the responsible element provider.
     *
     * @param entity the entity
     * @return whether this provider is responsible for resolving the passed entity into an element
     */
    default boolean canDetect(@NotNull Entity entity) {
        return true;
    }

    /**
     * Attempts to create an element instance which represents the specified entity.
     * <p>
     * Returns {@code null} if this provider cannot create an element for the entity.
     * <p>
     * May be called even if {@link #canDetect} returns false, for example, if {@link re.koda.easyarmorstands.api.EasyArmorStands#setEntityElementProvider(Entity, EntityElementProvider) the entity references this provider}.
     *
     * @param entity the entity
     * @return the element instance or {@code null}
     */
    @Nullable Element getElement(@NotNull Entity entity);

    /**
     * Returns the priority of this provider.
     * <p>
     * Providers with a higher priority are called first, until a provider returns a non-{@code null} element.
     *
     * @return the priority
     */
    default @NotNull Priority getPriority() {
        return Priority.NORMAL;
    }

    /**
     * The priority of an element provider.
     */
    enum Priority {
        HIGHEST,
        HIGH,
        NORMAL,
        LOW,
        LOWEST
    }
}
