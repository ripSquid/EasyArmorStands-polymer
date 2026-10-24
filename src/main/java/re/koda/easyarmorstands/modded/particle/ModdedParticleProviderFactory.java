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
package re.koda.easyarmorstands.modded.particle;

import re.koda.easyarmorstands.api.particle.ParticleProvider;
import re.koda.easyarmorstands.particle.GizmoParticleProvider;
import re.koda.easyarmorstands.particle.ParticleProviderFactory;
import re.koda.easyarmorstands.platform.entity.Entity;
import re.koda.easyarmorstands.platform.entity.Player;
import re.koda.easyarmorstands.platform.modded.entity.ModdedEntity;
import re.koda.easyarmorstands.platform.modded.entity.ModdedPlayer;
import re.koda.easyarmorstands.polymer.gizmo.PolymerGizmos;

public class ModdedParticleProviderFactory implements ParticleProviderFactory {
    private final PolymerGizmos gizmos;

    public ModdedParticleProviderFactory(PolymerGizmos gizmos) {
        this.gizmos = gizmos;
    }

    @Override
    public ParticleProvider createParticleProvider(Player player) {
        return new GizmoParticleProvider(gizmos.player(ModdedPlayer.toNative(player)));
    }

    @Override
    public boolean isParticle(Entity entity) {
        return gizmos.isGizmo(ModdedEntity.toNative(entity));
    }
}
