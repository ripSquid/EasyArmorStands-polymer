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
package re.koda.easyarmorstands.command.processor;

import re.koda.easyarmorstands.EasyArmorStandsHolder;
import re.koda.easyarmorstands.api.property.type.PropertyType;
import re.koda.easyarmorstands.command.sender.EasCommandSender;
import re.koda.easyarmorstands.command.sender.EasPlayer;
import re.koda.easyarmorstands.permission.Permissions;
import net.kyori.adventure.key.Key;
import org.incendo.cloud.key.CloudKey;
import org.incendo.cloud.permission.Permission;
import org.incendo.cloud.permission.PredicatePermission;
import org.intellij.lang.annotations.Subst;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;
import java.util.function.Predicate;

@NullMarked
public class PropertyPermissionPredicate implements Predicate<EasCommandSender> {
    private final @Nullable EasyArmorStandsHolder holder;
    private final Key key;
    private @Nullable PropertyType<?> type;

    private PropertyPermissionPredicate(@Nullable EasyArmorStandsHolder holder, Key key, @Nullable PropertyType<?> type) {
        this.holder = holder;
        this.key = key;
        this.type = type;
    }

    public static Permission createPermission(EasyArmorStandsHolder holder, @Subst("n:v") String value) {
        return createPermission(holder, Key.key(value));
    }

    public static Permission createPermission(PropertyType<?> type) {
        return createPermission(null, type.key(), type);
    }

    public static Permission createPermission(EasyArmorStandsHolder holder, Key key) {
        return createPermission(holder, key, null);
    }

    private static Permission createPermission(@Nullable EasyArmorStandsHolder holder, Key key, @Nullable PropertyType<?> type) {
        return PredicatePermission.of(
                CloudKey.of("property_permission_" + key.asString()),
                new PropertyPermissionPredicate(holder, key, type));
    }

    @Override
    public boolean test(EasCommandSender sender) {
        if (type == null) {
            type = Objects.requireNonNull(holder).get().propertyTypeRegistry().get(key);
        }
        if (sender instanceof EasPlayer player) {
            return player.get().hasPermission(Permissions.EDIT) && type.canChange(player.get());
        }
        return false;
    }
}
