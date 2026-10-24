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
package re.koda.easyarmorstands.platform.entity;

import re.koda.easyarmorstands.platform.profile.Profile;
import re.koda.easyarmorstands.platform.util.MainHand;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.Nullable;

public interface Mannequin extends LivingEntity {
    MainHand getMainHand();

    void setMainHand(MainHand mainHand);

    Profile getProfile();

    void setProfile(Profile profile);

    boolean isImmovable();

    void setImmovable(boolean immovable);

    @Nullable Component getDescription();

    void setDescription(@Nullable Component description);

    Pose getPose();

    void setPose(Pose pose);

    boolean isCapeVisible();

    void setCapeVisible(boolean visible);

    boolean isJacketVisible();

    void setJacketVisible(boolean visible);

    boolean isLeftSleeveVisible();

    void setLeftSleeveVisible(boolean visible);

    boolean isRightSleeveVisible();

    void setRightSleeveVisible(boolean visible);

    boolean isLeftPantsVisible();

    void setLeftPantsVisible(boolean visible);

    boolean isRightPantsVisible();

    void setRightPantsVisible(boolean visible);

    boolean isHatVisible();

    void setHatVisible(boolean visible);
}
