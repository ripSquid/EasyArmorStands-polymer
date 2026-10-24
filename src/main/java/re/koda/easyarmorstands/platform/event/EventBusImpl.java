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
package re.koda.easyarmorstands.platform.event;

import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EventBusImpl implements EventBus {
    @SuppressWarnings({"rawtypes"})
    private final Map<EventType, Entry> subscriptions = new HashMap<>();

    @SuppressWarnings("unchecked")
    private <C> Entry<C> getEntry(EventType<C> type, boolean create) {
        synchronized (subscriptions) {
            if (create) {
                return subscriptions.computeIfAbsent(type, Entry::new);
            } else {
                return subscriptions.get(type);
            }
        }
    }

    @Override
    public <C> void subscribe(EventType<C> type, C callback) {
        getEntry(type, true).subscribe(callback);
    }

    @Override
    public <C> C invoker(EventType<C> type) {
        return getEntry(type, false).invoker();
    }

    private static class Entry<C> {
        private final EventType<C> type;
        private final Set<C> callbacks = new HashSet<>();
        private @Nullable C merged;

        private Entry(EventType<C> type) {
            this.type = type;
        }

        public synchronized void subscribe(C callback) {
            callbacks.add(callback);
            merged = null;
        }

        public synchronized C invoker() {
            if (merged == null) {
                merged = type.dispatcher().merge(callbacks);
            }
            return merged;
        }
    }
}
