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
package re.koda.easyarmorstands.api.element;

import re.koda.easyarmorstands.api.editor.Session;
import re.koda.easyarmorstands.api.editor.button.Button;
import re.koda.easyarmorstands.api.editor.layer.Layer;
import re.koda.easyarmorstands.api.editor.node.Node;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * An element which can be selected in the editor.
 */
public interface SelectableElement extends EditableElement, NamedElement {
    /**
     * Creates a button which allows selecting the element.
     *
     * @param session the session
     * @return a button
     */
    @Contract(pure = true)
    @NotNull Button createButton(@NotNull Session session);

    /**
     * Creates a node which allows selecting the element.
     *
     * @param session the session
     * @return a node
     */
    @Contract(pure = true)
    default @NotNull Node createNode(@NotNull Session session) {
        return session.createElementNode(this, createButton(session));
    }

    /**
     * Creates a layer which allows editing the element.
     *
     * @param session the session
     * @return a layer
     */
    @Contract(pure = true)
    @NotNull Layer createLayer(@NotNull Session session);
}
