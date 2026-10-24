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
package re.koda.easyarmorstands.history.action;

import re.koda.easyarmorstands.command.sender.EasPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class GroupAction implements Action {
    private final Action[] actions;
    private Component description;

    public GroupAction(List<? extends Action> actions, @Nullable Component description) {
        this.description = description;
        this.actions = actions.toArray(new Action[0]);
    }

    @Override
    public boolean execute(EasPlayer player) {
        boolean ok = true;
        for (Action action : actions) {
            ok &= action.execute(player);
        }
        return ok;
    }

    @Override
    public boolean undo(EasPlayer player) {
        boolean ok = true;
        for (int i = actions.length - 1; i >= 0; i--) {
            ok &= actions[i].undo(player);
        }
        return ok;
    }

    @Override
    public Component describe() {
        if (description == null) {
            TextComponent.Builder builder = Component.text();
            for (int i = 0; i < actions.length; i++) {
                if (i != 0) {
                    builder.append(Component.text(", ", NamedTextColor.DARK_GRAY));
                }
                builder.append(actions[i].describe());
            }
            description = builder.build();
        }
        return description;
    }

    @Override
    public void onEntityReplaced(@NotNull UUID oldId, @NotNull UUID newId) {
        for (Action action : actions) {
            action.onEntityReplaced(oldId, newId);
        }
    }
}
