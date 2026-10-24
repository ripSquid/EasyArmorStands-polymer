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
package re.koda.easyarmorstands.editor.layer;

import re.koda.easyarmorstands.api.editor.Session;
import re.koda.easyarmorstands.api.editor.context.UpdateContext;
import re.koda.easyarmorstands.api.editor.layer.AbstractLayer;
import re.koda.easyarmorstands.api.editor.layer.ElementLayer;
import re.koda.easyarmorstands.api.element.EditableElement;
import re.koda.easyarmorstands.api.element.Element;
import re.koda.easyarmorstands.api.element.MenuElement;
import re.koda.easyarmorstands.editor.input.OpenElementMenuInput;
import re.koda.easyarmorstands.editor.input.ReturnInput;
import re.koda.easyarmorstands.permission.Permissions;
import org.jetbrains.annotations.NotNull;

public class SimpleEntityLayer extends AbstractLayer implements ElementLayer {
    private final Session session;
    private final Element element;
    private final boolean allowMenu;

    public SimpleEntityLayer(Session session, EditableElement element) {
        super(session);
        this.session = session;
        this.element = element;
        this.allowMenu = session.player().hasPermission(Permissions.OPEN);
        addNode(session.nodeProvider().tools(element.getTools(session.properties(element))));
    }

    @Override
    public void onUpdate(@NotNull UpdateContext context) {
        if (allowMenu && element instanceof MenuElement) {
            context.addInput(new OpenElementMenuInput(session, (MenuElement) element));
        }
        super.onUpdate(context);
        context.addInput(new ReturnInput(session));
    }

    @Override
    public boolean isValid() {
        return element.isValid();
    }

    @Override
    public @NotNull Element getElement() {
        return element;
    }
}
