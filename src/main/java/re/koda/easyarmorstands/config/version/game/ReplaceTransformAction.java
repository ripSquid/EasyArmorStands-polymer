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
package re.koda.easyarmorstands.config.version.game;

import io.leangen.geantyref.TypeToken;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.NodePath;
import org.spongepowered.configurate.transformation.TransformAction;

import java.util.Objects;

public class ReplaceTransformAction<T> implements TransformAction {
    private final TypeToken<T> type;
    private final T from;
    private final T to;

    private ReplaceTransformAction(TypeToken<T> type, T from, T to) {
        this.type = type;
        this.from = from;
        this.to = to;
    }

    public static <T> ReplaceTransformAction<T> replace(TypeToken<T> type, T from, T to) {
        return new ReplaceTransformAction<>(type, from, to);
    }

    public static <T> ReplaceTransformAction<T> replace(Class<T> type, T from, T to) {
        return replace(TypeToken.get(type), from, to);
    }

    public static ReplaceTransformAction<String> replaceString(String from, String to) {
        return replace(String.class, from, to);
    }

    @Override
    public Object[] visitPath(NodePath path, ConfigurationNode node) throws ConfigurateException {
        if (Objects.equals(node.get(type), from)) {
            node.set(type, to);
        }
        return null;
    }
}
