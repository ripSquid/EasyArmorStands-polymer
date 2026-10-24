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
package re.koda.easyarmorstands.particle;

import re.koda.easyarmorstands.api.particle.ParticleColor;
import re.koda.easyarmorstands.api.particle.PointParticle;
import re.koda.easyarmorstands.gizmo.api.PointGizmo;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaterniondc;
import org.joml.Vector3dc;

public class GizmoPointParticle extends GizmoParticle implements PointParticle {
    private final PointGizmo gizmo;
    private ParticleColor color = ParticleColor.WHITE;

    public GizmoPointParticle(PointGizmo gizmo) {
        super(gizmo);
        this.gizmo = gizmo;
        this.gizmo.setBillboard(true);
    }

    @Override
    public double getSize() {
        return gizmo.getSize();
    }

    @Override
    public void setSize(double size) {
        gizmo.setSize(size);
    }

    @Override
    public @NotNull Vector3dc getPosition() {
        return gizmo.getPosition();
    }

    @Override
    public void setPosition(@NotNull Vector3dc position) {
        gizmo.setPosition(position);
    }

    @Override
    public @NotNull Quaterniondc getRotation() {
        return gizmo.getRotation();
    }

    @Override
    public void setRotation(@NotNull Quaterniondc rotation) {
        gizmo.setRotation(rotation);
    }

    @Override
    public boolean isBillboard() {
        return gizmo.isBillboard();
    }

    @Override
    public void setBillboard(boolean billboard) {
        gizmo.setBillboard(billboard);
    }

    @Override
    public @NotNull ParticleColor getColor() {
        return color;
    }

    @Override
    public void setColor(@NotNull ParticleColor color) {
        this.color = color;
        gizmo.setColor(GizmoAdapter.convert(color));
    }
}
