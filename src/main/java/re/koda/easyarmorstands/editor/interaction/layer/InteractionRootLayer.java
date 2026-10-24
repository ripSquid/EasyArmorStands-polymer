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
package re.koda.easyarmorstands.editor.interaction.layer;

import re.koda.easyarmorstands.api.editor.Session;
import re.koda.easyarmorstands.api.editor.context.AddContext;
import re.koda.easyarmorstands.api.editor.context.EnterContext;
import re.koda.easyarmorstands.api.editor.context.ExitContext;
import re.koda.easyarmorstands.api.editor.context.RemoveContext;
import re.koda.easyarmorstands.api.editor.context.UpdateContext;
import re.koda.easyarmorstands.api.editor.layer.ElementLayer;
import re.koda.easyarmorstands.api.particle.BoundingBoxParticle;
import re.koda.easyarmorstands.api.particle.ParticleColor;
import re.koda.easyarmorstands.api.property.Property;
import re.koda.easyarmorstands.api.property.type.DisplayPropertyTypes;
import re.koda.easyarmorstands.api.property.type.EntityPropertyTypes;
import re.koda.easyarmorstands.api.util.BoundingBox;
import re.koda.easyarmorstands.editor.input.OpenElementMenuInput;
import re.koda.easyarmorstands.editor.input.ReturnInput;
import re.koda.easyarmorstands.editor.interaction.InteractionBoxEditor;
import re.koda.easyarmorstands.editor.layer.BoxResizeToolManager;
import re.koda.easyarmorstands.editor.layer.PropertyLayer;
import re.koda.easyarmorstands.element.InteractionElement;
import re.koda.easyarmorstands.permission.Permissions;
import re.koda.easyarmorstands.platform.util.Location;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3dc;

public class InteractionRootLayer extends PropertyLayer implements ElementLayer {
    private final Session session;
    private final InteractionElement element;
    private final BoundingBoxParticle boxParticle;
    private final Property<Location> locationProperty;
    private final Property<Float> widthProperty;
    private final Property<Float> heightProperty;
    private final boolean allowMenu;

    public InteractionRootLayer(Session session, InteractionElement element) {
        super(session, session.properties(element));
        this.session = session;
        this.element = element;
        this.boxParticle = session.particleProvider().createAxisAlignedBox();
        this.locationProperty = properties().get(EntityPropertyTypes.LOCATION);
        this.widthProperty = properties().get(DisplayPropertyTypes.BOX_WIDTH);
        this.heightProperty = properties().get(DisplayPropertyTypes.BOX_HEIGHT);
        this.allowMenu = session.player().hasPermission(Permissions.OPEN);
        addNode(session.nodeProvider().tools(element.getTools(properties())));
        new BoxResizeToolManager(session, this, new InteractionBoxEditor(properties()));
    }

    @Override
    public void onAdd(@NotNull AddContext context) {
        boxParticle.setColor(ParticleColor.GRAY);
        updateBoundingBox();
        session.addParticle(boxParticle);
    }

    @Override
    public void onRemove(@NotNull RemoveContext context) {
        session.removeParticle(boxParticle);
    }

    @Override
    public void onEnter(@NotNull EnterContext context) {
        boxParticle.setColor(ParticleColor.WHITE);
        updateBoundingBox();
        super.onEnter(context);
    }

    @Override
    public void onExit(@NotNull ExitContext context) {
        boxParticle.setColor(ParticleColor.GRAY);
        updateBoundingBox();
        super.onExit(context);
    }

    @Override
    public void onUpdate(@NotNull UpdateContext context) {
        updateBoundingBox();
        super.onUpdate(context);
        if (allowMenu) {
            context.addInput(new OpenElementMenuInput(session, element));
        }
        context.addInput(new ReturnInput(session));
    }

    @Override
    public void onInactiveUpdate(@NotNull UpdateContext context) {
        updateBoundingBox();
        super.onInactiveUpdate(context);
    }

    private void updateBoundingBox() {
        float width = widthProperty.getValue();
        float height = heightProperty.getValue();
        Location location = locationProperty.getValue();
        Vector3dc position = location.position();
        boxParticle.setBoundingBox(BoundingBox.of(position, width, height));
    }

    @Override
    public @NotNull InteractionElement getElement() {
        return element;
    }
}
