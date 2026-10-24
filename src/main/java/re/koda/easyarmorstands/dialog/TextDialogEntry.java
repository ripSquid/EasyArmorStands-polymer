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

import re.koda.easyarmorstands.EasyArmorStandsCommon;
import re.koda.easyarmorstands.api.property.Property;
import re.koda.easyarmorstands.platform.dialog.DialogInput;
import re.koda.easyarmorstands.platform.dialog.DialogInputProvider;
import re.koda.easyarmorstands.platform.dialog.DialogResponseView;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.translation.GlobalTranslator;

import java.util.Collection;
import java.util.Locale;

@SuppressWarnings("UnstableApiUsage")
public class TextDialogEntry implements DialogEntry {
    private final String key;
    private final Property<Component> property;
    private final MiniMessage serializer = EasyArmorStandsCommon.miniMessage();

    public TextDialogEntry(String key, Property<Component> property) {
        this.key = key;
        this.property = property;
    }

    @Override
    public void populateBody(Collection<DialogBodyEntry> body) {
        body.add(new TextDialogBodyEntry());
    }

    @Override
    public void populateInputs(Collection<DialogInput> inputs, Locale locale, DialogInputProvider provider) {
        Component label = GlobalTranslator.render(property.getType().getName(), locale);
        inputs.add(provider.createText(key, label, serializer.serialize(property.getValue())));
    }

    @Override
    public void save(DialogResponseView response) {
        String text = response.getText(key);
        if (text != null) {
            property.setValue(serializer.deserialize(text));
        }
    }

    @Override
    public void commit() {
        property.commit();
    }
}
