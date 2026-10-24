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

import re.koda.easyarmorstands.EasyArmorStandsCommon;
import re.koda.easyarmorstands.api.editor.EyeRay;
import re.koda.easyarmorstands.api.editor.Session;
import re.koda.easyarmorstands.api.editor.button.Button;
import re.koda.easyarmorstands.api.editor.context.ClickContext;
import re.koda.easyarmorstands.api.editor.input.Category;
import re.koda.easyarmorstands.api.editor.input.Input;
import re.koda.easyarmorstands.api.editor.input.InputProvider;
import re.koda.easyarmorstands.api.editor.layer.ElementLayer;
import re.koda.easyarmorstands.api.editor.layer.Layer;
import re.koda.easyarmorstands.api.editor.layer.LayerProvider;
import re.koda.easyarmorstands.api.editor.node.ButtonNode;
import re.koda.easyarmorstands.api.editor.node.Node;
import re.koda.easyarmorstands.api.editor.node.NodeProvider;
import re.koda.easyarmorstands.api.element.Element;
import re.koda.easyarmorstands.api.element.SelectableElement;
import re.koda.easyarmorstands.api.particle.Particle;
import re.koda.easyarmorstands.api.particle.ParticleProvider;
import re.koda.easyarmorstands.api.property.PropertyContainer;
import re.koda.easyarmorstands.command.sender.EasPlayer;
import re.koda.easyarmorstands.config.EasConfig;
import re.koda.easyarmorstands.config.InputHintsConfig;
import re.koda.easyarmorstands.editor.button.ElementButtonHandler;
import re.koda.easyarmorstands.group.GroupMember;
import re.koda.easyarmorstands.group.layer.GroupRootLayer;
import re.koda.easyarmorstands.particle.EditorParticle;
import re.koda.easyarmorstands.platform.entity.Player;
import re.koda.easyarmorstands.platform.util.Location;
import re.koda.easyarmorstands.property.TrackedPropertyContainer;
import re.koda.easyarmorstands.session.context.AddContextImpl;
import re.koda.easyarmorstands.session.context.ClickContextImpl;
import re.koda.easyarmorstands.session.context.EnterContextImpl;
import re.koda.easyarmorstands.session.context.ExitContextImpl;
import re.koda.easyarmorstands.session.context.RemoveContextImpl;
import re.koda.easyarmorstands.session.context.UpdateContextImpl;
import re.koda.easyarmorstands.util.Util;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;
import org.joml.Math;
import org.joml.Matrix4dc;
import org.joml.Vector2dc;
import org.joml.Vector3d;
import org.joml.Vector3dc;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class SessionImpl implements Session {
    private static final Title.Times titleTimes = Title.Times.times(Duration.ZERO, Duration.ofSeconds(2), Duration.ofSeconds(1));
    private final LinkedList<Layer> layerStack = new LinkedList<>();
    private final EasyArmorStandsCommon eas;
    private final SessionSnapper snapper;
    private final Set<EditorParticle> particles = new HashSet<>();
    private final ParticleProvider particleProvider;
    private final NodeProvider nodeProvider = new NodeProviderImpl(this);
    private final LayerProvider layerProvider;
    private final InputProvider inputProvider;
    private final List<Input> inputs = new ArrayList<>();
    private EasPlayer player;
    private int clickTicks = 5;
    private boolean valid = true;
    private Component currentTitle = Component.empty();
    private Component currentSubtitle = Component.empty();
    private Component currentActionBar = Component.empty();
    private int overlayTicks;
    private boolean toolRequired;
    private Category inputCategory = Category.PRIMARY;
    private boolean hasSecondaryInputs;

    public SessionImpl(EasyArmorStandsCommon eas, EasPlayer player) {
        this.eas = eas;
        this.player = player;
        this.snapper = new SessionSnapper(player.get());
        this.particleProvider = eas.particleProviderFactory().createParticleProvider(player.get());
        this.layerProvider = new LayerProviderImpl(eas, this);
        this.inputProvider = new InputProviderImpl(eas, this);
    }

    @Override
    public Layer getLayer() {
        return layerStack.peek();
    }

    public @UnmodifiableView List<Layer> getLayerStack() {
        return Collections.unmodifiableList(layerStack);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Layer> @Nullable T findLayer(@NotNull Class<T> type) {
        for (Layer layer : layerStack) {
            if (type.isAssignableFrom(layer.getClass())) {
                return (T) layer;
            }
        }
        return null;
    }

    @Override
    public double getScale(Vector3dc position) {
        EasConfig config = eas.getConfiguration();
        Vector3dc eyePosition = player.get().eyeLocation().position();
        double minDistance = config.editor.scale.minDistance;
        double maxDistance = config.editor.scale.maxDistance;
        if (maxDistance <= minDistance) {
            return 1;
        }
        double distance = Math.clamp(minDistance, maxDistance, eyePosition.distance(position));
        return distance / minDistance;
    }

    @Override
    public void pushLayer(@NotNull Layer layer) {
        pushLayer(layer, null);
    }

    @Override
    public void pushLayer(@NotNull Layer layer, @Nullable Vector3dc cursor) {
        if (!valid) {
            return;
        }
        if (!layerStack.isEmpty()) {
            layerStack.peek().onExit(ExitContextImpl.INSTANCE);
        }
        layerStack.push(layer);
        layer.onAdd(AddContextImpl.INSTANCE);
        layer.onEnter(new EnterContextImpl(this, cursor));
    }

    @Override
    public void popLayer() {
        if (!valid) {
            return;
        }
        Layer removed = layerStack.pop();
        removed.onExit(ExitContextImpl.INSTANCE);
        removed.onRemove(RemoveContextImpl.INSTANCE);
        if (!layerStack.isEmpty()) {
            layerStack.peek().onEnter(new EnterContextImpl(this, null));
        }
    }

    @Override
    public void returnToLayer(@NotNull Layer target) {
        int count = layerStack.indexOf(target);
        for (int i = 0; i < count; i++) {
            popLayer();
        }
    }

    @Override
    public void clearLayers() {
        if (!valid) {
            return;
        }
        if (!layerStack.isEmpty()) {
            layerStack.peek().onExit(ExitContextImpl.INSTANCE);
        }
        for (Layer layer : layerStack) {
            layer.onRemove(RemoveContextImpl.INSTANCE);
        }
        layerStack.clear();
    }

    private boolean hasClickCooldown(ClickContext.Type type) {
        return type != ClickContext.Type.SWAP_HANDS && type != ClickContext.Type.DROP;
    }

    public boolean handleClick(ClickContextImpl context) {
        if (!valid) {
            return false;
        }
        Layer layer = layerStack.peek();
        if (layer == null) {
            return false;
        }

        if (hasClickCooldown(context.type()) && clickTicks > 0) {
            return true;
        }
        clickTicks = 5;

        if (layer.onClick(context)) {
            return true;
        }

        for (Input input : inputs) {
            if (input.category() == inputCategory && input.clickType() == context.type()) {
                input.execute(context);
                return true;
            }
        }

        return context.type() != ClickContext.Type.DROP;
    }

    @Override
    public Element getElement() {
        for (Layer layer : layerStack) {
            if (layer instanceof ElementLayer) {
                return ((ElementLayer) layer).getElement();
            }
        }
        return null;
    }

    @Override
    public @NotNull List<Element> getElements() {
        for (Layer layer : layerStack) {
            if (layer instanceof ElementLayer) {
                return Collections.singletonList(((ElementLayer) layer).getElement());
            } else if (layer instanceof GroupRootLayer) {
                return ((GroupRootLayer) layer).getGroup().getMembers().stream().map(GroupMember::getElement).collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }

    boolean update() {
        if (clickTicks > 0) {
            clickTicks--;
        }

        while (!layerStack.isEmpty() && !layerStack.peek().isValid()) {
            popLayer();
        }

        UpdateContextImpl context = new UpdateContextImpl(this);
        Layer currentLayer = layerStack.peek();
        if (currentLayer != null) {
            currentLayer.onUpdate(context);
        }
        for (Layer layer : layerStack) {
            if (layer != currentLayer) {
                layer.onInactiveUpdate(context);
            }
        }

        for (EditorParticle particle : particles) {
            particle.updateGizmo();
        }

        inputs.clear();
        inputs.addAll(context.getInputs());

        hasSecondaryInputs = false;
        for (Input input : inputs) {
            if (input.category() == Category.SECONDARY) {
                hasSecondaryInputs = true;
                break;
            }
        }

        inputCategory = Category.PRIMARY;
        if (player.get().isSneaking() && hasSecondaryInputs) {
            inputCategory = Category.SECONDARY;
        }

        Component pendingActionBar = createActionBar(context.getActionBar(), context.getAvailableInputs());

        if (currentLayer != null) {
            currentLayer.onLateUpdate(context);
        }

        updateOverlay(context, pendingActionBar);

        return player.get().isValid();
    }

    private void updateOverlay(UpdateContextImpl context, Component pendingActionBar) {
        // Resend everything once per second
        // Send changes immediately

        Component pendingTitle = context.getTitle();
        Component pendingSubtitle = context.getSubtitle();

        boolean resendOverlay = overlayTicks <= 0;
        if (resendOverlay) {
            overlayTicks = 20;
            player.sendTitlePart(TitlePart.TIMES, titleTimes);
        }
        overlayTicks--;

        if (resendOverlay || !Objects.equals(currentTitle, pendingTitle) || !Objects.equals(currentSubtitle, pendingSubtitle)) {
            currentTitle = pendingTitle;
            currentSubtitle = pendingSubtitle;
            player.sendTitlePart(TitlePart.SUBTITLE, currentSubtitle);
            player.sendTitlePart(TitlePart.TITLE, currentTitle);
        }

        if (resendOverlay || !Objects.equals(currentActionBar, pendingActionBar)) {
            currentActionBar = pendingActionBar;
            player.sendActionBar(currentActionBar);
        }
    }

    private Component createActionBar(Component value, Set<Input> availableInputs) {
        InputHintsConfig config = eas.getConfiguration().editor.inputHints;
        if (!config.enabled) {
            return value;
        }

        TextComponent.Builder builder = Component.text();
        builder.append(value);

        EnumSet<ClickContext.Type> seen = EnumSet.noneOf(ClickContext.Type.class);
        for (Input input : inputs) {
            if (input.category() != inputCategory) {
                continue;
            }

            ClickContext.Type clickType = input.clickType();
            if (seen.add(clickType)) {
                availableInputs.add(input);
                builder.append(createInput(config, getKey(config, clickType), input.name(), input.style()));
            }
        }

        if (inputCategory == Category.PRIMARY && hasSecondaryInputs) {
            builder.append(createInput(config, config.sneakKey, Component.translatable("easyarmorstands.input.more"), Style.style(NamedTextColor.GRAY)));
        }

        return builder.build();
    }

    private Component createInput(InputHintsConfig config, Component key, Component input, Style style) {
        TagResolver resolver = TagResolver.builder()
                .tag("key", Tag.selfClosingInserting(key))
                .tag("input", Tag.selfClosingInserting(input))
                .build();
        return EasyArmorStandsCommon.miniMessage().deserialize(config.format, resolver).applyFallbackStyle(style);
    }

    private Component getKey(InputHintsConfig config, ClickContext.Type type) {
        if (type == ClickContext.Type.LEFT_CLICK) {
            return config.leftClickKey;
        } else if (type == ClickContext.Type.RIGHT_CLICK) {
            return config.rightClickKey;
        } else if (type == ClickContext.Type.SWAP_HANDS) {
            return config.swapHandsKey;
        } else if (type == ClickContext.Type.DROP) {
            return config.dropKey;
        }
        return Component.empty();
    }

    void stop() {
        Layer currentLayer = layerStack.peek();
        if (currentLayer != null) {
            currentLayer.onExit(ExitContextImpl.INSTANCE);
        }
        for (Layer layer : layerStack) {
            layer.onRemove(RemoveContextImpl.INSTANCE);
        }
        layerStack.clear();
        player.clearTitle();
        player.sendActionBar(Component.empty());
        for (EditorParticle particle : particles) {
            particle.hideGizmo();
        }
        particles.clear();
        valid = false;
    }

    public double getRange() {
        return eas.getConfiguration().editor.button.range;
    }

    public double getLookThreshold() {
        return eas.getConfiguration().editor.button.threshold;
    }

    @Override
    public void addParticle(@NotNull Particle particle) {
        if (!valid) {
            return;
        }
        EditorParticle editorParticle = (EditorParticle) particle;
        if (particles.add(editorParticle)) {
            editorParticle.showGizmo();
        }
    }

    @Override
    public void removeParticle(@NotNull Particle particle) {
        if (!valid) {
            return;
        }
        EditorParticle editorParticle = (EditorParticle) particle;
        if (particles.remove(editorParticle)) {
            editorParticle.hideGizmo();
        }
    }

    public boolean isValid() {
        return valid;
    }

    public void setPlayer(Player player) {
        this.player = new EasPlayer(eas, player);
    }

    @Override
    public @NotNull Player player() {
        return player.get();
    }

    @Override
    public @NotNull PropertyContainer properties(@NotNull Element element) {
        return new TrackedPropertyContainer(eas, element, player);
    }

    @Override
    public @NotNull EyeRay eyeRay() {
        double length = getRange();
        Location eyeLocation = player.get().eyeLocation();
        double threshold = getLookThreshold();
        return new EyeRayImpl(eyeLocation.world(), eyeLocation, length, threshold);
    }

    public @NotNull EyeRay eyeRay(Vector2dc cursor) {
        double length = getRange();
        Location eyeLocation = player.get().eyeLocation();
        Matrix4dc eyeMatrix = eyeMatrix(eyeLocation);
        Vector3d origin = eyeMatrix.transformPosition(cursor.x(), cursor.y(), 0, new Vector3d());
        Location location = eyeLocation.withPosition(origin);
        double threshold = getLookThreshold();
        return new EyeRayImpl(location.world(), location, length, threshold);
    }

    private Matrix4dc eyeMatrix(Location eyeLocation) {
        return Util.toMatrix4d(eyeLocation);
    }

    @Override
    public @NotNull ParticleProvider particleProvider() {
        return particleProvider;
    }

    @Override
    public @NotNull NodeProvider nodeProvider() {
        return nodeProvider;
    }

    @Override
    public @NotNull LayerProvider layerProvider() {
        return layerProvider;
    }

    @Override
    public @NotNull InputProvider inputProvider() {
        return inputProvider;
    }

    @Override
    public @NotNull SessionSnapper snapper() {
        return snapper;
    }

    @Override
    public @NotNull Node createElementNode(SelectableElement element, Button button) {
        return new ButtonNode(button, new ElementButtonHandler(eas, this, element));
    }

    public boolean isToolRequired() {
        return toolRequired;
    }

    public void setToolRequired(boolean toolRequired) {
        this.toolRequired = toolRequired;
    }
}
