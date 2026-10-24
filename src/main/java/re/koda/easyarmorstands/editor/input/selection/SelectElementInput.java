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
package re.koda.easyarmorstands.editor.input.selection;

import re.koda.easyarmorstands.EasyArmorStandsCommon;
import re.koda.easyarmorstands.api.editor.Session;
import re.koda.easyarmorstands.api.editor.context.ClickContext;
import re.koda.easyarmorstands.api.element.SelectableElement;
import re.koda.easyarmorstands.command.sender.EasPlayer;
import re.koda.easyarmorstands.editor.input.SelectLayerInput;
import re.koda.easyarmorstands.editor.layer.LayerFactory;
import org.jetbrains.annotations.NotNull;

public class SelectElementInput extends SelectLayerInput {
    private final EasyArmorStandsCommon eas;
    private final Session session;
    private final SelectableElement element;

    public SelectElementInput(EasyArmorStandsCommon eas, Session session, SelectableElement element, LayerFactory layerFactory) {
        super(session, layerFactory, null);
        this.eas = eas;
        this.session = session;
        this.element = element;
    }

    @Override
    public void execute(@NotNull ClickContext context) {
        EasPlayer player = new EasPlayer(eas, session.player());
        if (!player.canEditElement(element)) {
            return;
        }
        super.execute(context);
    }
}
