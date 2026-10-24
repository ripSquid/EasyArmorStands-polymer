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
package re.koda.easyarmorstands.property.entity;

import re.koda.easyarmorstands.api.property.Property;
import re.koda.easyarmorstands.api.property.type.EntityPropertyTypes;
import re.koda.easyarmorstands.api.property.type.PropertyType;
import re.koda.easyarmorstands.platform.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class EntityTagsProperty implements Property<Set<String>> {
    private final Entity entity;

    public EntityTagsProperty(Entity entity) {
        this.entity = entity;
    }

    @Override
    public @NotNull PropertyType<Set<String>> getType() {
        return EntityPropertyTypes.TAGS;
    }

    @Override
    public @NotNull Set<String> getValue() {
        return entity.getScoreboardTags();
    }

    @Override
    public boolean setValue(@NotNull Set<String> value) {
        Set<String> currentTags = new HashSet<>(entity.getScoreboardTags());
        boolean changed = false;

        // handle removed tags
        for (String tag : currentTags) {
            if (!value.contains(tag)) {
                if (entity.removeScoreboardTag(tag)) {
                    changed = true;
                }
            }
        }

        // handle added tags
        for (String tag : value) {
            if (!currentTags.contains(tag)) {
                if (entity.addScoreboardTag(tag)) {
                    changed = true;
                }
            }
        }

        return changed;
    }
}
