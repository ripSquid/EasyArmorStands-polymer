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

import re.koda.easyarmorstands.platform.dialog.Dialog;
import re.koda.easyarmorstands.platform.dialog.DialogBody;
import re.koda.easyarmorstands.platform.dialog.DialogFactory;
import re.koda.easyarmorstands.platform.dialog.DialogInput;
import re.koda.easyarmorstands.platform.dialog.DialogResponseView;
import re.koda.easyarmorstands.platform.modded.ModdedPlatform;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import net.minecraft.resources.Identifier;
import net.minecraft.server.dialog.ActionButton;
import net.minecraft.server.dialog.CommonButtonData;
import net.minecraft.server.dialog.CommonDialogData;
import net.minecraft.server.dialog.ConfirmationDialog;
import net.minecraft.server.dialog.DialogAction;
import net.minecraft.server.dialog.action.CustomAll;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class ModdedDialogFactory implements DialogFactory {
    private final ModdedPlatform platform;
    private final ModdedDialogBodyProvider bodyProvider;
    private final ModdedDialogInputProvider inputProvider;

    public ModdedDialogFactory(ModdedPlatform platform) {
        this.platform = platform;
        this.bodyProvider = new ModdedDialogBodyProvider(platform);
        this.inputProvider = new ModdedDialogInputProvider(platform);
    }

    @Override
    public ModdedDialogBodyProvider bodyProvider() {
        return bodyProvider;
    }

    @Override
    public ModdedDialogInputProvider inputProvider() {
        return inputProvider;
    }

    @Override
    public Dialog createDialog(Component title, List<DialogBody> body, List<DialogInput> inputs, Component saveLabel, Component cancelLabel, Consumer<DialogResponseView> saveAction, ClickCallback.Options callbackOptions) {
        Identifier actionId = platform.registerCustomClickAction(saveAction, callbackOptions);
        return ModdedDialog.fromNative(platform, new ConfirmationDialog(
                new CommonDialogData(
                        platform.getAdventure().asNative(title),
                        Optional.empty(),
                        true,
                        true,
                        DialogAction.CLOSE,
                        body.stream().map(ModdedDialogBody::toNative).toList(),
                        inputs.stream().map(ModdedDialogInput::toNative).toList()),
                new ActionButton(
                        new CommonButtonData(
                                platform.getAdventure().asNative(saveLabel),
                                CommonButtonData.DEFAULT_WIDTH
                        ),
                        Optional.of(new CustomAll(actionId, Optional.empty()))),
                new ActionButton(
                        new CommonButtonData(
                                platform.getAdventure().asNative(cancelLabel),
                                CommonButtonData.DEFAULT_WIDTH
                        ),
                        Optional.empty())
        ));
    }
}
