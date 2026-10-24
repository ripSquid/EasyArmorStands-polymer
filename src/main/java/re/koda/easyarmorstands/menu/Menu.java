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
package re.koda.easyarmorstands.menu;

import re.koda.easyarmorstands.menu.click.MenuClick;
import re.koda.easyarmorstands.menu.click.MenuClickInterceptor;
import re.koda.easyarmorstands.menu.slot.MenuSlot;
import re.koda.easyarmorstands.platform.entity.Player;
import re.koda.easyarmorstands.platform.inventory.InventoryHolder;
import re.koda.easyarmorstands.platform.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

@ApiStatus.NonExtendable
public interface Menu extends InventoryHolder {
    static int index(int row, int column) {
        return 9 * row + column;
    }

    @Contract(pure = true)
    int getSize();

    @Contract(pure = true)
    @Nullable MenuSlot getSlot(int index);

    void close(@NotNull Player player);

    @Nullable ItemStack getItem(int index);

    void updateItem(int index);

    void updateItem(@NotNull MenuSlot slot);

    void updateItems(@NotNull Predicate<@NotNull MenuSlot> predicate);

    void queueTask(@NotNull Runnable task);

    void interceptNextClick(@NotNull MenuClickInterceptor interceptor);

    void addCloseListener(@NotNull MenuCloseListener listener);

    void onClick(@NotNull MenuClick click);
}
