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
import re.koda.easyarmorstands.api.element.EditableElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import org.joml.Vector3d;
import org.joml.Vector3dc;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class Group {
    private final Session session;
    private final Set<GroupMember> members = new LinkedHashSet<>();

    public Group(Session session) {
        this.session = session;
    }

    public void addMember(EditableElement element) {
        addMember(new GroupMember(session, element));
    }

    public void addMember(GroupMember member) {
        members.add(member);
    }

    public void update() {
        members.removeIf(m -> !m.isValid());
    }

    public @UnmodifiableView @NotNull Set<GroupMember> getMembers() {
        return Collections.unmodifiableSet(members);
    }

    public @NotNull Session getSession() {
        return session;
    }

    public Vector3dc getAveragePosition() {
        Vector3d temp = new Vector3d();
        Vector3d position = new Vector3d();
        for (GroupMember member : members) {
            position.add(member.getBoundingBox().getCenter(temp));
        }
        position.div(members.size());
        return position;
    }

    public boolean isValid() {
        return !members.isEmpty();
    }
}
