# EasyArmorStands (Polymer)

A server-side Fabric port of [EasyArmorStands by 56738](https://github.com/56738/EasyArmorStands),
with Polymer virtual entities for the editor gizmos and support for Fabric's built-in Permission API.
Players can edit armor stands and display entities with an unmodified client; no client mod or resource pack is required.

This fork builds a single Fabric mod. It includes the local Polymer port's package relocation to
`re.koda.easyarmorstands`, permission bridge, spawn-menu setting, and game tests.
Paper, NeoForge, and Bukkit plugin integrations are not included.

## Installation

Requires Minecraft **26.3**, Java **25**, Fabric Loader **0.19.2 or newer**, and Fabric API **0.164.0+26.3 or newer** for Minecraft 26.3.

Build with `./gradlew build`, then place `build/libs/EasyArmorStands-Polymer-0.1.0.jar` and Fabric API
in the server's `mods` folder. Polymer, Adventure, Cloud, Configurate, and the legacy Fabric Permissions API
are bundled in the mod jar.

Remove the original EasyArmorStands mod before installing this fork. Its mod ID is
`easyarmorstands-polymer`, and it cannot be loaded alongside `easyarmorstands`.

## Usage

Use `/eas give` to get the editor tool, then hold it and right-click an entity to select it.

- Right-click a line to move the selected part, or a circle to rotate it.
- Move or look around to adjust it, then right-click to confirm; left-click cancels.
- Press **F** to open the selected entity's menu.
- Hold **Shift** while selecting entities to build a group selection.
- Use `/eas undo` and `/eas redo` to undo or redo edits.

Armor stands have six body-part bones plus a yellow position point. Select a bone before editing it.
Display entities support movement, rotation, scaling, and a bounding-box editor through `/eas box`.

Useful commands:

- `/eas help`: show the available commands and their usage.
- `/eas spawn`: open the entity spawn menu.
- `/eas name`: edit an entity's custom name.
- `/eas text`: edit a text display's text.
- `/eas block`: choose a block display's block and block state.
- `/eas clone`: duplicate the selection.
- `/eas align`: align the selection with its block.
- `/eas snap angle` and `/eas snap move`: configure snapping.
- `/eas reset <property>`: reset a property.

Names and text support [MiniMessage formatting](https://docs.papermc.io/adventure/minimessage/format/).

## Permissions

Both command permissions and editor actions consult Fabric's built-in Permission API
(`fabric-permission-api-v1`, included in Fabric API). Dotted EasyArmorStands permissions map to identifiers:

- `easyarmorstands.give` becomes `easyarmorstands:give`.
- `easyarmorstands.spawn` becomes `easyarmorstands:spawn`.
- `easyarmorstands.property.glow` becomes `easyarmorstands:property/glow`.

A permission provider can grant or deny these identifiers. When it supplies no answer, checks fall back
to the legacy `fabric-permissions-api-v0` providers and then the server's operator permission level.
Fabric API itself does not provide a command for managing these grants.

The existing `easyarmorstands.survival` and `easyarmorstands.creative` permission packs remain available.
See the [upstream permission reference](https://github.com/56738/EasyArmorStands/wiki/Permissions)
for the individual permission names.

## Configuration

Configuration is stored in `config/easyarmorstands/config.yml`. The local port adds:

```yaml
editor:
  spawn-menu: true
```

Set it to `false` to disable the spawn menu, including `/eas spawn` and the editor shortcut.
Reload configuration with `/eas reload`.

## Building and testing

Use the included Gradle wrapper with Java 25:

```sh
./gradlew build
./gradlew runGameTest
```

The game-test task launches a dedicated test server and accepts the Minecraft EULA for that test run.
It checks tool delivery, armor-stand selection/editing/undo/redo/cloning, Polymer gizmo creation and session
cleanup, disabling the spawn menu, and permission grants and denials through Fabric's built-in API.

The mod and corresponding source jar are generated in `build/libs/`.
GitHub Actions builds both artifacts and runs the game tests; it does not publish releases or upload to mod repositories.

## License and attribution

Licensed under **GPL-3.0-or-later**. Based on EasyArmorStands commit
`c589944e23b49170fb7380742fe755c392f1e0c7` and the gizmo library by 56738.
See [LICENSE](LICENSE) and [NOTICE.md](NOTICE.md) for attribution and bundled-library licenses.
