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
import re.koda.easyarmorstands.api.editor.context.ExitContext;
import re.koda.easyarmorstands.api.editor.context.UpdateContext;
import re.koda.easyarmorstands.api.editor.layer.Layer;
import re.koda.easyarmorstands.api.editor.tool.ToolSession;
import re.koda.easyarmorstands.editor.input.tool.AbortInput;
import re.koda.easyarmorstands.editor.input.tool.ConfirmInput;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.jetbrains.annotations.NotNull;

public abstract class ToolLayer implements Layer {
    private final Session session;
    private final ToolSession toolSession;
    private final Component name;

    public ToolLayer(Session session, ToolSession toolSession, Component name) {
        this.session = session;
        this.toolSession = toolSession;
        this.name = name;
    }

    @Override
    public void onExit(@NotNull ExitContext context) {
        toolSession.commit(toolSession.getDescription());
    }

    @Override
    public void onUpdate(@NotNull UpdateContext context) {
        updateActionBar(context);
        if (canConfirm()) {
            context.addInput(new ConfirmInput(session));
        }
        context.addInput(new AbortInput(session, toolSession));
    }

    protected void updateActionBar(@NotNull UpdateContext context) {
        TextComponent.Builder builder = Component.text();
        builder.append(name);
        Component state = toolSession.getStatus();
        if (state != null) {
            builder.append(Component.text(": "));
            builder.append(state);
        }
        context.setActionBar(builder);
    }

    protected boolean canConfirm() {
        return true;
    }

    @Override
    public boolean isValid() {
        return toolSession.isValid();
    }

    public @NotNull Component getName() {
        return name;
    }
}
