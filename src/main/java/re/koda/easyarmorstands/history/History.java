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
package re.koda.easyarmorstands.history;

import re.koda.easyarmorstands.EasyArmorStandsCommon;
import re.koda.easyarmorstands.history.action.Action;
import re.koda.easyarmorstands.history.action.GroupAction;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.List;
import java.util.UUID;

public class History {
    private final ArrayDeque<Action> past = new ArrayDeque<>();
    private final ArrayDeque<Action> future = new ArrayDeque<>();
    private final ChangeTracker tracker;

    public History(EasyArmorStandsCommon eas) {
        tracker = new ChangeTracker(eas, this);
    }

    public ArrayDeque<Action> getPast() {
        return past;
    }

    public ArrayDeque<Action> getFuture() {
        return future;
    }

    public ChangeTracker getTracker() {
        return tracker;
    }

    public void push(Action action) {
        past.push(action);
        future.clear();
        while (past.size() > 100) {
            past.removeLast();
        }
    }

    public void push(List<? extends Action> actions, @Nullable Component description) {
        if (actions.size() == 1) {
            push(actions.get(0));
        } else if (actions.size() > 1) {
            push(new GroupAction(actions, description));
        }
    }

    public Action takeUndoAction() {
        Action action = past.poll();
        if (action != null) {
            future.push(action);
        }
        return action;
    }

    public Action takeRedoAction() {
        Action action = future.poll();
        if (action != null) {
            past.push(action);
        }
        return action;
    }

    public void onEntityReplaced(@NotNull UUID oldId, @NotNull UUID newId) {
        for (Action action : past) {
            action.onEntityReplaced(oldId, newId);
        }
        for (Action action : future) {
            action.onEntityReplaced(oldId, newId);
        }
    }
}
