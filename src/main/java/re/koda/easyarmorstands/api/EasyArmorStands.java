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
package re.koda.easyarmorstands.api;

import re.koda.easyarmorstands.api.editor.SessionManager;
import re.koda.easyarmorstands.api.element.Element;
import re.koda.easyarmorstands.api.element.ElementSpawnRequest;
import re.koda.easyarmorstands.api.element.ElementType;
import re.koda.easyarmorstands.api.element.ElementTypeRegistry;
import re.koda.easyarmorstands.api.element.EntityElementProvider;
import re.koda.easyarmorstands.api.element.EntityElementProviderRegistry;
import re.koda.easyarmorstands.api.property.type.PropertyTypeRegistry;
import re.koda.easyarmorstands.platform.Platform;
import re.koda.easyarmorstands.platform.entity.Entity;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface EasyArmorStands {
    String NAMESPACE = "easyarmorstands";

    static @NotNull Key key(@KeyPattern.Value String value) {
        return Key.key(NAMESPACE, value);
    }

    @Contract(pure = true)
    @NotNull Platform platform();

    @Contract(pure = true)
    @NotNull ElementTypeRegistry elementTypeRegistry();

    @Contract(pure = true)
    @NotNull PropertyTypeRegistry propertyTypeRegistry();

    @Contract(pure = true)
    @NotNull EntityElementProviderRegistry entityElementProviderRegistry();

    @Contract(pure = true)
    @NotNull SessionManager sessionManager();

    @Contract(pure = true)
    @NotNull ElementSpawnRequest elementSpawnRequest(ElementType type);

    /**
     * Attempts to resolve the entity into an element.
     *
     * @param entity the entity to resolve
     * @return the element or {@code null}
     */
    @Nullable Element getElement(Entity entity);

    /**
     * Stores the key of the entity element provider inside the entity.
     * <p>
     * The provider will automatically be used when {@link #getElement resolving the entity into an element}.
     *
     * @param entity   the entity
     * @param provider the provider
     */
    void setEntityElementProvider(Entity entity, @Nullable EntityElementProvider provider);

    void refreshEntity(Entity entity);
}
