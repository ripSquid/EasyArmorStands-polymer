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

import re.koda.easyarmorstands.api.editor.Session;
import re.koda.easyarmorstands.api.editor.context.UpdateContext;
import re.koda.easyarmorstands.api.editor.layer.ResettableLayer;
import re.koda.easyarmorstands.api.property.Property;
import re.koda.easyarmorstands.api.property.PropertyContainer;
import re.koda.easyarmorstands.api.property.type.DisplayPropertyTypes;
import re.koda.easyarmorstands.editor.input.ReturnInput;
import re.koda.easyarmorstands.element.DisplayElement;
import re.koda.easyarmorstands.element.DisplayShearToolProvider;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;

public class DisplayShearLayer extends DisplayLayer implements ResettableLayer {
    private final Session session;
    private final Component name;
    private final Property<Quaternionfc> rightRotationProperty;

    public DisplayShearLayer(Session session, PropertyContainer properties, DisplayElement<?> element) {
        super(session, properties);
        this.session = session;
        this.name = DisplayPropertyTypes.RIGHT_ROTATION.getName().color(NamedTextColor.GOLD);
        this.rightRotationProperty = properties.get(DisplayPropertyTypes.RIGHT_ROTATION);
        DisplayShearToolProvider tools = element.getShearTools(properties);
        addNode(session.nodeProvider().tools(tools));
    }

    @Override
    public void onUpdate(@NotNull UpdateContext context) {
        super.onUpdate(context);
        context.setActionBar(name);
        context.addInput(new ReturnInput(session));
    }

    @Override
    public void reset() {
        rightRotationProperty.setValue(new Quaternionf());
    }
}
