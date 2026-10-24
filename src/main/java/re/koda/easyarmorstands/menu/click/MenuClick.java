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
package re.koda.easyarmorstands.menu.click;

import re.koda.easyarmorstands.api.editor.Session;
import re.koda.easyarmorstands.menu.Menu;
import re.koda.easyarmorstands.menu.slot.MenuSlot;
import re.koda.easyarmorstands.platform.entity.Player;
import re.koda.easyarmorstands.platform.inventory.ItemStack;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.identity.Identity;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

@ApiStatus.NonExtendable
public interface MenuClick extends ForwardingAudience.Single {
    @NotNull Menu menu();

    @Nullable MenuSlot slot();

    int index();

    @NotNull Player player();

    @Nullable Session session();

    default @NotNull Locale locale() {
        return getOrDefault(Identity.LOCALE, Locale.US);
    }

    @NotNull ItemStack cursor();

    void close();

    void updateItem();

    void updateItem(@NotNull MenuSlot slot);

    void queueTask(@NotNull Runnable task);

    void interceptNextClick(@NotNull MenuClickInterceptor interceptor);

    boolean isLeftClick();

    boolean isRightClick();

    boolean isShiftClick();
}
