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

import re.koda.easyarmorstands.api.Axis;
import re.koda.easyarmorstands.api.particle.CircleParticle;
import re.koda.easyarmorstands.api.particle.ParticleColor;
import re.koda.easyarmorstands.gizmo.api.CircleGizmo;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaterniondc;
import org.joml.Vector3dc;

public class GizmoCircleParticle extends GizmoParticle implements CircleParticle, EditorParticle {
    private final CircleGizmo gizmo;
    private Axis axis = Axis.Y;
    private ParticleColor color = ParticleColor.WHITE;

    public GizmoCircleParticle(CircleGizmo gizmo) {
        super(gizmo);
        this.gizmo = gizmo;
    }

    @Override
    public @NotNull Vector3dc getCenter() {
        return gizmo.getPosition();
    }

    @Override
    public void setCenter(@NotNull Vector3dc center) {
        gizmo.setPosition(center);
    }

    @Override
    public @NotNull Axis getAxis() {
        return axis;
    }

    @Override
    public void setAxis(@NotNull Axis axis) {
        this.axis = axis;
        gizmo.setAxis(GizmoAdapter.convert(axis));
    }

    @Override
    public double getWidth() {
        return gizmo.getWidth();
    }

    @Override
    public void setWidth(double width) {
        gizmo.setWidth(width);
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
    public double getRadius() {
        return gizmo.getRadius();
    }

    @Override
    public void setRadius(double radius) {
        gizmo.setRadius(radius);
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
