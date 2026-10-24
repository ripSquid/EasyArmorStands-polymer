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
package re.koda.easyarmorstands;

import io.leangen.geantyref.TypeToken;
import re.koda.easyarmorstands.api.EasyArmorStands;
import re.koda.easyarmorstands.api.editor.context.ClickContext;
import re.koda.easyarmorstands.api.editor.layer.ElementSelectionLayer;
import re.koda.easyarmorstands.api.element.Element;
import re.koda.easyarmorstands.api.element.ElementDiscoverySource;
import re.koda.easyarmorstands.api.element.ElementSpawnRequest;
import re.koda.easyarmorstands.api.element.ElementType;
import re.koda.easyarmorstands.api.element.ElementTypeRegistry;
import re.koda.easyarmorstands.api.element.EntityElementProvider;
import re.koda.easyarmorstands.api.element.ReferenceProvider;
import re.koda.easyarmorstands.api.property.Property;
import re.koda.easyarmorstands.api.property.PropertyContainer;
import re.koda.easyarmorstands.clipboard.Clipboard;
import re.koda.easyarmorstands.clipboard.ClipboardManager;
import re.koda.easyarmorstands.command.ClipboardCommands;
import re.koda.easyarmorstands.command.DisplayCommands;
import re.koda.easyarmorstands.command.HistoryCommands;
import re.koda.easyarmorstands.command.PropertyCommands;
import re.koda.easyarmorstands.command.SessionCommands;
import re.koda.easyarmorstands.command.annotation.PropertyPermission;
import re.koda.easyarmorstands.command.parser.ArgumentParserProvider;
import re.koda.easyarmorstands.command.parser.BlockDataArgumentParser;
import re.koda.easyarmorstands.command.parser.LayerValueArgumentParser;
import re.koda.easyarmorstands.command.processor.ClipboardInjector;
import re.koda.easyarmorstands.command.processor.ClipboardProcessor;
import re.koda.easyarmorstands.command.processor.ElementInjector;
import re.koda.easyarmorstands.command.processor.ElementProcessor;
import re.koda.easyarmorstands.command.processor.ElementSelectionInjector;
import re.koda.easyarmorstands.command.processor.ElementSelectionProcessor;
import re.koda.easyarmorstands.command.processor.GroupProcessor;
import re.koda.easyarmorstands.command.processor.PropertyPermissionBuilderModifier;
import re.koda.easyarmorstands.command.processor.SessionInjector;
import re.koda.easyarmorstands.command.processor.SessionProcessor;
import re.koda.easyarmorstands.command.processor.ValueLayerInjector;
import re.koda.easyarmorstands.command.requirement.CommandRequirementBuilderModifier;
import re.koda.easyarmorstands.command.requirement.CommandRequirementPostProcessor;
import re.koda.easyarmorstands.command.requirement.ElementRequirement;
import re.koda.easyarmorstands.command.requirement.ElementSelectionRequirement;
import re.koda.easyarmorstands.command.requirement.RequireElement;
import re.koda.easyarmorstands.command.requirement.RequireElementSelection;
import re.koda.easyarmorstands.command.requirement.RequireSession;
import re.koda.easyarmorstands.command.requirement.SessionRequirement;
import re.koda.easyarmorstands.command.sender.EasCommandSender;
import re.koda.easyarmorstands.command.sender.EasPlayer;
import re.koda.easyarmorstands.command.util.ElementSelection;
import re.koda.easyarmorstands.config.EasConfig;
import re.koda.easyarmorstands.config.serializer.EasSerializers;
import re.koda.easyarmorstands.config.version.Transformations;
import re.koda.easyarmorstands.config.version.game.GameVersionTransformation;
import re.koda.easyarmorstands.dialog.DialogBuilder;
import re.koda.easyarmorstands.editor.layer.ElementSelectionLayerImpl;
import re.koda.easyarmorstands.editor.layer.EntityElementDiscoverySource;
import re.koda.easyarmorstands.editor.layer.ValueLayer;
import re.koda.easyarmorstands.element.ArmorStandElementType;
import re.koda.easyarmorstands.element.DefaultEntityElementProvider;
import re.koda.easyarmorstands.element.DefaultEntityElementType;
import re.koda.easyarmorstands.element.DisplayElementType;
import re.koda.easyarmorstands.element.ElementMenuPopulator;
import re.koda.easyarmorstands.element.ElementSpawnRequestImpl;
import re.koda.easyarmorstands.element.ElementTypeRegistryImpl;
import re.koda.easyarmorstands.element.EntityElementKeys;
import re.koda.easyarmorstands.element.EntityElementPopulator;
import re.koda.easyarmorstands.element.EntityElementProviderRegistryImpl;
import re.koda.easyarmorstands.element.InteractionElementType;
import re.koda.easyarmorstands.element.MannequinElementType;
import re.koda.easyarmorstands.element.ReferenceProviderImpl;
import re.koda.easyarmorstands.element.SimpleEntityElementProvider;
import re.koda.easyarmorstands.element.TextDisplayElementType;
import re.koda.easyarmorstands.event.EventDispatcher;
import re.koda.easyarmorstands.history.History;
import re.koda.easyarmorstands.history.HistoryManager;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.loader.HeaderMode;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;
import org.spongepowered.configurate.util.MapFactories;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;
import re.koda.easyarmorstands.menu.Menu;
import re.koda.easyarmorstands.menu.MenuButtonCollector;
import re.koda.easyarmorstands.menu.MenuListener;
import re.koda.easyarmorstands.menu.layout.MenuLayout;
import re.koda.easyarmorstands.menu.layout.MenuLayoutRule;
import re.koda.easyarmorstands.message.Message;
import re.koda.easyarmorstands.message.MessageManager;
import re.koda.easyarmorstands.message.TranslationManager;
import re.koda.easyarmorstands.particle.ParticleProviderFactory;
import re.koda.easyarmorstands.platform.Platform;
import re.koda.easyarmorstands.platform.block.Block;
import re.koda.easyarmorstands.platform.block.BlockData;
import re.koda.easyarmorstands.platform.entity.BlockDisplay;
import re.koda.easyarmorstands.platform.entity.Entity;
import re.koda.easyarmorstands.platform.entity.EntityType;
import re.koda.easyarmorstands.platform.entity.ItemDisplay;
import re.koda.easyarmorstands.platform.entity.Player;
import re.koda.easyarmorstands.platform.entity.TextDisplay;
import re.koda.easyarmorstands.platform.event.EventType;
import re.koda.easyarmorstands.platform.inventory.EquipmentSlot;
import re.koda.easyarmorstands.platform.inventory.ItemStack;
import re.koda.easyarmorstands.property.TrackedPropertyContainer;
import re.koda.easyarmorstands.property.type.DefaultPropertyTypes;
import re.koda.easyarmorstands.property.type.PropertyTypeRegistryImpl;
import re.koda.easyarmorstands.registry.EntityTypeKeys;
import re.koda.easyarmorstands.registry.ItemTypeKeys;
import re.koda.easyarmorstands.session.SessionImpl;
import re.koda.easyarmorstands.session.SessionListener;
import re.koda.easyarmorstands.session.SessionManagerImpl;
import re.koda.easyarmorstands.session.SessionToolProvider;
import re.koda.easyarmorstands.session.context.ClickContextImpl;
import net.kyori.adventure.key.InvalidKeyException;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.annotations.AnnotationParser;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.exception.InvalidCommandSenderException;
import org.incendo.cloud.help.result.CommandEntry;
import org.incendo.cloud.injection.ParameterInjector;
import org.incendo.cloud.minecraft.extras.MinecraftExceptionHandler;
import org.incendo.cloud.minecraft.extras.MinecraftHelp;
import org.incendo.cloud.minecraft.extras.RichDescription;
import org.incendo.cloud.minecraft.extras.parser.TextColorParser;
import org.incendo.cloud.minecraft.extras.suggestion.ComponentTooltipSuggestion;
import org.incendo.cloud.services.type.ConsumerService;
import org.incendo.cloud.suggestion.Suggestion;
import org.incendo.cloud.suggestion.SuggestionProvider;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.Callable;

