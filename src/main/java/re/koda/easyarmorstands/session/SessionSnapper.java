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
package re.koda.easyarmorstands.session;

import re.koda.easyarmorstands.api.editor.Snapper;
import re.koda.easyarmorstands.platform.entity.Player;
import re.koda.easyarmorstands.util.Util;

public class SessionSnapper implements Snapper {
    public static final double DEFAULT_OFFSET_INCREMENT = 1.0 / 32;
    public static final double DEFAULT_POSITION_INCREMENT = 1.0 / 32;
    public static final double DEFAULT_ANGLE_INCREMENT = 2 * Math.PI / 256;

    private final Player player;
    private double offsetIncrement = DEFAULT_OFFSET_INCREMENT;
    private double positionIncrement = DEFAULT_POSITION_INCREMENT;
    private double angleIncrement = DEFAULT_ANGLE_INCREMENT;

    public SessionSnapper(Player player) {
        this.player = player;
    }

    private double snap(double value, double increment) {
        if (player.isSneaking()) {
            return value;
        }
        return Util.snap(value, increment);
    }

    @Override
    public double snapOffset(double offset) {
        return snap(offset, offsetIncrement);
    }

    @Override
    public double snapPosition(double position) {
        return snap(position, positionIncrement);
    }

    @Override
    public double snapAngle(double angle) {
        return snap(angle, angleIncrement);
    }

    public double getOffsetIncrement() {
        return offsetIncrement;
    }

    public void setOffsetIncrement(double offsetIncrement) {
        this.offsetIncrement = offsetIncrement;
    }

    public double getPositionIncrement() {
        return positionIncrement;
    }

    public void setPositionIncrement(double positionIncrement) {
        this.positionIncrement = positionIncrement;
    }

    public double getAngleIncrement() {
        return angleIncrement;
    }

    public void setAngleIncrement(double angleIncrement) {
        this.angleIncrement = angleIncrement;
    }
}
