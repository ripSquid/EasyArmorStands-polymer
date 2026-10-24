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
package re.koda.easyarmorstands.platform.modded.profile;

import com.mojang.authlib.GameProfile;
import re.koda.easyarmorstands.platform.modded.ModdedPlatform;
import re.koda.easyarmorstands.platform.modded.ModdedPlatformHolder;
import re.koda.easyarmorstands.platform.profile.Profile;
import net.kyori.adventure.platform.modcommon.MinecraftAudiences;
import net.kyori.adventure.text.object.ObjectContents;
import net.kyori.adventure.text.object.PlayerHeadObjectContents;
import net.minecraft.core.ClientAsset;
import net.minecraft.util.Util;
import net.minecraft.world.item.component.ResolvableProfile;

public interface ModdedProfile extends Profile, ModdedPlatformHolder {
    ResolvableProfile getNative();

    static ModdedProfile fromNative(ModdedPlatform platform, ResolvableProfile profile) {
        return new ModdedProfileImpl(platform, profile);
    }

    static ResolvableProfile toNative(Profile profile) {
        return ((ModdedProfile) profile).getNative();
    }

    @Override
    default PlayerHeadObjectContents asObjectContents() {
        PlayerHeadObjectContents.Builder builder = ObjectContents.playerHead();
        ResolvableProfile profile = getNative();
        GameProfile partialProfile = profile.partialProfile();
        if (profile instanceof ResolvableProfile.Dynamic) {
            if (partialProfile.id().equals(Util.NIL_UUID)) {
                builder.name(partialProfile.name());
            } else {
                builder.id(partialProfile.id());
            }
            return builder.build();
        }
        builder.id(partialProfile.id().equals(Util.NIL_UUID) ? null : partialProfile.id());
        builder.name(partialProfile.name().isEmpty() ? null : partialProfile.name());
        builder.profileProperties(partialProfile.properties().values().stream()
                .map(property -> PlayerHeadObjectContents.property(property.name(), property.value(), property.signature()))
                .toList());
        builder.texture(profile.skinPatch().body().map(ClientAsset.ResourceTexture::id).map(MinecraftAudiences::asAdventure).orElse(null));
        return builder.build();
    }
}
