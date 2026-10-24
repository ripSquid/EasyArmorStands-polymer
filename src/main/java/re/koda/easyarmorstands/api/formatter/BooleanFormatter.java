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
package re.koda.easyarmorstands.api.formatter;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public record BooleanFormatter(Component trueName, Component falseName) implements ValueFormatter<Boolean> {
    private static final BooleanFormatter TOGGLE = BooleanFormatter.translatable(
            "easyarmorstands.property.common.enabled",
            "easyarmorstands.property.common.disabled");
    private static final BooleanFormatter VISIBILITY = BooleanFormatter.translatable(
            "easyarmorstands.property.common.visible",
            "easyarmorstands.property.common.invisible");

    public static BooleanFormatter toggle() {
        return TOGGLE;
    }

    public static BooleanFormatter visibility() {
        return VISIBILITY;
    }

    public static BooleanFormatter translatable(String trueKey, String falseKey) {
        return new BooleanFormatter(
                Component.translatable(trueKey, NamedTextColor.GREEN),
                Component.translatable(falseKey, NamedTextColor.RED));
    }

    public BooleanFormatter inverted() {
        return new BooleanFormatter(falseName, trueName);
    }

    @Override
    public Component format(Boolean value) {
        return value ? trueName : falseName;
    }
}
