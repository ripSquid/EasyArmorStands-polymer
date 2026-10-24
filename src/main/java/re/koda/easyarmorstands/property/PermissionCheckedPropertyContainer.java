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
package re.koda.easyarmorstands.property;

import re.koda.easyarmorstands.EasyArmorStandsCommon;
import re.koda.easyarmorstands.api.element.Element;
import re.koda.easyarmorstands.api.property.Property;
import re.koda.easyarmorstands.api.property.PropertyWrapperContainer;
import re.koda.easyarmorstands.command.sender.EasPlayer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A property container which performs permission checks before modifying the value.
 */
public class PermissionCheckedPropertyContainer extends PropertyWrapperContainer {
    private final EasyArmorStandsCommon eas;
    private final Element element;
    private final EasPlayer player;

    public PermissionCheckedPropertyContainer(EasyArmorStandsCommon eas, Element element, EasPlayer player) {
        super(element.getProperties());
        this.eas = eas;
        this.element = element;
        this.player = player;
    }

    @Override
    protected @NotNull <T> Property<T> wrap(@NotNull Property<T> property) {
        return new PermissionCheckedPropertyWrapper<>(property, element, player);
    }

    @Override
    public void commit(@Nullable Component description) {
        super.commit(description);
        eas.eventDispatcher().dispatchCommitElement(player.get(), element);
    }
}
