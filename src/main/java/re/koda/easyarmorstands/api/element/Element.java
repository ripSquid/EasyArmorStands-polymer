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

import re.koda.easyarmorstands.api.property.PropertyContainer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Any object with properties.
 * <p>
 * Usually an {@link EntityElement entity}, but it could also be something else.
 */
public interface Element {
    /**
     * The type of this element.
     *
     * @return the type
     */
    @Contract(pure = true)
    @NotNull ElementType getType();

    /**
     * The properties of the element.
     * <p>
     * Changes to these properties will be performed immediately without any permission checks or history tracking.
     *
     * @return the properties of the element
     * @see re.koda.easyarmorstands.api.editor.Session#properties(Element) Permission checks and history tracking
     */
    @Contract(pure = true)
    @NotNull PropertyContainer getProperties();

    /**
     * A reference which can be used to retrieve the element later.
     * <p>
     * The reference should remain valid even after the chunk is unloaded and loaded or the element is deleted
     * and recreated.
     * It is stored in the history and used to find the element when undoing or redoing a change.
     *
     * @return a reference which can be used to retrieve the element
     */
    @Contract(pure = true)
    @NotNull ElementReference getReference(ReferenceProvider provider);

    @Contract(pure = true)
    boolean isValid();
}