@NullMarked
public abstract class EasyArmorStandsCommon implements EasyArmorStands {
    private static final MiniMessage miniMessage = MiniMessage.builder()
            .emitVirtuals(true)
            .build();

    private final TypeSerializerCollection serializers;
    private final TranslationManager translationManager;
    private final Platform platform;
    private final CommandManager<EasCommandSender> commandManager;
    private EasConfig config = new EasConfig();
    private @Nullable MessageManager messageManager;
    private @Nullable ReferenceProvider referenceProvider;
    private @Nullable ElementTypeRegistryImpl elementTypeRegistry;
    private @Nullable PropertyTypeRegistryImpl propertyTypeRegistry;
    private @Nullable EntityElementProviderRegistryImpl entityElementProviderRegistry;
    private @Nullable SessionManagerImpl sessionManager;
    private @Nullable HistoryManager historyManager;
    private @Nullable ClipboardManager clipboardManager;

    public EasyArmorStandsCommon(
            TranslationManager translationManager,
            Platform platform, CommandManager<EasCommandSender> commandManager) {
        this.platform = platform;
        this.translationManager = translationManager;
        this.commandManager = commandManager;
        this.serializers = EasSerializers.serializers(platform);

        MenuListener menuListener = new MenuListener(this);
        platform.getEventBus().subscribe(EventType.MENU_CLICK, menuListener);
        platform.getEventBus().subscribe(EventType.MENU_DRAG, menuListener);

        SessionListener sessionListener = new SessionListener(this);
        platform.getEventBus().subscribe(EventType.MENU_CLOSE, sessionListener);
        platform.getEventBus().subscribe(EventType.PLAYER_ADD, sessionListener);
        platform.getEventBus().subscribe(EventType.PLAYER_DESTROY_ENTITY, sessionListener);
        platform.getEventBus().subscribe(EventType.PLAYER_DROP_ITEM, sessionListener);
        platform.getEventBus().subscribe(EventType.PLAYER_LEFT_CLICK_BLOCK, sessionListener);
        platform.getEventBus().subscribe(EventType.PLAYER_LEFT_CLICK, sessionListener);
        platform.getEventBus().subscribe(EventType.PLAYER_LEFT_CLICK_ENTITY, sessionListener);
        platform.getEventBus().subscribe(EventType.PLAYER_PICK_UP_ITEM, sessionListener);
        platform.getEventBus().subscribe(EventType.PLAYER_PLACED_ENTITY, sessionListener);
        platform.getEventBus().subscribe(EventType.PLAYER_REMOVE, sessionListener);
        platform.getEventBus().subscribe(EventType.PLAYER_REPLACE, sessionListener);
        platform.getEventBus().subscribe(EventType.PLAYER_RIGHT_CLICK_BLOCK, sessionListener);
        platform.getEventBus().subscribe(EventType.PLAYER_RIGHT_CLICK, sessionListener);
        platform.getEventBus().subscribe(EventType.PLAYER_RIGHT_CLICK_ENTITY, sessionListener);
        platform.getEventBus().subscribe(EventType.PLAYER_SWAP_HANDS, sessionListener);
        platform.getEventBus().subscribe(EventType.PLAYER_SWITCH_SELECTED_SLOT, sessionListener);
    }

