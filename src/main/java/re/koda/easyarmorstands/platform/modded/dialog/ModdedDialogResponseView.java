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
package re.koda.easyarmorstands.platform.modded.dialog;

import re.koda.easyarmorstands.platform.dialog.DialogResponseView;
import re.koda.easyarmorstands.platform.modded.ModdedPlatform;
import re.koda.easyarmorstands.platform.modded.ModdedPlatformHolder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.function.BiFunction;

public interface ModdedDialogResponseView extends DialogResponseView, ModdedPlatformHolder {
    Tag getNative();

    static ModdedDialogResponseView fromNative(ModdedPlatform platform, Tag tag) {
        return new ModdedDialogResponseViewImpl(platform, tag);
    }

    static Tag toNative(DialogResponseView view) {
        return ((ModdedDialogResponseView) view).getNative();
    }

    default <T> @Nullable T getValue(String key, BiFunction<CompoundTag, String, Optional<T>> getter) {
        return getNative().asCompound().flatMap(t -> getter.apply(t, key)).orElse(null);
    }

    @Override
    default @Nullable Boolean getBoolean(String key) {
        return getValue(key, CompoundTag::getBoolean);
    }

    @Override
    default @Nullable String getText(String key) {
        return getValue(key, CompoundTag::getString);
    }
}
