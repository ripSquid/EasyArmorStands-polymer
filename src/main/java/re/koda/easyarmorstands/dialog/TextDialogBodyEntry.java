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
package re.koda.easyarmorstands.dialog;

import re.koda.easyarmorstands.message.Message;
import re.koda.easyarmorstands.message.MessageStyle;
import re.koda.easyarmorstands.platform.dialog.DialogBody;
import re.koda.easyarmorstands.platform.dialog.DialogBodyProvider;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.translation.GlobalTranslator;

import java.util.List;
import java.util.Locale;

public class TextDialogBodyEntry implements DialogBodyEntry {
    @Override
    public void populateBody(List<DialogBody> body, Locale locale, DialogBodyProvider provider) {
        Component text = Component.translatable("easyarmorstands.dialog.minimessage.hint")
                .arguments(Message.format(MessageStyle.LINK, Component.translatable("easyarmorstands.dialog.minimessage"))
                        .hoverEvent(Message.hover("easyarmorstands.click-to-open-minimessage"))
                        .clickEvent(ClickEvent.openUrl("https://docs.papermc.io/adventure/minimessage/format/")));
        body.add(provider.createText(GlobalTranslator.render(text, locale)));
    }
}
