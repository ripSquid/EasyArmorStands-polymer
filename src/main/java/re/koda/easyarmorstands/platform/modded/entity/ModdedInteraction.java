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
package re.koda.easyarmorstands.platform.modded.entity;

import re.koda.easyarmorstands.platform.entity.Interaction;
import re.koda.easyarmorstands.platform.modded.ModdedPlatform;

public interface ModdedInteraction extends Interaction, ModdedEntity {
    @Override
    net.minecraft.world.entity.Interaction getNative();

    static ModdedInteraction fromNative(ModdedPlatform platform, net.minecraft.world.entity.Interaction entity) {
        return new ModdedInteractionImpl(platform, entity);
    }

    static net.minecraft.world.entity.Interaction toNative(Interaction entity) {
        return ((ModdedInteraction) entity).getNative();
    }

    @Override
    default float getInteractionWidth() {
        return getNative().getWidth();
    }

    @Override
    default void setInteractionWidth(float width) {
        getNative().setWidth(width);
    }

    @Override
    default float getInteractionHeight() {
        return getNative().getHeight();
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    default void setInteractionHeight(float height) {
        getNative().setHeight(height);
    }

    @Override
    default boolean isResponsive() {
        return getNative().getResponse();
    }

    @Override
    default void setResponsive(boolean responsive) {
        getNative().setResponse(responsive);
    }
}
