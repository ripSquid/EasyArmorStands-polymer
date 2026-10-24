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
package re.koda.easyarmorstands.platform.modded;

import re.koda.easyarmorstands.platform.dialog.DialogResponseView;
import net.kyori.adventure.text.event.ClickCallback;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

class ModdedClickActionRegistry {
    private final Map<Identifier, Entry> entries = new HashMap<>();

    public Identifier registerClickAction(Consumer<DialogResponseView> action, ClickCallback.Options options) {
        String path = "callback/" + UUID.randomUUID().toString().replace('-', '_');
        Identifier id = Identifier.fromNamespaceAndPath("easyarmorstands", path);
        Entry entry = new Entry(action, System.nanoTime(), options.lifetime().toNanos());
        synchronized (entries) {
            entries.put(id, entry);
        }
        return id;
    }

    public @Nullable Consumer<DialogResponseView> resolve(Identifier id) {
        Entry entry;
        synchronized (entries) {
            entry = entries.remove(id);
        }
        if (entry == null) {
            return null;
        }
        return entry.action();
    }

    public void clean() {
        long time = System.nanoTime();
        synchronized (entries) {
            entries.values().removeIf(e -> e.isExpiredAt(time));
        }
    }

    private record Entry(Consumer<DialogResponseView> action, long created, long lifetime) {
        private boolean isExpiredAt(long time) {
            long age = time - created;
            return age >= lifetime;
        }
    }
}
