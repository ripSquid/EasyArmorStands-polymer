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
package re.koda.easyarmorstands.property.display.text;

import re.koda.easyarmorstands.api.property.Property;
import re.koda.easyarmorstands.api.property.type.PropertyType;
import re.koda.easyarmorstands.api.property.type.TextDisplayPropertyTypes;
import re.koda.easyarmorstands.platform.color.ARGBColor;
import re.koda.easyarmorstands.platform.entity.TextDisplay;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class TextDisplayBackgroundProperty implements Property<Optional<ARGBColor>> {
    private static final int DEFAULT_COLOR = 0x40000000;
    private final TextDisplay entity;

    public TextDisplayBackgroundProperty(TextDisplay entity) {
        this.entity = entity;
    }

    @Override
    public @NotNull PropertyType<Optional<ARGBColor>> getType() {
        return TextDisplayPropertyTypes.BACKGROUND;
    }

    @Override
    public @NotNull Optional<ARGBColor> getValue() {
        if (entity.isDefaultBackground()) {
            return Optional.empty();
        }
        ARGBColor color = entity.getBackgroundColor();
        if (color == null) {
            color = ARGBColor.WHITE;
        } else if (color.value() == DEFAULT_COLOR) {
            return Optional.empty();
        }
        return Optional.of(color);
    }

    @Override
    public boolean setValue(@NotNull Optional<ARGBColor> value) {
        entity.setDefaultBackground(value.isEmpty());
        entity.setBackgroundColor(value.orElse(null));
        return true;
    }
}
