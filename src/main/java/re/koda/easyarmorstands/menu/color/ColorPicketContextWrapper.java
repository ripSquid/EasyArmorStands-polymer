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
package re.koda.easyarmorstands.menu.color;

import re.koda.easyarmorstands.platform.color.RGBColor;
import re.koda.easyarmorstands.platform.Platform;
import re.koda.easyarmorstands.platform.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ColorPicketContextWrapper implements ColorPickerContext {
    private final ColorPickerContext context;
    private final List<Runnable> subscriptions = new ArrayList<>();

    public ColorPicketContextWrapper(ColorPickerContext context) {
        this.context = context;
    }

    @Override
    public @NotNull Platform platform() {
        return context.platform();
    }

    @Override
    public @NotNull ItemStack item() {
        return context.item();
    }

    @Override
    public @NotNull RGBColor getColor() {
        return context.getColor();
    }

    @Override
    public void setColor(@NotNull RGBColor color) {
        context.setColor(color);
        for (Runnable subscription : subscriptions) {
            subscription.run();
        }
    }

    public void subscribe(@NotNull Runnable callback) {
        subscriptions.add(callback);
    }
}
