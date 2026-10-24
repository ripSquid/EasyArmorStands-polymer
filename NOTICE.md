# EasyArmorStands (Polymer)

This mod is a modified version of **EasyArmorStands** by 56738
(https://github.com/56738/EasyArmorStands, commit `c589944e23b49170fb7380742fe755c392f1e0c7`),
and contains code from the **gizmo** library by 56738 (https://github.com/56738/gizmo).
Both are licensed under the GNU General Public License v3.0 or later.

Modifications (2026-10-22, ripsquid):
- Packaged as a single Fabric mod (`easyarmorstands-polymer`); Paper, NeoForge and Bukkit plugin integrations removed.
- Java packages relocated from `me.m56738.easyarmorstands` / `me.m56738.gizmo` to `re.koda.easyarmorstands`.
- Editor gizmos are rendered with Polymer virtual entities (`re.koda.easyarmorstands.polymer.gizmo`)
  instead of the gizmo-fabric backend.
- Permission checks also consult the Fabric Permission API (`fabric-permission-api-v1`);
  `easyarmorstands.foo.bar` is checked as `easyarmorstands:foo/bar`.
- New config option `editor.spawn-menu` to disable the spawn menu.

This program is free software: you can redistribute it and/or modify it under the terms of the
GNU General Public License as published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version. See `LICENSE`.

The complete corresponding source code is distributed alongside the mod jar
(`EasyArmorStands-Polymer-<version>-sources.jar`).

Bundled libraries (jar-in-jar) keep their own licenses: Polymer (LGPL-3.0), Adventure (MIT),
Cloud (MIT), Configurate (Apache-2.0), fabric-permissions-api (MIT).
