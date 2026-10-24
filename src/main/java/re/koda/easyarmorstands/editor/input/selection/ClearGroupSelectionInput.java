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

import re.koda.easyarmorstands.api.editor.context.ClickContext;
import re.koda.easyarmorstands.api.editor.input.Input;
import re.koda.easyarmorstands.editor.layer.ElementSelectionLayerImpl;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;

public class ClearGroupSelectionInput implements Input {
    private static final Component NAME = Component.translatable("easyarmorstands.input.select.group.clear");
    private static final Style STYLE = Style.style(NamedTextColor.RED);
    private final ElementSelectionLayerImpl layer;

    public ClearGroupSelectionInput(ElementSelectionLayerImpl layer) {
        this.layer = layer;
    }

    @Override
    public @NotNull Component name() {
        return NAME;
    }

    @Override
    public @NotNull Style style() {
        return STYLE;
    }

    @Override
    public ClickContext.@NotNull Type clickType() {
        return ClickContext.Type.LEFT_CLICK;
    }

    @Override
    public void execute(@NotNull ClickContext context) {
        layer.clearGroupSelection();
    }
}
