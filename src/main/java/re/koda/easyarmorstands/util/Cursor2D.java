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
package re.koda.easyarmorstands.util;

import re.koda.easyarmorstands.api.editor.Session;
import re.koda.easyarmorstands.api.editor.context.EnterContext;
import re.koda.easyarmorstands.api.editor.context.UpdateContext;
import re.koda.easyarmorstands.api.particle.ParticleColor;
import re.koda.easyarmorstands.api.particle.PointParticle;
import org.joml.Vector2d;
import org.joml.Vector3d;
import org.joml.Vector3dc;

public class Cursor2D {
    private final Session session;
    private final PointParticle particle;

    private final Vector3d origin = new Vector3d();
    private final Vector3d normal = new Vector3d();
    private final Vector3d current = new Vector3d();
    private final Vector2d cursor = new Vector2d();

    public Cursor2D(Session session) {
        this.session = session;
        this.particle = session.particleProvider().createPoint();
    }

    public void start(EnterContext context, Vector3dc origin, Vector3dc cursor, Vector3dc normal) {
        this.origin.set(origin);
        this.current.set(cursor);
        this.normal.set(normal);
        particle.setPosition(current);
        particle.setColor(ParticleColor.YELLOW);
        session.addParticle(particle);
        Vector3d cursor3 = new Vector3d();
        context.eyeRay().inverseMatrix().transformPosition(cursor, cursor3);
        this.cursor.x = cursor3.x;
        this.cursor.y = cursor3.y;
    }

    public void update(UpdateContext context) {
        Vector3dc intersection = context.eyeRay(cursor).intersectPlane(origin, normal, Double.POSITIVE_INFINITY);
        if (intersection != null) {
            current.set(intersection);
        }
        particle.setPosition(current);
    }

    public Vector3dc get() {
        return current;
    }

    public void stop() {
        session.removeParticle(particle);
    }
}
