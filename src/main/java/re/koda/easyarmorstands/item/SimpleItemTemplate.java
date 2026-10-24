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
package re.koda.easyarmorstands.item;

import re.koda.easyarmorstands.platform.inventory.ItemStack;
import re.koda.easyarmorstands.util.ItemTemplate;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class SimpleItemTemplate implements ItemTemplate {
    private static final Style NAME_FALLBACK_STYLE = Style.style(TextDecoration.ITALIC.withState(false));
    private static final Style LORE_FALLBACK_STYLE = Style.style(TextDecoration.ITALIC.withState(false));

    private final ItemStack template;
    private final String displayName;
    private final List<String> lore;
    private final TagResolver resolver;
    private final ItemRenderer renderer;

    public SimpleItemTemplate(ItemStack template, String displayName, List<String> lore, TagResolver resolver, ItemRenderer renderer) {
        this.template = template;
        this.displayName = displayName;
        this.lore = Collections.unmodifiableList(new ArrayList<>(lore));
        this.resolver = resolver;
        this.renderer = renderer;
    }

    private Component renderName(Locale locale, TagResolver resolver) {
        return renderer.renderName(displayName, locale, resolver)
                .applyFallbackStyle(NAME_FALLBACK_STYLE);
    }

    private List<Component> renderLore(Locale locale, TagResolver resolver) {
        List<Component> renderedLore = new ArrayList<>(lore.size());
        for (String line : lore) {
            renderedLore.add(renderer.renderLore(line, locale, resolver)
                    .applyFallbackStyle(LORE_FALLBACK_STYLE));
        }
        return renderedLore;
    }

    @Override
    public ItemStack render(Locale locale) {
        return render(locale, TagResolver.empty());
    }

    @Override
    public ItemStack render(Locale locale, TagResolver resolver) {
        resolver = TagResolver.builder()
                .resolver(this.resolver)
                .resolver(resolver)
                .build();

        return template
                .withCustomName(renderName(locale, resolver))
                .withLore(renderLore(locale, resolver));
    }
}
