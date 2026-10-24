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
package re.koda.easyarmorstands.modded.color;

import re.koda.easyarmorstands.platform.color.RGBColor;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

public class DataItemColorAccessor<T> implements ItemColorAccessor {
    private final DataComponentType<T> type;
    private final T defaultValue;
    private final Predicate<Holder<Item>> predicate;
    private final IntFunction<T> provider;
    private final ToIntFunction<T> extractor;

    public DataItemColorAccessor(DataComponentType<T> type, T defaultValue, Predicate<Holder<Item>> predicate, IntFunction<T> provider, ToIntFunction<T> extractor) {
        this.type = type;
        this.defaultValue = defaultValue;
        this.predicate = predicate;
        this.provider = provider;
        this.extractor = extractor;
    }

    @Override
    public boolean isSupported(ItemStack item) {
        return item.has(type) || item.is(predicate);
    }

    @Override
    public RGBColor getColor(ItemStack item) {
        return RGBColor.of(extractor.applyAsInt(item.getOrDefault(type, defaultValue)));
    }

    @Override
    public void setColor(ItemStack item, RGBColor color) {
        item.set(type, provider.apply(color.value()));
    }
}
