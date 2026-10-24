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
package re.koda.easyarmorstands.modded.command;

import re.koda.easyarmorstands.command.parser.ArgumentParserProvider;
import re.koda.easyarmorstands.command.sender.EasCommandSender;
import re.koda.easyarmorstands.command.util.MultipleEntitySelector;
import re.koda.easyarmorstands.command.util.MultiplePlayerSelector;
import re.koda.easyarmorstands.command.util.SingleEntitySelector;
import re.koda.easyarmorstands.platform.entity.Entity;
import re.koda.easyarmorstands.platform.entity.Player;
import re.koda.easyarmorstands.platform.modded.ModdedPlatform;
import re.koda.easyarmorstands.platform.modded.entity.ModdedEntity;
import re.koda.easyarmorstands.platform.modded.entity.ModdedPlayer;
import re.koda.easyarmorstands.platform.modded.world.ModdedWorld;
import re.koda.easyarmorstands.platform.util.Location;
import re.koda.easyarmorstands.platform.util.MappedCollection;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.incendo.cloud.minecraft.modded.ModdedCommandContextKeys;
import org.incendo.cloud.minecraft.modded.parser.VanillaArgumentParsers;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ParserDescriptor;
import org.joml.Vector3d;

import java.util.Collection;

import static java.util.concurrent.CompletableFuture.completedFuture;

public class ModdedArgumentParserProvider implements ArgumentParserProvider {
    private final ModdedPlatform platform;

    public ModdedArgumentParserProvider(ModdedPlatform platform) {
        this.platform = platform;
    }

    @Override
    public <C extends EasCommandSender> ParserDescriptor<C, Location> locationParser() {
        return VanillaArgumentParsers.<C>vec3Parser(true).flatMapSuccess(Location.class,
                (context, coordinates) -> {
                    CommandSourceStack stack = (CommandSourceStack) context.get(ModdedCommandContextKeys.SHARED_SUGGESTION_PROVIDER);
                    ServerLevel level = stack.getLevel();
                    Vec3 position = coordinates.wrappedCoordinates().getPosition(stack);
                    Vec2 rotation = coordinates.wrappedCoordinates().getRotation(stack);
                    return ArgumentParseResult.successFuture(Location.of(
                            ModdedWorld.fromNative(platform, level),
                            new Vector3d(position.x, position.y, position.z),
                            rotation.y, rotation.x));
                });
    }

    @Override
    public <C extends EasCommandSender> ParserDescriptor<C, SingleEntitySelector> singleEntitySelector() {
        return VanillaArgumentParsers.<C>singleEntitySelectorParser().mapSuccess(SingleEntitySelector.class,
                (_, selector) -> {
                    Entity entity = ModdedEntity.fromNative(platform, selector.single());
                    return completedFuture(new SingleEntitySelector(entity));
                });
    }

    @Override
    public <C extends EasCommandSender> ParserDescriptor<C, MultipleEntitySelector> multipleEntitySelector() {
        return VanillaArgumentParsers.<C>multipleEntitySelectorParser().mapSuccess(MultipleEntitySelector.class,
                (_, selector) -> {
                    Collection<Entity> entities = new MappedCollection<>(selector.values(),
                            e -> ModdedEntity.fromNative(platform, e));
                    return completedFuture(new MultipleEntitySelector(entities));
                });
    }

    @Override
    public <C extends EasCommandSender> ParserDescriptor<C, MultiplePlayerSelector> multiplePlayerSelector() {
        return VanillaArgumentParsers.<C>multiplePlayerSelectorParser().mapSuccess(MultiplePlayerSelector.class,
                (_, selector) -> {
                    Collection<Player> entities = new MappedCollection<>(selector.values(),
                            e -> ModdedPlayer.fromNative(platform, e));
                    return completedFuture(new MultiplePlayerSelector(entities));
                });
    }
}
