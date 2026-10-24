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

import re.koda.easyarmorstands.EasyArmorStandsCommon;
import re.koda.easyarmorstands.api.property.Property;
import re.koda.easyarmorstands.clipboard.Clipboard;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.List;

public class GravityHandler extends BooleanHandler {
    private final Property<Boolean> canTickProperty;

    public GravityHandler(EasyArmorStandsCommon eas, Property<Boolean> property, Property<Boolean> canTickProperty) {
        super(eas, property);
        this.canTickProperty = canTickProperty;
    }

    private boolean isMisconfigured() {
        // gravity enabled but ticking disabled
        return property.getValue() && !canTickProperty.getValue();
    }

    @Override
    protected boolean toggleNextValue() {
        if (isMisconfigured()) {
            return canTickProperty.setValue(true);
        }
        return super.toggleNextValue();
    }

    @Override
    protected boolean togglePreviousValue() {
        return super.toggleNextValue();
    }

    @Override
    public void modifyDescription(List<Component> description) {
        if (isMisconfigured()) {
            description.add(Component.translatable("easyarmorstands.property.gravity.can-tick-warning", NamedTextColor.YELLOW));
        }
    }

    @Override
    protected void handlePropertyShiftClick(Clipboard clipboard) {
        super.handlePropertyShiftClick(clipboard);
        clipboard.handlePropertyShiftClick(canTickProperty);
    }
}
