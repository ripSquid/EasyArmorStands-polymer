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
package re.koda.easyarmorstands.group;

import re.koda.easyarmorstands.api.editor.Session;
import re.koda.easyarmorstands.api.editor.tool.ToolProvider;
import re.koda.easyarmorstands.api.element.EditableElement;
import re.koda.easyarmorstands.api.util.BoundingBox;

import java.util.Objects;

public class GroupMember {
    private final Session session;
    private final EditableElement element;
    private final ToolProvider tools;

    public GroupMember(Session session, EditableElement element) {
        this.session = session;
        this.element = element;
        this.tools = element.getTools(session.properties(element));
    }

    public EditableElement getElement() {
        return element;
    }

    public ToolProvider getTools() {
        return tools;
    }

    public BoundingBox getBoundingBox() {
        return element.getBoundingBox();
    }

    public boolean isValid() {
        return element.isValid();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupMember that = (GroupMember) o;
        return Objects.equals(session, that.session) && Objects.equals(element, that.element);
    }

    @Override
    public int hashCode() {
        return Objects.hash(session, element);
    }
}
