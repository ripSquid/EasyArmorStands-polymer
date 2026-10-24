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
package re.koda.easyarmorstands.platform.modded.scheduler;

import re.koda.easyarmorstands.platform.scheduler.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ModdedScheduler implements Scheduler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModdedScheduler.class);
    private final List<Runnable> queue = new ArrayList<>();

    @Override
    public void runTask(Runnable runnable) {
        synchronized (queue) {
            queue.add(runnable);
        }
    }

    public void execute() {
        List<Runnable> tasks;
        synchronized (queue) {
            tasks = List.copyOf(queue);
            queue.clear();
        }

        for (Runnable task : tasks) {
            try {
                task.run();
            } catch (Throwable t) {
                LOGGER.error("Failed to run queued task", t);
            }
        }
    }
}
