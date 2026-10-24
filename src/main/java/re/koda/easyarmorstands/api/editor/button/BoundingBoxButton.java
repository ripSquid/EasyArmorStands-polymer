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
import re.koda.easyarmorstands.api.particle.BoundingBoxParticle;
import re.koda.easyarmorstands.api.particle.ParticleColor;
import re.koda.easyarmorstands.api.particle.PointParticle;
import re.koda.easyarmorstands.api.util.BoundingBox;
import re.koda.easyarmorstands.api.util.BoundingBoxProvider;
import re.koda.easyarmorstands.api.util.PositionProvider;
import re.koda.easyarmorstands.api.util.RotationProvider;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaterniond;
import org.joml.Vector3d;
import org.joml.Vector3dc;

import java.util.function.Consumer;

public final class BoundingBoxButton implements Button {
    private final Session session;
    private final BoundingBoxProvider boxProvider;
    private final PositionProvider positionProvider;
    private final RotationProvider rotationProvider;
    private final Vector3d position = new Vector3d();
    private final Quaterniond rotation = new Quaterniond();
    private final PointParticle pointParticle;
    private final BoundingBoxParticle boxParticle;
    private double scale;
    private BoundingBox box;
    private boolean previewVisible;
    private boolean boxVisible;

    public BoundingBoxButton(@NotNull Session session, @NotNull BoundingBoxProvider boxProvider, @NotNull PositionProvider positionProvider, @NotNull RotationProvider rotationProvider) {
        this.session = session;
        this.pointParticle = session.particleProvider().createPoint();
        this.boxProvider = boxProvider;
        this.positionProvider = positionProvider;
        this.rotationProvider = rotationProvider;
        this.pointParticle.setBillboard(false);
        this.boxParticle = session.particleProvider().createAxisAlignedBox();
    }

    @Override
    public void update() {
        box = BoundingBox.of(boxProvider.getBoundingBox());
        position.set(positionProvider.getPosition());
        rotation.set(rotationProvider.getRotation());
        scale = session.getScale(position);
    }

    @Override
    public void intersect(@NotNull EyeRay ray, @NotNull Consumer<@NotNull ButtonResult> results) {
        Vector3dc intersection = ray.intersectPoint(position, scale);
        if (intersection != null) {
            results.accept(ButtonResult.of(intersection));
        }
        intersection = ray.intersectBox(box);
        if (intersection != null) {
            results.accept(ButtonResult.of(intersection, -1));
        }
    }

    @Override
    public void updatePreview(boolean focused, boolean selected) {
        pointParticle.setPosition(position);
        pointParticle.setRotation(rotation);
        pointParticle.setColor(Button.color(focused, selected, ParticleColor.WHITE));
        pointParticle.setSize(scale / 16);
        boxParticle.setBoundingBox(box);
        boxParticle.setColor(Button.color(focused, selected, ParticleColor.WHITE));
        boolean showBox = (focused || selected) && !box.getMinPosition().equals(box.getMaxPosition());
        if (previewVisible && showBox != boxVisible) {
            if (showBox) {
                session.addParticle(boxParticle);
            } else {
                session.removeParticle(boxParticle);
            }
        }
        boxVisible = showBox;
    }

    @Override
    public void showPreview() {
        previewVisible = true;
        session.addParticle(pointParticle);
        if (boxVisible) {
            session.addParticle(boxParticle);
        }
    }

    @Override
    public void hidePreview() {
        previewVisible = false;
        session.removeParticle(pointParticle);
        if (boxVisible) {
            session.removeParticle(boxParticle);
        }
    }
}
