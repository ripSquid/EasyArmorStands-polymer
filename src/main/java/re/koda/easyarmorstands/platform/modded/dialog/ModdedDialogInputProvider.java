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

import re.koda.easyarmorstands.platform.dialog.DialogInputProvider;
import re.koda.easyarmorstands.platform.modded.ModdedPlatform;
import net.kyori.adventure.text.Component;
import net.minecraft.server.dialog.Input;
import net.minecraft.server.dialog.body.PlainMessage;
import net.minecraft.server.dialog.input.BooleanInput;
import net.minecraft.server.dialog.input.TextInput;

import java.util.Optional;

public class ModdedDialogInputProvider implements DialogInputProvider {
    private final ModdedPlatform platform;

    public ModdedDialogInputProvider(ModdedPlatform platform) {
        this.platform = platform;
    }

    @Override
    public ModdedDialogInput createBoolean(String key, Component label, boolean initial) {
        net.minecraft.network.chat.Component nativeLabel = platform.getAdventure().asNative(label);
        BooleanInput input = new BooleanInput(nativeLabel, initial, "true", "false");
        return ModdedDialogInput.fromNative(platform, new Input(key, input));
    }

    @Override
    public ModdedDialogInput createText(String key, Component label, String initial) {
        net.minecraft.network.chat.Component nativeLabel = platform.getAdventure().asNative(label);
        TextInput.MultilineOptions options = new TextInput.MultilineOptions(Optional.empty(), Optional.of(64));
        TextInput input = new TextInput(PlainMessage.DEFAULT_WIDTH, nativeLabel, true, initial, 32768, Optional.of(options));
        return ModdedDialogInput.fromNative(platform, new Input(key, input));
    }
}
