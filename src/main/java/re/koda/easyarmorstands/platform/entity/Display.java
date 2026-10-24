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

import re.koda.easyarmorstands.platform.color.RGBColor;
import org.joml.Quaternionfc;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

public interface Display extends Entity {
    float getDisplayWidth();

    void setDisplayWidth(float width);

    float getDisplayHeight();

    void setDisplayHeight(float height);

    Vector3fc getTranslation();

    void setTranslation(Vector3fc translation);

    Quaternionfc getLeftRotation();

    void setLeftRotation(Quaternionfc rotation);

    Vector3fc getScale();

    void setScale(Vector3fc scale);

    Quaternionfc getRightRotation();

    void setRightRotation(Quaternionfc rotation);

    Billboard getBillboard();

    void setBillboard(Billboard billboard);

    @Nullable Brightness getBrightness();

    void setBrightness(@Nullable Brightness brightness);

    @Nullable RGBColor getGlowColorOverride();

    void setGlowColorOverride(@Nullable RGBColor glowColor);

    float getViewRange();

    void setViewRange(float range);

    int getInterpolationDuration();

    void setInterpolationDuration(int duration);

    int getTeleportDuration();

    void setTeleportDuration(int duration);

    void setInterpolationDelay(int delay);

    enum Billboard {
        FIXED,
        VERTICAL,
        HORIZONTAL,
        CENTER,
    }

    record Brightness(int blockLight, int skyLight) {
    }
}
