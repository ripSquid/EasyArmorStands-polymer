/*
 * EasyArmorStands-Polymer - a Polymer based Fabric port of EasyArmorStands
 * Copyright (C) 56738 and EasyArmorStands contributors (original work)
 * Copyright (C) 2026 ripsquid
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
package re.koda.easyarmorstands.platform.fabric;

import me.lucko.fabric.api.permissions.v0.PermissionCheckEvent;
import net.fabricmc.fabric.api.permission.v1.PermissionContextOwner;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Answers EasyArmorStands permission checks using the Fabric Permission API
 * ({@code fabric-permission-api-v1}) that is part of Fabric API.
 * <p>
 * Permissions are dotted strings such as {@code easyarmorstands.property.glow}; in the
 * Fabric Permission API they are identified as {@code easyarmorstands:property/glow}.
 * <p>
 * The bridge hooks into the {@code fabric-permissions-api-v0} check event, which is also what
 * Cloud uses for command permissions, so commands and editor actions are checked the same way.
 * If no Fabric Permission API provider has an answer, other v0 providers (such as older
 * LuckPerms versions) and finally the operator level decide, as before.
 */
public final class FabricPermissionBridge {
    private static final String PREFIX = "easyarmorstands.";
    private static final Map<String, Identifier> IDENTIFIERS = new ConcurrentHashMap<>();

    private FabricPermissionBridge() {
    }

    public static void register() {
        PermissionCheckEvent.EVENT.register(FabricPermissionBridge::check);
    }

    private static TriState check(SharedSuggestionProvider source, String permission) {
        if (!(source instanceof PermissionContextOwner owner)) {
            return TriState.DEFAULT;
        }
        Identifier identifier = toIdentifier(permission);
        if (identifier == null) {
            return TriState.DEFAULT;
        }
        return owner.checkPermission(identifier);
    }

    /**
     * Converts a permission such as {@code easyarmorstands.property.glow} into the identifier
     * {@code easyarmorstands:property/glow}.
     */
    public static @Nullable Identifier toIdentifier(String permission) {
        if (!permission.startsWith(PREFIX)) {
            return null;
        }
        return IDENTIFIERS.computeIfAbsent(permission, p -> Identifier.tryBuild(
                "easyarmorstands",
                p.substring(PREFIX.length()).toLowerCase(Locale.ROOT).replace('.', '/')));
    }
}
