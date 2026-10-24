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

import re.koda.easyarmorstands.platform.dialog.Dialog;
import re.koda.easyarmorstands.platform.dialog.DialogBody;
import re.koda.easyarmorstands.platform.dialog.DialogFactory;
import re.koda.easyarmorstands.platform.dialog.DialogInput;
import re.koda.easyarmorstands.platform.dialog.DialogResponseView;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.translation.GlobalTranslator;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class DialogBuilder {
    private final DialogFactory factory;
    private final Locale locale;
    private final List<DialogEntry> entries = new ArrayList<>();
    private Component title = Component.empty();

    public DialogBuilder(DialogFactory factory, Locale locale) {
        this.factory = factory;
        this.locale = locale;
    }

    public void setTitle(Component title) {
        this.title = title;
    }

    public void addEntry(DialogEntry entry) {
        entries.add(entry);
    }

    public Dialog build() {
        ClickCallback.Options callbackOptions = ClickCallback.Options.builder()
                .lifetime(Duration.ofHours(1))
                .uses(1)
                .build();

        Set<DialogBodyEntry> bodyEntries = new LinkedHashSet<>();
        List<DialogInput> inputs = new ArrayList<>();
        for (DialogEntry entry : entries) {
            entry.populateBody(bodyEntries);
            entry.populateInputs(inputs, locale, factory.inputProvider());
        }

        List<DialogBody> body = new ArrayList<>();
        for (DialogBodyEntry entry : bodyEntries) {
            entry.populateBody(body, locale, factory.bodyProvider());
        }

        Component saveLabel = GlobalTranslator.render(Component.translatable("easyarmorstands.button.save"), locale);
        Component cancelLabel = GlobalTranslator.render(Component.translatable("easyarmorstands.button.cancel"), locale);
        return factory.createDialog(
                GlobalTranslator.render(title, locale),
                body,
                inputs,
                saveLabel,
                cancelLabel,
                this::save,
                callbackOptions);
    }

    private void save(DialogResponseView response) {
        for (DialogEntry entry : entries) {
            entry.save(response);
        }
        for (DialogEntry entry : entries) {
            entry.commit();
        }
    }
}