    public static void registerCommands(CommandManager<EasCommandSender> commandManager, ArgumentParserProvider parserProvider, ClassLoader classLoader, EasyArmorStandsHolder holder) {
        commandManager.parserRegistry().registerSuggestionProvider("help_queries",
                SuggestionProvider.blocking((ctx, _) -> commandManager.createHelpHandler()
                        .queryRootIndex(ctx.sender())
                        .entries()
                        .stream()
                        .map(EasyArmorStandsCommon::createSuggestion)
                        .toList()));
        commandManager.parserRegistry().registerNamedParserSupplier("node_value",
                _ -> new LayerValueArgumentParser<>());
        commandManager.parserRegistry().registerParserSupplier(TypeToken.get(TextColor.class),
                _ -> new TextColorParser<>());
        commandManager.parserRegistry().registerParserSupplier(TypeToken.get(BlockData.class),
                _ -> new BlockDataArgumentParser<>());

        commandManager.parserRegistry().registerParser(parserProvider.locationParser());
        commandManager.parserRegistry().registerParser(parserProvider.singleEntitySelector());
        commandManager.parserRegistry().registerParser(parserProvider.multipleEntitySelector());
        commandManager.parserRegistry().registerParser(parserProvider.multiplePlayerSelector());

        AnnotationParser<EasCommandSender> annotationParser = new AnnotationParser<>(commandManager, EasCommandSender.class);
        annotationParser.descriptionMapper(RichDescription::translatable);

        annotationParser.registerBuilderModifier(RequireSession.class, new CommandRequirementBuilderModifier<>(_ -> new SessionRequirement()));
        annotationParser.registerBuilderModifier(RequireElement.class, new CommandRequirementBuilderModifier<>(_ -> new ElementRequirement()));
        annotationParser.registerBuilderModifier(RequireElementSelection.class, new CommandRequirementBuilderModifier<>(_ -> new ElementSelectionRequirement()));

        annotationParser.registerBuilderModifier(PropertyPermission.class, new PropertyPermissionBuilderModifier(holder));
        annotationParser.parse(new SessionCommands());
        annotationParser.parse(new HistoryCommands());
        annotationParser.parse(new ClipboardCommands());
        annotationParser.parse(new DisplayCommands());
        try {
            annotationParser.parseContainers(classLoader);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        PropertyCommands.register(commandManager, parserProvider);

        commandManager.registerCommandPreProcessor(context -> {
            if (!holder.isInitialized()) {
                context.commandContext().sender().sendMessage(Component.text("EasyArmorStands failed to load", NamedTextColor.RED));
                ConsumerService.interrupt();
            }
        });
    }

    private static Suggestion createSuggestion(CommandEntry<EasCommandSender> entry) {
        Description description = entry.command().commandDescription().description();
        String syntax = entry.syntax();
        Component tooltip;
        if (description instanceof RichDescription richDescription) {
            tooltip = richDescription.contents();
        } else if (!description.isEmpty()) {
            tooltip = Component.text(description.textDescription());
        } else {
            return Suggestion.suggestion(syntax);
        }
        return ComponentTooltipSuggestion.suggestion(syntax, tooltip);
    }

    public abstract String getVersion();

    public abstract ParticleProviderFactory particleProviderFactory();

    public abstract SessionToolProvider sessionToolProvider();

    public abstract Path getConfigFolder();

    public abstract ComponentLogger getLogger();

    public abstract ClassLoader getClassLoader();

    public abstract EventDispatcher eventDispatcher();

    public ClipboardManager clipboardManager() {
        if (clipboardManager == null) {
            throw new IllegalStateException();
        }
        return clipboardManager;
    }

    public static MiniMessage miniMessage() {
        return miniMessage;
    }

    @Override
    public Platform platform() {
        return platform;
    }

    public void onLoad() {
        EntityTypeKeys.validate(platform);
        ItemTypeKeys.validate(platform);

        referenceProvider = new ReferenceProviderImpl(platform);
        elementTypeRegistry = new ElementTypeRegistryImpl();
        propertyTypeRegistry = new PropertyTypeRegistryImpl();
        entityElementProviderRegistry = new EntityElementProviderRegistryImpl(this);

        new DefaultPropertyTypes(propertyTypeRegistry);

        ArmorStandElementType armorStandElementType = new ArmorStandElementType(this);
        elementTypeRegistry.register(armorStandElementType);
        entityElementProviderRegistry.register(new SimpleEntityElementProvider<>(this, armorStandElementType));

        MannequinElementType mannequinElementType = new MannequinElementType(this);
        elementTypeRegistry.register(mannequinElementType);
        entityElementProviderRegistry.register(new SimpleEntityElementProvider<>(this, mannequinElementType));

        DisplayElementType<ItemDisplay> itemDisplayType = new DisplayElementType<>(this, platform.getEntityType(EntityTypeKeys.ITEM_DISPLAY), ItemDisplay.class);
        elementTypeRegistry.register(itemDisplayType);
        entityElementProviderRegistry.register(new SimpleEntityElementProvider<>(this, itemDisplayType));

        DisplayElementType<BlockDisplay> blockDisplayType = new DisplayElementType<>(this, platform.getEntityType(EntityTypeKeys.BLOCK_DISPLAY), BlockDisplay.class);
        elementTypeRegistry.register(blockDisplayType);
        entityElementProviderRegistry.register(new SimpleEntityElementProvider<>(this, blockDisplayType));

        DisplayElementType<TextDisplay> textDisplayType = new TextDisplayElementType(this);
        elementTypeRegistry.register(textDisplayType);
        entityElementProviderRegistry.register(new SimpleEntityElementProvider<>(this, textDisplayType));

        InteractionElementType interactionType = new InteractionElementType(this);
        elementTypeRegistry.register(interactionType);
        entityElementProviderRegistry.register(new SimpleEntityElementProvider<>(this, interactionType));

        for (EntityType entityType : platform.getEntityTypes()) {
            if (elementTypeRegistry.getOrNull(entityType.key()) != null) {
                continue;
            }
            DefaultEntityElementType<Entity> type = new DefaultEntityElementType<>(this, entityType, Entity.class);
            elementTypeRegistry.register(type);
            entityElementProviderRegistry.register(new DefaultEntityElementProvider<>(this, type));
        }

        loadConfig();
        messageManager = new MessageManager();
        messageManager.load(config);

        sessionManager = new SessionManagerImpl(eventDispatcher(), sessionToolProvider(), this);
        historyManager = new HistoryManager(this);
        clipboardManager = new ClipboardManager(this);

        MinecraftExceptionHandler.<EasCommandSender>createNative()
                .defaultArgumentParsingHandler()
                .defaultInvalidSyntaxHandler()
                .defaultNoPermissionHandler()
                .defaultCommandExecutionHandler()
                .defaultInvalidSenderHandler()
                .handler(InvalidCommandSenderException.class,
                        (_, _) -> Message.error("easyarmorstands.error.not-a-player"))
                .registerTo(commandManager);

        MinecraftHelp<EasCommandSender> help = MinecraftHelp.createNative("/eas help", commandManager);

        commandManager.parameterInjectorRegistry()
                .registerInjector(EasyArmorStandsCommon.class, ParameterInjector.constantInjector(this))
                .registerInjector(Platform.class, ParameterInjector.constantInjector(platform))
                .registerInjector(ValueLayer.class, new ValueLayerInjector())
                .registerInjector(SessionImpl.class, new SessionInjector())
                .registerInjector(Clipboard.class, new ClipboardInjector())
                .registerInjector(Element.class, new ElementInjector())
                .registerInjector(ElementSelection.class, new ElementSelectionInjector())
                .registerInjector(ElementTypeRegistry.class, ParameterInjector.constantInjector(elementTypeRegistry))
                .registerInjector(new TypeToken<>() {
                }, ParameterInjector.constantInjector(help));

        commandManager.registerCommandPreProcessor(new ElementSelectionProcessor(this));
        commandManager.registerCommandPreProcessor(new GroupProcessor());
        commandManager.registerCommandPreProcessor(new ElementProcessor());
        commandManager.registerCommandPreProcessor(new SessionProcessor());
        commandManager.registerCommandPreProcessor(new ClipboardProcessor());

        commandManager.registerCommandPostProcessor(new CommandRequirementPostProcessor());
    }

    private Callable<BufferedReader> getDefaultConfigSource(String name) {
        return () -> {
            InputStream resource = getClassLoader().getResourceAsStream(name);
            if (resource == null) {
                throw new FileNotFoundException(name);
            }
            return new BufferedReader(new InputStreamReader(resource, StandardCharsets.UTF_8));
        };
    }

    public void onDisable() {
        sessionManager.stopAllSessions();
    }

    public void reload() {
        loadConfig();
        messageManager.load(config);
        translationManager.load(getConfigFolder(), getLogger());
        for (SessionImpl session : sessionManager.getAllSessions()) {
            if (session.getLayer() instanceof ElementSelectionLayerImpl layer) {
                layer.refresh();
            }
        }
    }

    private void loadConfig() {
        loadConfig("config.yml", new ConfigProcessor() {
            @Override
            public void process(CommentedConfigurationNode node) throws ConfigurateException {
                GameVersionTransformation.config(platform).apply(node);
                Transformations.config().apply(node);
            }

            @Override
            public void apply(CommentedConfigurationNode node) throws ConfigurateException {
                config = node.get(EasConfig.class);
            }
        });
    }

    private void loadConfig(String name, ConfigProcessor configProcessor) {
        try {
            loadMergedConfig(name, configProcessor, true);
            return;
        } catch (ConfigurateException e) {
            getLogger().error("Failed to load {}: {}", name, e.getMessage());
        } catch (Exception e) {
            getLogger().error("Failed to load {}", name, e);
        }

        try {
            loadDefaultConfig(name, configProcessor);
        } catch (ConfigurateException e) {
            getLogger().error("Failed to load default {}", name, e);
        }
    }

    private CommentedConfigurationNode loadMergedConfig(String name, ConfigProcessor configProcessor, boolean create) throws ConfigurateException {
        CommentedConfigurationNode defaultNode = loadDefaultConfig(name, new ConfigProcessor() {
            @Override
            public void process(CommentedConfigurationNode node) throws ConfigurateException {
                configProcessor.process(node);
            }

            @Override
            public void apply(CommentedConfigurationNode node) {
            }
        });

        YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .nodeStyle(NodeStyle.BLOCK)
                .indent(2)
                .headerMode(HeaderMode.PRESET)
                .defaultOptions(o -> o
                        .serializers(b -> b.registerAll(serializers))
                        .mapFactory(MapFactories.sortedNatural())
                        .header(defaultNode.options().header())
                        .shouldCopyDefaults(false))
                .path(getConfigFolder().resolve(name))
                .build();

        CommentedConfigurationNode node = loader.load();
        boolean exists = !node.empty();

        if (exists) {
            configProcessor.process(node);
        }

        node.mergeFrom(defaultNode);

        configProcessor.apply(node);

        if (exists || create) {
            loader.save(node);
        }

        return node;
    }

    private CommentedConfigurationNode loadDefaultConfig(String name, ConfigProcessor configProcessor) throws ConfigurateException {
        YamlConfigurationLoader defaultLoader = YamlConfigurationLoader.builder()
                .defaultOptions(o -> o.serializers(b -> b.registerAll(serializers)))
                .source(getDefaultConfigSource(name))
                .build();

        CommentedConfigurationNode node = defaultLoader.load();

        if (!node.empty()) {
            configProcessor.process(node);
        }
        configProcessor.apply(node);

        return node;
    }

    public History getHistory(Player player) {
        return historyManager.getHistory(player);
    }

    public Clipboard getClipboard(Player player) {
        return clipboardManager.getClipboard(player);
    }

    public HistoryManager historyManager() {
        return historyManager;
    }

    public CommandManager<EasCommandSender> getCommandManager() {
        return commandManager;
    }

    public EasConfig getConfiguration() {
        return config;
    }

    public void openSpawnMenu(Player player) {
        if (!config.editor.spawnMenu) {
            return;
        }
        MenuButtonCollector collector = new MenuButtonCollector();
        new EntityElementPopulator(platform()).populateSpawnMenu(this, collector, player);
        eventDispatcher().dispatchSpawnMenuOpen(player, collector);
        Menu menu = MenuLayout.createSimple(this).createMenu(Component.translatable("easyarmorstands.menu.spawn.title"), player.locale(), collector.getButtons());
        player.openInventory(menu.getInventory());
    }

    public abstract Menu createColorPicker(Player player, Property<ItemStack> property);

    public abstract boolean isColorPickerSupported(ItemStack item);

    public void openElementMenu(Player player, Element element) {
        MenuButtonCollector collector = new MenuButtonCollector();
        PropertyContainer properties = new TrackedPropertyContainer(this, element, new EasPlayer(this, player));
        new ElementMenuPopulator(this).populateMenu(element, collector, player, properties);
        eventDispatcher().dispatchElementMenuOpen(player, element, collector, properties);
        MenuLayout layout;
        if (collector.getButtons().stream()
                .anyMatch(MenuLayoutRule.equipmentSlot(EquipmentSlot.HEAD)::matches)) {
            layout = MenuLayout.createEquipment(this);
        } else {
            layout = MenuLayout.createDefault(this);
        }
        Menu menu = layout.createMenu(element.getType().getDisplayName(), player.locale(), collector.getButtons());
        player.openInventory(menu.getInventory());
    }

    @Override
    public EntityElementProviderRegistryImpl entityElementProviderRegistry() {
        return entityElementProviderRegistry;
    }

    @Override
    public SessionManagerImpl sessionManager() {
        return Objects.requireNonNull(sessionManager);
    }

    @Override
    public ElementSpawnRequest elementSpawnRequest(ElementType type) {
        return new ElementSpawnRequestImpl(this, type);
    }

    @Override
    public ElementTypeRegistry elementTypeRegistry() {
        return elementTypeRegistry;
    }

    @Override
    public PropertyTypeRegistryImpl propertyTypeRegistry() {
        return Objects.requireNonNull(propertyTypeRegistry);
    }

    @Override
    public @Nullable Element getElement(Entity entity) {
        if (!entity.isValid()) {
            return null;
        }
        if (particleProviderFactory().isParticle(entity)) {
            return null;
        }
        return entityElementProviderRegistry.getElement(entity);
    }

    @Override
    public void refreshEntity(Entity entity) {
        for (SessionImpl session : sessionManager.getAllSessions()) {
            ElementSelectionLayer layer = session.findLayer(ElementSelectionLayer.class);
            if (layer != null) {
                for (ElementDiscoverySource source : layer.getSources()) {
                    if (source instanceof EntityElementDiscoverySource entitySource) {
                        layer.refreshEntry(entitySource.getEntry(entity));
                    }
                }
            }
        }
    }

    public ReferenceProvider referenceProvider() {
        return referenceProvider;
    }

    public DialogBuilder createDialogBuilder(Player player) {
        return new DialogBuilder(platform.getDialogFactory(), player.locale());
    }

    @SuppressWarnings("PatternValidation")
    public @Nullable Key getEntityElementProvider(Entity entity) {
        String providerName = entity.getCustomDataString(EntityElementKeys.ELEMENT_TYPE);
        if (providerName == null) {
            return null;
        }
        try {
            return Key.key(providerName);
        } catch (InvalidKeyException e) {
            return null;
        }
    }

    @Override
    public void setEntityElementProvider(Entity entity, @Nullable EntityElementProvider provider) {
        setEntityElementType(entity, provider != null ? provider.key() : null);
    }

    public void setEntityElementType(Entity entity, @Nullable Key key) {
        if (key != null) {
            entity.setCustomDataString(EntityElementKeys.ELEMENT_TYPE, key.asString());
        } else {
            entity.removeCustomData(EntityElementKeys.ELEMENT_TYPE);
        }

    }

    public abstract ItemStack createEntitySpawnEgg(Entity entity);

    public abstract boolean isIgnored(Entity entity);

    public void update() {
        if (sessionManager != null) {
            sessionManager.update();
        }
    }

    public boolean handleClick(Player player, ClickContext.Type type, @Nullable Entity entity, @Nullable Block block) {
        if (sessionManager == null) {
            return false;
        }
        SessionImpl session = sessionManager.getSession(player);
        if (session != null) {
            return session.handleClick(new ClickContextImpl(session.eyeRay(), type, entity, block));
        }
        if (type == ClickContext.Type.RIGHT_CLICK && sessionManager.isHoldingTool(player)) {
            session = sessionManager.startSession(player);
            session.setToolRequired(true);
            return true;
        }
        return false;
    }

    public void handleSwitchSlot(Player player) {
        if (sessionManager == null) {
            return;
        }
        sessionManager.updateHeldItem(player);
    }

    private interface ConfigProcessor {
        void process(CommentedConfigurationNode node) throws ConfigurateException;

        void apply(CommentedConfigurationNode node) throws ConfigurateException;
    }
}
