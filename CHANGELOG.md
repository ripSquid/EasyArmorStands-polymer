# 0.1.0 (Polymer)

- Port the local Fabric-only implementation into this fork, retaining the upstream Git history.
- Render editor gizmos with Polymer virtual entities visible to the editing player.
- Bridge EasyArmorStands command and editor permissions to Fabric's built-in Permission API.
- Add `editor.spawn-menu` to disable the spawn menu and its editor shortcut.
- Package one Fabric mod and its corresponding source jar; remove Paper, NeoForge, and Bukkit integrations.
- Add dedicated-server game tests and update CI for the Fabric-only build.

# API changes

* Introduced an abstraction layer to enable adding support for other platforms.
* Plugins should use the `easyarmorstands-paper-api` dependency instead of `easyarmorstands-api`.
* Most objects can be converted from and to Paper objects using e.g. `PaperPlayer.fromNative(player)` and
  `PaperPlayer.toNative(player)`.
    * Other objects can be converted using `PaperAdapter`.
* The `EasyArmorStands` object can be obtained using `EasyArmorStandsPaper.getInstance()`.
* Moved the API to a new Maven repository: `https://repo.56738.me/repository/maven-public/`
