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
package re.koda.easyarmorstands.session.context;

import re.koda.easyarmorstands.api.editor.EyeRay;
import re.koda.easyarmorstands.api.editor.context.LateUpdateContext;
import re.koda.easyarmorstands.api.editor.context.UpdateContext;
import re.koda.easyarmorstands.api.editor.input.Input;
import re.koda.easyarmorstands.session.SessionImpl;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2dc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UpdateContextImpl implements UpdateContext, LateUpdateContext {
    private final @NotNull SessionImpl session;
    private final @NotNull List<@NotNull Input> inputs = new ArrayList<>();
    private final @NotNull Set<@NotNull Input> availableInputs = new HashSet<>();
    private EyeRay eyeRay;
    private @NotNull Component actionBar = Component.empty();
    private @NotNull Component title = Component.empty();
    private @NotNull Component subtitle = Component.empty();

    public UpdateContextImpl(@NotNull SessionImpl session) {
        this.session = session;
    }

    @Override
    public @NotNull EyeRay eyeRay() {
        if (eyeRay == null) {
            eyeRay = session.eyeRay();
        }
        return eyeRay;
    }

    @Override
    public @NotNull EyeRay eyeRay(@NotNull Vector2dc cursor) {
        return session.eyeRay(cursor);
    }

    public @NotNull Component getActionBar() {
        return actionBar;
    }

    @Override
    public void setActionBar(@NotNull ComponentLike actionBar) {
        this.actionBar = actionBar.asComponent();
    }

    public @NotNull Component getTitle() {
        return title;
    }

    @Override
    public void setTitle(@NotNull ComponentLike title) {
        this.title = title.asComponent();
    }

    public @NotNull Component getSubtitle() {
        return subtitle;
    }

    @Override
    public void setSubtitle(@NotNull ComponentLike subtitle) {
        this.subtitle = subtitle.asComponent();
    }

    @Override
    public boolean isInputAvailable(@NotNull Input input) {
        return availableInputs.contains(input);
    }

    @Override
    public void addInput(@NotNull Input input) {
        inputs.add(input);
    }

    public @NotNull List<@NotNull Input> getInputs() {
        return inputs;
    }

    public @NotNull Set<@NotNull Input> getAvailableInputs() {
        return availableInputs;
    }
}
