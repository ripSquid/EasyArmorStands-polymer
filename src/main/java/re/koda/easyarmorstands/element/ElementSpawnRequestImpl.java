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
package re.koda.easyarmorstands.element;

import re.koda.easyarmorstands.EasyArmorStandsCommon;
import re.koda.easyarmorstands.api.editor.Session;
import re.koda.easyarmorstands.api.element.Element;
import re.koda.easyarmorstands.api.element.ElementSpawnRequest;
import re.koda.easyarmorstands.api.element.ElementType;
import re.koda.easyarmorstands.api.property.PropertyMap;
import re.koda.easyarmorstands.api.property.type.EntityPropertyTypes;
import re.koda.easyarmorstands.command.sender.EasPlayer;
import re.koda.easyarmorstands.history.action.ElementCreateAction;
import re.koda.easyarmorstands.platform.entity.Player;
import re.koda.easyarmorstands.platform.util.Location;
import re.koda.easyarmorstands.util.EasMath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

public class ElementSpawnRequestImpl implements ElementSpawnRequest {
    private final EasyArmorStandsCommon eas;
    private final PropertyMap properties = new PropertyMap();
    private final ElementType type;
    private EasPlayer player;

    public ElementSpawnRequestImpl(EasyArmorStandsCommon eas, ElementType type) {
        this.eas = eas;
        this.type = type;
    }

    @Override
    public @NotNull ElementType getType() {
        return type;
    }

    @Override
    public @Nullable Player getPlayer() {
        return player != null ? player.get() : null;
    }

    @Override
    public void setPlayer(@Nullable Player player) {
        this.player = player != null ? new EasPlayer(eas, player) : null;
    }

    @Override
    public @NotNull PropertyMap getProperties() {
        return properties;
    }

    @Override
    public @Nullable Element spawn() {
        PropertyMap properties = new PropertyMap();
        if (player != null) {
            Location originLocation;
            if (type.isSpawnedAtEyeHeight()) {
                originLocation = player.get().eyeLocation();
            } else {
                originLocation = player.get().location();
            }
            Vector3d offset = EasMath.getDirection(originLocation).mul(2);
            if (!player.get().isFlying()) {
                offset.y = 0;
            }
            Location location = originLocation
                    .withPosition(originLocation.position().add(offset, new Vector3d()))
                    .withYaw(originLocation.yaw() + 180);
            Session session = player.session();
            if (session != null) {
                Vector3d position = new Vector3d(location.position());
                session.snapper().snapPosition(position);
                location = location
                        .withPosition(position)
                        .withYaw((float) session.snapper().snapAngle(location.yaw()))
                        .withPitch((float) session.snapper().snapAngle(location.pitch()));
            }
            properties.put(EntityPropertyTypes.LOCATION, location);
        }
        type.applyDefaultProperties(properties);
        properties.putAll(this.properties);

        if (player != null && !player.canCreateElement(type, properties)) {
            return null;
        }

        Element element = type.createElement(properties);
        if (element == null) {
            return null;
        }

        if (player != null) {
            player.history().push(new ElementCreateAction(eas, element));
            player.clipboard().handleAutoApply(element);
        }
        return element;
    }
}
