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
package re.koda.easyarmorstands.group.layer;

import re.koda.easyarmorstands.api.editor.Session;
import re.koda.easyarmorstands.api.editor.context.AddContext;
import re.koda.easyarmorstands.api.editor.context.EnterContext;
import re.koda.easyarmorstands.api.editor.context.ExitContext;
import re.koda.easyarmorstands.api.editor.context.RemoveContext;
import re.koda.easyarmorstands.api.editor.context.UpdateContext;
import re.koda.easyarmorstands.api.editor.layer.AbstractLayer;
import re.koda.easyarmorstands.api.particle.BoundingBoxParticle;
import re.koda.easyarmorstands.api.particle.ParticleColor;
import re.koda.easyarmorstands.api.util.BoundingBox;
import re.koda.easyarmorstands.editor.input.ReturnInput;
import re.koda.easyarmorstands.group.Group;
import re.koda.easyarmorstands.group.GroupMember;
import re.koda.easyarmorstands.group.tool.GroupToolProvider;
import org.jetbrains.annotations.NotNull;

public class GroupRootLayer extends AbstractLayer {
    private final Session session;
    private final Group group;
    private final BoundingBoxParticle boxParticle;

    public GroupRootLayer(Group group) {
        super(group.getSession());
        this.session = group.getSession();
        this.group = group;
        this.boxParticle = session.particleProvider().createAxisAlignedBox();
        this.boxParticle.setColor(ParticleColor.GRAY);
        addNode(session.nodeProvider().tools(new GroupToolProvider(group)));
    }

    private void updateBox() {
        BoundingBox box = null;
        for (GroupMember member : group.getMembers()) {
            BoundingBox boundingBox = member.getBoundingBox();
            if (box == null) {
                box = boundingBox;
            } else {
                box = BoundingBox.of(box, boundingBox);
            }
        }
        if (box != null) {
            boxParticle.setBoundingBox(box);
        }
    }

    @Override
    public void onAdd(@NotNull AddContext context) {
        super.onAdd(context);
        updateBox();
        session.addParticle(boxParticle);
    }

    @Override
    public void onEnter(@NotNull EnterContext context) {
        super.onEnter(context);
        boxParticle.setColor(ParticleColor.WHITE);
    }

    @Override
    public void onUpdate(@NotNull UpdateContext context) {
        super.onUpdate(context);
        group.update();
        updateBox();
        context.addInput(new ReturnInput(session));
    }

    @Override
    public void onExit(@NotNull ExitContext context) {
        boxParticle.setColor(ParticleColor.GRAY);
        super.onExit(context);
    }

    @Override
    public void onInactiveUpdate(@NotNull UpdateContext context) {
        super.onInactiveUpdate(context);
        group.update();
        updateBox();
    }

    @Override
    public void onRemove(@NotNull RemoveContext context) {
        session.removeParticle(boxParticle);
        super.onRemove(context);
    }

    @Override
    public boolean isValid() {
        return group.isValid();
    }

    public Group getGroup() {
        return group;
    }
}
