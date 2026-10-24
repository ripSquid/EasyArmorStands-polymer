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

import re.koda.easyarmorstands.api.editor.layer.ElementSelectionLayer;
import re.koda.easyarmorstands.api.util.BoundingBox;
import re.koda.easyarmorstands.platform.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * A source which can discover elements inside a bounding box.
 *
 * @see ElementSelectionLayer#addSource(ElementDiscoverySource) Registering sources
 */
public interface ElementDiscoverySource {
    /**
     * Returns entries for all potential elements inside the provided bounding box.
     * <p>
     * Should be as fast as possible and not perform any expensive checks.
     *
     * @param world    the queried world
     * @param box      the queried bounding box
     * @param consumer a consumer which should be called for each potential element
     */
    void discover(@NotNull World world, @NotNull BoundingBox box, @NotNull Consumer<@NotNull ElementDiscoveryEntry> consumer);
}
