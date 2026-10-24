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
package re.koda.easyarmorstands.config.version;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;

public class Version implements Comparable<Version> {
    private static final Comparator<Version> COMPARATOR =
            Comparator.comparingInt(Version::getMajor)
                    .thenComparingInt(Version::getMinor)
                    .thenComparingInt(Version::getPatch);

    private final int major;
    private final int minor;
    private final int patch;

    public Version(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public static @Nullable Version tryParse(@Nullable String version) {
        if (version == null) {
            return null;
        }
        try {
            return parse(version);
        } catch (Exception e) {
            return null;
        }
    }

    public static Version parse(String version) {
        String[] parts = version.split("\\D", 4);
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid version: " + version);
        }

        int major = parseIntOrZero(parts[0]);
        int minor = parseIntOrZero(parts[1]);
        int patch;
        if (minor != 0 && parts.length >= 3) {
            patch = parseIntOrZero(parts[2]);
        } else {
            patch = 0;
        }
        return new Version(major, minor, patch);
    }

    private static int parseIntOrZero(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getPatch() {
        return patch;
    }

    @Override
    public int compareTo(@NotNull Version other) {
        return COMPARATOR.compare(this, other);
    }

    @Override
    public String toString() {
        if (patch != 0) {
            return major + "." + minor + "." + patch;
        } else {
            return major + "." + minor;
        }
    }
}
