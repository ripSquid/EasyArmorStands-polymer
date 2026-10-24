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
package re.koda.easyarmorstands.property.button;

import re.koda.easyarmorstands.api.menu.button.MenuButton;
import re.koda.easyarmorstands.api.menu.button.MenuIcon;
import re.koda.easyarmorstands.api.property.Property;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PropertyButtonBuilder<T> {
    private final Property<T> property;
    private final List<Component> description = new ArrayList<>();
    private @Nullable MenuIcon icon;
    private @Nullable ButtonHandler handler;

    public PropertyButtonBuilder(Property<T> property) {
        this.property = property;
    }

    public PropertyButtonBuilder<T> handler(ButtonHandler handler) {
        this.handler = handler;
        return this;
    }

    public PropertyButtonBuilder<T> icon(MenuIcon icon) {
        this.icon = icon;
        return this;
    }

    public PropertyButtonBuilder<T> description(Component... description) {
        this.description.addAll(List.of(description));
        return this;
    }

    public MenuButton build() {
        Objects.requireNonNull(handler, "handler");
        Objects.requireNonNull(icon, "icon");
        return new PropertyButton<>(property, handler, icon, description);
    }
}
