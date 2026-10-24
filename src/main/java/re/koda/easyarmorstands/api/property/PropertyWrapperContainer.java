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
package re.koda.easyarmorstands.api.property;

import re.koda.easyarmorstands.api.property.type.PropertyType;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public abstract class PropertyWrapperContainer implements PropertyContainer {
    private final PropertyContainer container;

    protected PropertyWrapperContainer(@NotNull PropertyContainer container) {
        this.container = container;
    }

    protected abstract <T> @NotNull Property<T> wrap(@NotNull Property<T> property);

    private <T> @Nullable Property<T> wrapOrNull(@Nullable Property<T> property) {
        if (property != null) {
            return wrap(property);
        } else {
            return null;
        }
    }

    @Override
    public void forEach(@NotNull Consumer<@NotNull Property<?>> consumer) {
        container.forEach(p -> consumer.accept(wrap(p)));
    }

    @Override
    public @Nullable <T> Property<T> getOrNull(@NotNull PropertyType<T> type) {
        return wrapOrNull(container.getOrNull(type));
    }

    @Override
    public @NotNull <T> Property<T> get(@NotNull PropertyType<T> type) {
        return wrap(container.get(type));
    }

    @Override
    public boolean isValid() {
        return container.isValid();
    }

    @Override
    public void commit(@Nullable Component description) {
        container.commit(description);
    }
}
