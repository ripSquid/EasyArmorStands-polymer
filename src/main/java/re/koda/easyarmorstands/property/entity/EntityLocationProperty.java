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

import re.koda.easyarmorstands.api.property.PendingChange;
import re.koda.easyarmorstands.api.property.Property;
import re.koda.easyarmorstands.api.property.type.EntityPropertyTypes;
import re.koda.easyarmorstands.api.property.type.PropertyType;
import re.koda.easyarmorstands.platform.entity.Entity;
import re.koda.easyarmorstands.platform.util.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3dc;

public class EntityLocationProperty implements Property<Location> {
    private static final double HEIGHT_LIMIT = 20000000;
    private static final double COORDINATE_LIMIT = 30000000;
    private final Entity entity;

    public EntityLocationProperty(Entity entity) {
        this.entity = entity;
    }

    @Override
    public @NotNull PropertyType<Location> getType() {
        return EntityPropertyTypes.LOCATION;
    }

    @Override
    public @NotNull Location getValue() {
        return entity.location();
    }

    @Override
    public @Nullable PendingChange prepareChange(@NotNull Location value) {
        if (!isValid(value)) {
            return null;
        }

        return Property.super.prepareChange(value);
    }

    @Override
    public boolean setValue(@NotNull Location value) {
        if (!isValid(value)) {
            return false;
        }

        boolean ok = entity.teleport(value);
        if (ok) {
            entity.setFallDistance(0);
        }
        return ok;
    }

    private boolean isValid(@NotNull Location location) {
        Vector3dc position = location.position();
        return isValid(position.x(), COORDINATE_LIMIT) &&
                isValid(position.y(), HEIGHT_LIMIT) &&
                isValid(position.z(), COORDINATE_LIMIT);
    }

    private boolean isValid(double value, double limit) {
        return isValid(value, -limit, limit);
    }

    private boolean isValid(double value, double min, double max) {
        return value >= min && value < max;
    }
}
