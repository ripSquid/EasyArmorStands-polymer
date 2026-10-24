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
package re.koda.easyarmorstands.editor.display.layer;

import re.koda.easyarmorstands.EasyArmorStandsCommon;
import re.koda.easyarmorstands.api.editor.Session;
import re.koda.easyarmorstands.api.editor.context.ClickContext;
import re.koda.easyarmorstands.api.editor.context.UpdateContext;
import re.koda.easyarmorstands.api.editor.layer.ElementLayer;
import re.koda.easyarmorstands.api.editor.layer.ResettableLayer;
import re.koda.easyarmorstands.api.element.Element;
import re.koda.easyarmorstands.api.property.Property;
import re.koda.easyarmorstands.api.property.type.BlockDisplayPropertyTypes;
import re.koda.easyarmorstands.api.property.type.DisplayPropertyTypes;
import re.koda.easyarmorstands.api.property.type.EntityPropertyTypes;
import re.koda.easyarmorstands.command.sender.EasPlayer;
import re.koda.easyarmorstands.editor.input.OpenElementMenuInput;
import re.koda.easyarmorstands.editor.input.ReturnInput;
import re.koda.easyarmorstands.element.DisplayElement;
import re.koda.easyarmorstands.message.Message;
import re.koda.easyarmorstands.permission.Permissions;
import re.koda.easyarmorstands.platform.block.Block;
import re.koda.easyarmorstands.platform.block.BlockData;
import re.koda.easyarmorstands.platform.entity.Player;
import re.koda.easyarmorstands.platform.util.Location;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class DisplayRootLayer extends DisplayLayer implements ElementLayer, ResettableLayer {
    private final EasyArmorStandsCommon eas;
    private final Session session;
    private final DisplayElement<?> element;
    private final Property<Location> locationProperty;
    private final Property<BlockData> blockDataProperty;
    private final boolean allowMenu;

    public DisplayRootLayer(EasyArmorStandsCommon eas, Session session, DisplayElement<?> element) {
        super(session, session.properties(element));
        this.eas = eas;
        this.session = session;
        this.element = element;
        this.locationProperty = properties().get(EntityPropertyTypes.LOCATION);
        this.blockDataProperty = properties().getOrNull(BlockDisplayPropertyTypes.BLOCK);
        this.allowMenu = session.player().hasPermission(Permissions.OPEN);
        addNode(session.nodeProvider().tools(element.getTools(properties())));
    }

    @Override
    public boolean onClick(@NotNull ClickContext context) {
        if (super.onClick(context)) {
            return true;
        }
        Player player = session.player();
        if (blockDataProperty != null && context.type() == ClickContext.Type.LEFT_CLICK && player.isSneaking()) {
            Block block = context.block();
            if (block != null) {
                BlockData blockData = block.getBlockData();
                if (blockDataProperty.setValue(blockData)) {
                    properties().commit();
                    new EasPlayer(eas, player).sendMessage(Message.success("easyarmorstands.success.changed-block",
                            blockDataProperty.getType().getValueComponent(blockData)));
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onUpdate(@NotNull UpdateContext context) {
        if (allowMenu) {
            context.addInput(new OpenElementMenuInput(session, element));
        }
        super.onUpdate(context);
        context.addInput(new ReturnInput(session));
    }

    @Override
    public @NotNull Element getElement() {
        return element;
    }

    @Override
    public void reset() {
        properties().get(DisplayPropertyTypes.TRANSLATION).setValue(new Vector3f());
        properties().get(DisplayPropertyTypes.LEFT_ROTATION).setValue(new Quaternionf());
        properties().get(DisplayPropertyTypes.SCALE).setValue(new Vector3f(1));
        properties().get(DisplayPropertyTypes.RIGHT_ROTATION).setValue(new Quaternionf());

        locationProperty.setValue(locationProperty.getValue()
                .withYaw(0)
                .withPitch(0));

        properties().commit();
    }
}
