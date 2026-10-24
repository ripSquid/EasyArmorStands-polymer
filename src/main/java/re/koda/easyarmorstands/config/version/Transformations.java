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

import re.koda.easyarmorstands.config.MessageConfig;
import re.koda.easyarmorstands.config.version.game.ReplaceTransformAction;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;
import org.spongepowered.configurate.transformation.TransformAction;

import static org.spongepowered.configurate.NodePath.path;

public class Transformations {
    public static ConfigurationTransformation.Versioned config() {
        return ConfigurationTransformation.versionedBuilder()
                .versionKey("_version", "config")
                .addVersion(0, initialTransform())
                .addVersion(1, ConfigurationTransformation.builder()
                        .addAction(path("integration", "Towny", "allow-wilderness"), TransformAction.remove())
                        .build())
                .addVersion(2, ConfigurationTransformation.builder()
                        .addAction(path("editor", "tool", "description"), TransformAction.remove())
                        .build())
                .addVersion(3, ConfigurationTransformation.builder()
                        .addAction(path("editor", "input-hints"), (p, v) -> new Object[]{"editor", "input-hints", "enabled"})
                        .build())
                .addVersion(4, ConfigurationTransformation.builder()
                        .addAction(path("message", "server-side-translation"), TransformAction.remove())
                        .addAction(path("editor", "menu"), TransformAction.remove())
                        .build())
                .build();
    }

    public static ConfigurationTransformation.Versioned properties() {
        return ConfigurationTransformation.versionedBuilder()
                .versionKey("_version", "config")
                .addVersion(0, ConfigurationTransformation.builder()
                        .addAction(path("easyarmorstands:armor_stand/base_plate", "button", "data"),
                                ReplaceTransformAction.replace(int.class, -1, null))
                        .addAction(path("easyarmorstands:armor_stand/lock", "button", "data"),
                                ReplaceTransformAction.replace(int.class, -1, null))
                        .addAction(path("easyarmorstands:armor_stand/marker", "button", "data"),
                                ReplaceTransformAction.replace(int.class, -1, null))
                        .addAction(path("easyarmorstands:armor_stand/size", "button", "data"),
                                ReplaceTransformAction.replace(int.class, -1, null))
                        .build())
                .build();
    }

    public static ConfigurationTransformation initialTransform() {
        return ConfigurationTransformation.builder()
                .addAction(path("update-check"),
                        (path, value) -> new Object[]{"update-check", "enabled"})
                .addAction(path("tool"),
                        (path, value) -> new Object[]{"editor", "tool"})
                .addAction(path("editor-scale-min-distance"),
                        (path, value) -> new Object[]{"editor", "scale", "min-distance"})
                .addAction(path("editor-scale-max-distance"),
                        (path, value) -> new Object[]{"editor", "scale", "max-distance"})
                .addAction(path("editor-range"),
                        (path, value) -> new Object[]{"editor", "button", "range"})
                .addAction(path("editor-selection-range"),
                        (path, value) -> new Object[]{"editor", "selection", "range"})
                .addAction(path("editor-selection-limit"),
                        (path, value) -> new Object[]{"editor", "selection", "group", "limit"})
                .addAction(path("editor-selection-distance"),
                        (path, value) -> new Object[]{"editor", "selection", "group", "range"})
                .addAction(path("editor-button-limit"),
                        (path, value) -> new Object[]{"editor", "discovery", "limit"})
                .addAction(path("editor-look-threshold"),
                        (path, value) -> new Object[]{"editor", "button", "threshold"})
                .addAction(path("interpolation-ticks"),
                        (path, value) -> new Object[]{"editor", "button", "interpolation-ticks"})
                .addAction(path("menu-background"),
                        (path, value) -> new Object[]{"editor", "menu", "background"})
                .addAction(path("message-formats"),
                        (path, value) -> {
                            value.set(MessageConfig.formatType(), value.get(MessageConfig.formatType()));
                            return new Object[]{"message", "format"};
                        })
                .addAction(path("server-side-translation"),
                        (path, value) -> new Object[]{"message", "server-side-translation"})
                .addAction(path("swap-hands-button"), TransformAction.remove())
                .build();
    }
}
