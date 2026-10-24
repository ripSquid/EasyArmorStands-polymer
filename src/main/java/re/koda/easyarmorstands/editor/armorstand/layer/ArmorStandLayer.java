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
package re.koda.easyarmorstands.editor.armorstand.layer;

import re.koda.easyarmorstands.api.editor.Session;
import re.koda.easyarmorstands.api.editor.context.UpdateContext;
import re.koda.easyarmorstands.api.editor.layer.ElementLayer;
import re.koda.easyarmorstands.api.property.PropertyContainer;
import re.koda.easyarmorstands.editor.input.OpenElementMenuInput;
import re.koda.easyarmorstands.editor.layer.PropertyLayer;
import re.koda.easyarmorstands.element.ArmorStandElement;
import re.koda.easyarmorstands.permission.Permissions;
import org.jetbrains.annotations.NotNull;

public class ArmorStandLayer extends PropertyLayer implements ElementLayer {
    private final Session session;
    private final ArmorStandElement element;
    private final boolean allowMenu;

    public ArmorStandLayer(Session session, PropertyContainer properties, ArmorStandElement element) {
        super(session, properties);
        this.session = session;
        this.element = element;
        this.allowMenu = session.player().hasPermission(Permissions.OPEN);
    }

    @Override
    public void onUpdate(@NotNull UpdateContext context) {
        super.onUpdate(context);
        if (allowMenu) {
            context.addInput(new OpenElementMenuInput(session, element));
        }
    }

    @Override
    public @NotNull ArmorStandElement getElement() {
        return element;
    }
}
