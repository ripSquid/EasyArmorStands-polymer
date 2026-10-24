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
import re.koda.easyarmorstands.api.menu.click.MenuClickContext;
import re.koda.easyarmorstands.api.property.Property;
import re.koda.easyarmorstands.clipboard.Clipboard;
import re.koda.easyarmorstands.message.Message;

public abstract class ToggleHandler<T> implements ButtonHandler {
    private final EasyArmorStandsCommon eas;
    protected final Property<T> property;

    public ToggleHandler(EasyArmorStandsCommon eas, Property<T> property) {
        this.eas = eas;
        this.property = property;
    }

    @Override
    public void onClick(MenuClickContext context) {
        boolean changed;
        if (context.isShiftClick()) {
            handlePropertyShiftClick(eas.getClipboard(context.player()));
            return;
        } else if (context.isLeftClick()) {
            changed = toggleNextValue();
        } else if (context.isRightClick()) {
            changed = togglePreviousValue();
        } else {
            return;
        }
        if (changed) {
            property.commit();
        } else {
            context.player().sendMessage(Message.error("easyarmorstands.error.cannot-change"));
        }
    }

    protected void handlePropertyShiftClick(Clipboard clipboard) {
        clipboard.handlePropertyShiftClick(property);
    }

    protected abstract boolean toggleNextValue();

    protected boolean togglePreviousValue() {
        return toggleNextValue();
    }
}
