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
package re.koda.easyarmorstands.api;

import re.koda.easyarmorstands.platform.entity.ArmorStand;
import re.koda.easyarmorstands.platform.util.Rotations;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.Function;

public enum ArmorStandPart {
    HEAD(
            ArmorStand::getHeadPose,
            ArmorStand::setHeadPose,
            Component.translatable("easyarmorstands.armor-stand-part.head")
    ),
    BODY(
            ArmorStand::getBodyPose,
            ArmorStand::setBodyPose,
            Component.translatable("easyarmorstands.armor-stand-part.body")
    ),
    LEFT_ARM(
            ArmorStand::getLeftArmPose,
            ArmorStand::setLeftArmPose,
            Component.translatable("easyarmorstands.armor-stand-part.left-arm")
    ),
    RIGHT_ARM(
            ArmorStand::getRightArmPose,
            ArmorStand::setRightArmPose,
            Component.translatable("easyarmorstands.armor-stand-part.right-arm")
    ),
    LEFT_LEG(
            ArmorStand::getLeftLegPose,
            ArmorStand::setLeftLegPose,
            Component.translatable("easyarmorstands.armor-stand-part.left-leg")
    ),
    RIGHT_LEG(
            ArmorStand::getRightLegPose,
            ArmorStand::setRightLegPose,
            Component.translatable("easyarmorstands.armor-stand-part.right-leg")
    );

    private final Function<ArmorStand, Rotations> poseGetter;
    private final BiConsumer<ArmorStand, Rotations> poseSetter;
    private final Component displayName;

    ArmorStandPart(Function<ArmorStand, Rotations> poseGetter, BiConsumer<ArmorStand, Rotations> poseSetter, Component displayName) {
        this.poseGetter = poseGetter;
        this.poseSetter = poseSetter;
        this.displayName = displayName;
    }

    public @NotNull Rotations getPose(@NotNull ArmorStand armorStand) {
        return poseGetter.apply(armorStand);
    }

    public void setPose(@NotNull ArmorStand armorStand, @NotNull Rotations pose) {
        poseSetter.accept(armorStand, pose);
    }

    public Component displayName() {
        return displayName;
    }
}
