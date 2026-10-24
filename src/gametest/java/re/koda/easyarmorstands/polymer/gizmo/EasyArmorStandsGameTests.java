/*
 * EasyArmorStands-Polymer - a Polymer based Fabric port of EasyArmorStands
 * Copyright (C) 56738 and EasyArmorStands contributors (original work)
 * Copyright (C) 2026 ripsquid
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
package re.koda.easyarmorstands.polymer.gizmo;

import eu.pb4.polymer.virtualentity.api.ElementHolder;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import re.koda.easyarmorstands.EasyArmorStandsCommon;
import re.koda.easyarmorstands.api.editor.layer.ElementSelectionLayer;
import re.koda.easyarmorstands.api.element.Element;
import re.koda.easyarmorstands.api.element.EntityElement;
import re.koda.easyarmorstands.fabric.api.EasyArmorStandsFabricHolder;
import re.koda.easyarmorstands.gizmo.api.GizmoFactory;
import re.koda.easyarmorstands.modded.EasyArmorStandsModdedImpl;
import re.koda.easyarmorstands.particle.GizmoParticleProvider;
import re.koda.easyarmorstands.platform.entity.Player;
import re.koda.easyarmorstands.platform.modded.entity.ModdedEntity;
import re.koda.easyarmorstands.platform.modded.entity.ModdedPlayer;
import re.koda.easyarmorstands.session.SessionImpl;

import net.fabricmc.fabric.api.permission.v1.PermissionContext;
import net.fabricmc.fabric.api.permission.v1.PermissionEvents;
import net.fabricmc.fabric.api.permission.v1.PermissionNode;
import net.minecraft.resources.Identifier;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;

public class EasyArmorStandsGameTests {
    /** Test permission provider for the Fabric Permission API: player UUID -> permission -> value. */
    private static final Map<UUID, Map<Identifier, Boolean>> PERMISSIONS = new ConcurrentHashMap<>();

    static {
        PermissionEvents.ON_REQUEST.register(new PermissionEvents.OnRequest() {
            @Override
            public <T> T handlePermissionRequest(PermissionContext context, PermissionNode<T> node) {
                Map<Identifier, Boolean> permissions = PERMISSIONS.get(context.uuid());
                if (permissions == null) {
                    return null;
                }
                Boolean value = permissions.get(node.key());
                return value != null ? node.cast(value) : null;
            }
        });
    }

    private static EasyArmorStandsModdedImpl eas() {
        return (EasyArmorStandsModdedImpl) EasyArmorStandsFabricHolder.getInstance();
    }

    private static Player wrap(ServerPlayer player) {
        return ModdedPlayer.fromNative(eas().platform(), player);
    }

    private static ServerPlayer createEditor(GameTestHelper helper) {
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        MinecraftServer server = helper.getLevel().getServer();
        server.getPlayerList().op(new NameAndId(player.getGameProfile()));
        Vec3 pos = helper.absoluteVec(new Vec3(1.5, 1, 1.5));
        player.snapTo(pos.x, pos.y, pos.z, -45, 10);
        return player;
    }

    private static void run(GameTestHelper helper, ServerPlayer player, String command) {
        MinecraftServer server = helper.getLevel().getServer();
        server.getCommands().performPrefixedCommand(player.createCommandSourceStack().withPermission(LevelBasedPermissionSet.OWNER), command);
    }

    private static int countGizmoElements(SessionImpl session) throws ReflectiveOperationException {
        GizmoParticleProvider provider = (GizmoParticleProvider) session.particleProvider();
        Field factoryField = GizmoParticleProvider.class.getDeclaredField("gizmoFactory");
        factoryField.setAccessible(true);
        GizmoFactory factory = (GizmoFactory) factoryField.get(provider);
        Field holderField = PolymerGizmoFactory.class.getDeclaredField("holder");
        holderField.setAccessible(true);
        ElementHolder holder = (ElementHolder) holderField.get(factory);
        if (holder == null || holder.getWatchingPlayers().isEmpty()) {
            return 0;
        }
        return holder.getElements().size();
    }

    @GameTest
    public void giveTool(GameTestHelper helper) {
        ServerPlayer player = createEditor(helper);
        run(helper, player, "eas give");
        ItemStack tool = player.getInventory().getSelectedItem();
        helper.assertTrue(eas().isTool(tool), Component.literal("/eas give did not give the editor tool, got " + tool));
        player.connection.disconnect(Component.empty());
        helper.succeed();
    }

    @GameTest(maxTicks = 100)
    public void editArmorStand(GameTestHelper helper) {
        ServerPlayer player = createEditor(helper);
        ArmorStand stand = helper.spawn(EntityTypes.ARMOR_STAND, new Vec3(3.5, 1, 3.5));
        run(helper, player, "eas give");
        player.setItemInHand(InteractionHand.MAIN_HAND, player.getInventory().getSelectedItem());
        EasyArmorStandsCommon eas = eas();
        eas.sessionManager().updateHeldItem(wrap(player));

        helper.runAfterDelay(2, () -> {
            SessionImpl session = eas.sessionManager().getSession(wrap(player));
            helper.assertTrue(session != null, Component.literal("holding the tool did not start a session"));
            helper.assertTrue(session.findLayer(ElementSelectionLayer.class) != null, Component.literal("no selection layer"));

            run(helper, player, "eas select @e[type=minecraft:armor_stand,limit=1,sort=nearest]");
            Element element = session.getElement();
            helper.assertTrue(element instanceof EntityElement<?> e
                            && ModdedEntity.toNative(e.getEntity()) == stand,
                    Component.literal("/eas select did not select the armor stand, got " + element));

            run(helper, player, "eas name set <red>Hello");
            helper.assertTrue(stand.getCustomName() != null && stand.getCustomName().getString().equals("Hello"),
                    Component.literal("/eas name set did not rename, got " + stand.getCustomName()));
            run(helper, player, "eas undo");
            helper.assertTrue(stand.getCustomName() == null, Component.literal("/eas undo did not revert the name"));
            run(helper, player, "eas redo");
            helper.assertTrue(stand.getCustomName() != null, Component.literal("/eas redo did not reapply the name"));

            run(helper, player, "eas clone");
            List<ArmorStand> stands = helper.getLevel().getEntitiesOfClass(ArmorStand.class, stand.getBoundingBox().inflate(4));
            helper.assertTrue(stands.size() == 2, Component.literal("/eas clone should create a second armor stand, found " + stands.size()));
        });

        helper.runAfterDelay(6, () -> {
            SessionImpl session = eas.sessionManager().getSession(wrap(player));
            int elements;
            try {
                elements = countGizmoElements(session);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
            // armor stand root layer shows a position point and 6 bone lines
            helper.assertTrue(elements >= 7, Component.literal("expected gizmo display elements for the armor stand bones, got " + elements));
            player.connection.disconnect(Component.empty());
            helper.assertTrue(eas.sessionManager().getSession(wrap(player)) == null, Component.literal("session should stop when the player leaves"));
            helper.succeed();
        });
    }

    @GameTest
    public void spawnMenuCanBeDisabled(GameTestHelper helper) {
        ServerPlayer player = createEditor(helper);
        EasyArmorStandsCommon eas = eas();
        boolean previous = eas.getConfiguration().editor.spawnMenu;
        try {
            eas.getConfiguration().editor.spawnMenu = false;
            run(helper, player, "eas spawn");
            helper.assertTrue(player.containerMenu == player.inventoryMenu,
                    Component.literal("/eas spawn opened a menu although the spawn menu is disabled"));
            eas.getConfiguration().editor.spawnMenu = true;
            run(helper, player, "eas spawn");
            helper.assertTrue(player.containerMenu != player.inventoryMenu,
                    Component.literal("/eas spawn did not open the spawn menu"));
        } finally {
            eas.getConfiguration().editor.spawnMenu = previous;
            player.connection.disconnect(Component.empty());
        }
        helper.succeed();
    }

    @GameTest
    public void fabricPermissionApiGrant(GameTestHelper helper) {
        // not an operator, only granted the permission through the Fabric Permission API
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        PERMISSIONS.put(player.getUUID(), Map.of(Identifier.fromNamespaceAndPath("easyarmorstands", "give"), true));
        try {
            helper.getLevel().getServer().getCommands().performPrefixedCommand(player.createCommandSourceStack(), "eas give");
            helper.assertTrue(eas().isTool(player.getInventory().getSelectedItem()),
                    Component.literal("easyarmorstands:give granted through the Fabric Permission API was not honored"));
        } finally {
            PERMISSIONS.remove(player.getUUID());
            player.connection.disconnect(Component.empty());
        }
        helper.succeed();
    }

    @GameTest
    public void fabricPermissionApiDeny(GameTestHelper helper) {
        // operator, but spawning is denied through the Fabric Permission API
        ServerPlayer player = createEditor(helper);
        PERMISSIONS.put(player.getUUID(), Map.of(Identifier.fromNamespaceAndPath("easyarmorstands", "spawn"), false));
        try {
            helper.getLevel().getServer().getCommands().performPrefixedCommand(player.createCommandSourceStack(), "eas spawn");
            helper.assertTrue(player.containerMenu == player.inventoryMenu,
                    Component.literal("easyarmorstands:spawn denied through the Fabric Permission API was not honored"));
        } finally {
            PERMISSIONS.remove(player.getUUID());
            player.connection.disconnect(Component.empty());
        }
        helper.succeed();
    }
}
