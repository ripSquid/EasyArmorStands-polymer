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
import org.joml.Vector3d;
import org.joml.Vector3dc;

public class Cursor3D {
    private final Session session;
    private final PointParticle particle;

    private final Vector3d cursor = new Vector3d();
    private final Vector3d current = new Vector3d();

    public Cursor3D(Session session) {
        this.session = session;
        this.particle = session.particleProvider().createPoint();
    }

    public void start(EnterContext context, Vector3dc cursor) {
        context.eyeRay().inverseMatrix().transformPosition(cursor, this.cursor);
        this.current.set(cursor);
        particle.setPosition(current);
        particle.setColor(ParticleColor.YELLOW);
        session.addParticle(particle);
    }

    public void update(UpdateContext context) {
        context.eyeRay().matrix().transformPosition(cursor, current);
        particle.setPosition(current);
    }

    public Vector3dc get() {
        return current;
    }

    public void stop() {
        session.removeParticle(particle);
    }
}
