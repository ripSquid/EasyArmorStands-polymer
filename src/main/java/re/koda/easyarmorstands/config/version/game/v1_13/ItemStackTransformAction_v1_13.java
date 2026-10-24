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
package re.koda.easyarmorstands.config.version.game.v1_13;

import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.NodePath;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.transformation.TransformAction;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ItemStackTransformAction_v1_13 implements TransformAction {
    private static final String[] COLORS = new String[]{
            "white",
            "orange",
            "magenta",
            "light_blue",
            "yellow",
            "lime",
            "pink",
            "gray",
            "light_gray",
            "cyan",
            "purple",
            "blue",
            "brown",
            "green",
            "red",
            "black"
    };

    private static final List<String> COLORED_TYPES = Arrays.asList(
            "banner",
            "wool",
            "concrete",
            "stained_glass",
            "stained_glass_pane"
    );

    @Override
    public Object[] visitPath(NodePath path, ConfigurationNode value) throws SerializationException {
        ConfigurationNode typeNode = value.node("type");
        ConfigurationNode dataNode = value.node("data");

        if (processColored(typeNode, dataNode)) {
            return null;
        }

        if (processBoneMeal(typeNode, dataNode)) {
            return null;
        }

        if (processPlayerHead(typeNode, dataNode)) {
            return null;
        }

        if (processReplacement(typeNode, "step", "stone_slab")) {
            return null;
        }

        if (processReplacement(typeNode, "iron_fence", "iron_bars")) {
            return null;
        }

        if (processReplacement(typeNode, "double_plant", "sunflower")) {
            return null;
        }

        return null;
    }

    private boolean processColored(ConfigurationNode typeNode, ConfigurationNode dataNode) {
        String type = typeNode.getString();
        if (dataNode.virtual()) {
            return false;
        }
        int data = dataNode.getInt();
        if (type != null && data >= 0 && data < COLORS.length &&
                COLORED_TYPES.contains(type.toLowerCase(Locale.ROOT))) {
            String nameWithColor = COLORS[data] + "_" + type;
            typeNode.raw(nameWithColor.toLowerCase(Locale.ROOT));
            dataNode.raw(null);
            return true;
        }
        return false;
    }

    private boolean processBoneMeal(ConfigurationNode typeNode, ConfigurationNode dataNode) throws SerializationException {
        String type = typeNode.getString();
        if (dataNode.virtual()) {
            return false;
        }
        int data = dataNode.getInt();
        if (type != null && type.equalsIgnoreCase("ink_sack") && data == 15) {
            typeNode.raw("bone_meal");
            dataNode.raw(null);
            return true;
        }
        return false;
    }

    private boolean processPlayerHead(ConfigurationNode typeNode, ConfigurationNode dataNode) throws SerializationException {
        String type = typeNode.getString();
        if (dataNode.virtual()) {
            return false;
        }
        int data = dataNode.getInt();
        if (type != null && type.equalsIgnoreCase("skull_item") && data == 3) {
            typeNode.raw("player_head");
            dataNode.raw(null);
            return true;
        }
        return false;
    }

    private boolean processReplacement(ConfigurationNode node, String from, String to) throws SerializationException {
        String value = node.getString();
        if (Objects.equals(value, from)) {
            node.set(to);
            return true;
        }
        return false;
    }
}
