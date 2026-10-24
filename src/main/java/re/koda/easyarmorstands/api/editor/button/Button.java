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
package re.koda.easyarmorstands.api.editor.button;

import re.koda.easyarmorstands.api.editor.EyeRay;
import re.koda.easyarmorstands.api.particle.ParticleColor;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface Button {
    static ParticleColor color(boolean focused, boolean selected, ParticleColor defaultColor) {
        if (selected) {
            if (focused) {
                return ParticleColor.AQUA;
            } else {
                return ParticleColor.BLUE;
            }
        } else {
            if (focused) {
                return ParticleColor.YELLOW;
            } else {
                return defaultColor;
            }
        }
    }

    /**
     * Updates internal properties of the button.
     */
    void update();

    /**
     * Tests whether the provided eye ray intersects with this button.
     * <p>
     * Called after {@link #update()}.
     *
     * @param ray     The eye ray.
     * @param results A consumer which should be called with each intersection.
     */
    void intersect(@NotNull EyeRay ray, @NotNull Consumer<@NotNull ButtonResult> results);

    /**
     * Updates the preview.
     * <p>
     * Called after {@link #update()}.
     *
     * @param focused  Whether the player is looking at this button.
     * @param selected Whether the button is currently selected.
     */
    void updatePreview(boolean focused, boolean selected);

    /**
     * Makes the preview visible.
     * <p>
     * Called after {@link #updatePreview}.
     * The preview should remain visible until {@link #hidePreview} is called.
     */
    void showPreview();

    /**
     * Makes the preview invisible.
     */
    void hidePreview();
}
