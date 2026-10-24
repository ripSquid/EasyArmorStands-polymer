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
package re.koda.easyarmorstands.menu.slot;

import re.koda.easyarmorstands.api.menu.button.MenuButton;
import re.koda.easyarmorstands.api.menu.button.MenuIcon;
import re.koda.easyarmorstands.api.menu.click.MenuClickContext;
import re.koda.easyarmorstands.menu.button.MenuSlotButton;
import re.koda.easyarmorstands.menu.click.MenuClick;
import re.koda.easyarmorstands.message.Message;
import re.koda.easyarmorstands.message.MessageStyle;
import re.koda.easyarmorstands.platform.entity.Player;
import re.koda.easyarmorstands.platform.inventory.ItemStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.translation.GlobalTranslator;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Locale;

@NullMarked
public class MenuButtonSlot implements MenuSlot {
    public static final Style FALLBACK_STYLE = Style.style(
            NamedTextColor.WHITE,
            TextDecoration.ITALIC.withState(false));
    private final MenuButton button;

    private MenuButtonSlot(MenuButton button) {
        this.button = button;
    }

    public static MenuSlot toSlot(MenuButton button) {
        if (button instanceof MenuSlotButton menuSlotButton) {
            return menuSlotButton.getSlot();
        } else {
            return new MenuButtonSlot(button);
        }
    }

    public static ItemStack createItem(MenuIcon icon, Component name, Component value, List<Component> description, Locale locale) {
        ItemStack item = icon.asItem();

        if (name == Component.empty() && description.isEmpty()) {
            return item.withHideTooltip(true);
        }

        return item
                .withCustomName(formatCustomName(name, value, locale))
                .withLore(description.stream()
                        .map(c -> format(c, MessageStyle.BUTTON_DESCRIPTION, locale))
                        .toList())
                .withHiddenComponents();
    }

    public static Component formatCustomName(Component name, Component value, Locale locale) {
        TextComponent.Builder builder = Component.text()
                .append(format(name, MessageStyle.BUTTON_NAME, locale));
        if (value != Component.empty()) {
            builder.append(Component.text(": ", FALLBACK_STYLE));
            builder.append(GlobalTranslator.render(value, locale)
                    .applyFallbackStyle(TextDecoration.ITALIC.withState(false)));
        }
        return builder.build();
    }

    public static Component format(Component component, MessageStyle style, Locale locale) {
        return render(Message.format(style, component), locale);
    }

    public static Component render(Component component, Locale locale) {
        return GlobalTranslator.render(component, locale).applyFallbackStyle(FALLBACK_STYLE);
    }

    public MenuButton getButton() {
        return button;
    }

    @Override
    public @Nullable ItemStack getItem(Locale locale) {
        if (button instanceof MenuSlotButton menuSlotButton) {
            return menuSlotButton.getItem(locale);
        }
        return createItem(button.icon(), button.name(), button.value(), button.description(), locale);
    }

    @Override
    public void onClick(MenuClick click) {
        if (button instanceof MenuSlotButton menuSlotButton) {
            menuSlotButton.getSlot().onClick(click);
            return;
        }
        button.onClick(new MenuClickContext() {
            @Override
            public Player player() {
                return click.player();
            }

            @Override
            public boolean isLeftClick() {
                return click.isLeftClick();
            }

            @Override
            public boolean isRightClick() {
                return click.isRightClick();
            }

            @Override
            public boolean isShiftClick() {
                return click.isShiftClick();
            }

            @Override
            public void closeMenu() {
                click.close();
            }

            @Override
            public void updateMenu() {
                click.menu().updateItems(slot -> true);
            }
        });
        click.updateItem();
    }
}
