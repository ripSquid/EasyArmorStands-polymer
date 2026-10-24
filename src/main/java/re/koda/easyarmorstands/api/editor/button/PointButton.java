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
import re.koda.easyarmorstands.api.editor.Session;
import re.koda.easyarmorstands.api.particle.ParticleColor;
import re.koda.easyarmorstands.api.particle.PointParticle;
import re.koda.easyarmorstands.api.util.PositionProvider;
import re.koda.easyarmorstands.api.util.RotationProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import org.joml.Vector3dc;

import java.util.Objects;
import java.util.function.Consumer;

public final class PointButton implements Button {
    private final PointParticle particle;
    private final Session session;
    private final PositionProvider positionProvider;
    private final @Nullable RotationProvider rotationProvider;
    private final Vector3d position = new Vector3d();
    private double scale;
    private ParticleColor color = ParticleColor.WHITE;
    private int priority = 0;

    public PointButton(@NotNull Session session, @NotNull PositionProvider positionProvider, @Nullable RotationProvider rotationProvider) {
        this.session = session;
        this.positionProvider = positionProvider;
        this.rotationProvider = rotationProvider;
        this.particle = session.particleProvider().createPoint();
    }

    public @NotNull ParticleColor getColor() {
        return color;
    }

    public void setColor(@NotNull ParticleColor color) {
        this.color = Objects.requireNonNull(color);
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public void update() {
        position.set(positionProvider.getPosition());
        scale = session.getScale(position);
    }

    @Override
    public void intersect(@NotNull EyeRay ray, @NotNull Consumer<@NotNull ButtonResult> results) {
        Vector3dc intersection = ray.intersectPoint(position, scale);
        if (intersection != null) {
            results.accept(ButtonResult.of(intersection, priority));
        }
    }

    @Override
    public void updatePreview(boolean focused, boolean selected) {
        particle.setPosition(position);
        particle.setSize(scale / 16);
        if (rotationProvider != null) {
            particle.setRotation(rotationProvider.getRotation());
            particle.setBillboard(false);
        } else {
            particle.setBillboard(true);
        }
        particle.setColor(Button.color(focused, selected, color));
    }

    @Override
    public void showPreview() {
        session.addParticle(particle);
    }

    @Override
    public void hidePreview() {
        session.removeParticle(particle);
    }
}
