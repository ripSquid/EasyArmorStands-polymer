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
import re.koda.easyarmorstands.api.particle.BoundingBoxParticle;
import re.koda.easyarmorstands.api.particle.ParticleColor;
import re.koda.easyarmorstands.api.util.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector3dc;

import java.util.function.Supplier;

public class LineBoundingBoxParticle implements BoundingBoxParticle, EditorParticle {
    private final Line[] lines;
    private BoundingBox box = BoundingBox.of(new Vector3d());
    private ParticleColor color = ParticleColor.WHITE;

    public LineBoundingBoxParticle(Supplier<GizmoLineParticle> lineSupplier) {
        this.lines = new Line[]{
                createLine(lineSupplier, 0.5, 0, 0, Axis.X),
                createLine(lineSupplier, 0.5, 0, 1, Axis.X),
                createLine(lineSupplier, 0.5, 1, 0, Axis.X),
                createLine(lineSupplier, 0.5, 1, 1, Axis.X),
                createLine(lineSupplier, 0, 0.5, 0, Axis.Y),
                createLine(lineSupplier, 0, 0.5, 1, Axis.Y),
                createLine(lineSupplier, 1, 0.5, 0, Axis.Y),
                createLine(lineSupplier, 1, 0.5, 1, Axis.Y),
                createLine(lineSupplier, 0, 0, 0.5, Axis.Z),
                createLine(lineSupplier, 1, 0, 0.5, Axis.Z),
                createLine(lineSupplier, 0, 1, 0.5, Axis.Z),
                createLine(lineSupplier, 1, 1, 0.5, Axis.Z)
        };
    }

    private Line createLine(Supplier<GizmoLineParticle> lineSupplier, double x, double y, double z, Axis axis) {
        return new Line(lineSupplier, axis, new Vector3d(x, y, z));
    }

    @Override
    public @NotNull BoundingBox getBoundingBox() {
        return box;
    }

    @Override
    public void setBoundingBox(@NotNull BoundingBox box) {
        if (!this.box.equals(box)) {
            this.box = BoundingBox.of(box);
            refresh();
        }
    }

    @Override
    public double getLineWidth() {
        return lines[0].particle.getWidth();
    }

    @Override
    public void setLineWidth(double lineWidth) {
        for (Line line : lines) {
            line.particle.setWidth(lineWidth);
        }
    }

    @Override
    public @NotNull ParticleColor getColor() {
        return color;
    }

    @Override
    public void setColor(@NotNull ParticleColor color) {
        this.color = color;
        for (Line line : lines) {
            line.particle.setColor(color);
        }
    }

    private void refresh() {
        for (Line line : lines) {
            line.refresh(box);
        }
    }

    @Override
    public void showGizmo() {
        for (Line line : lines) {
            line.particle.showGizmo();
        }
    }

    @Override
    public void updateGizmo() {
        for (Line line : lines) {
            line.particle.updateGizmo();
        }
    }

    @Override
    public void hideGizmo() {
        for (Line line : lines) {
            line.particle.hideGizmo();
        }
    }

    private static class Line {
        private final Axis axis;
        private final Vector3dc offset;
        private final GizmoLineParticle particle;
        private final Vector3d position = new Vector3d();
        private final Vector3d size = new Vector3d();

        private Line(Supplier<GizmoLineParticle> lineSupplier, Axis axis, Vector3dc offset) {
            this.axis = axis;
            this.offset = offset;
            this.particle = lineSupplier.get();
            this.particle.setAxis(axis);
        }

        public void refresh(BoundingBox box) {
            Vector3dc min = box.getMinPosition();
            Vector3dc max = box.getMaxPosition();
            max.sub(min, size);
            min.fma(offset, size, position);
            particle.setCenter(position);
            particle.setLength(axis.getDirection().dot(size) + particle.getWidth());
        }
    }
}
