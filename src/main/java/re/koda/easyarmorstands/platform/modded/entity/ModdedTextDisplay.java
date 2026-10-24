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
package re.koda.easyarmorstands.platform.modded.entity;

import re.koda.easyarmorstands.platform.color.ARGBColor;
import re.koda.easyarmorstands.platform.entity.TextDisplay;
import re.koda.easyarmorstands.platform.modded.ModdedPlatform;
import net.kyori.adventure.text.Component;
import net.minecraft.world.entity.Display;
import org.jspecify.annotations.Nullable;

public interface ModdedTextDisplay extends TextDisplay, ModdedDisplay {
    @Override
    Display.TextDisplay getNative();

    static ModdedTextDisplay fromNative(ModdedPlatform platform, Display.TextDisplay entity) {
        return new ModdedTextDisplayImpl(platform, entity);
    }

    static Display.TextDisplay toNative(TextDisplay entity) {
        return ((ModdedTextDisplay) entity).getNative();
    }

    default boolean getFlag(byte flag) {
        return (getNative().getFlags() & flag) != 0;
    }

    default void setFlag(byte flag, boolean value) {
        int flags = getNative().getFlags();
        if (value) {
            flags |= flag;
        } else {
            flags &= ~flag;
        }
        getNative().setFlags((byte) flags);
    }

    @Override
    default boolean isDefaultBackground() {
        return getFlag(Display.TextDisplay.FLAG_USE_DEFAULT_BACKGROUND);
    }

    @Override
    default void setDefaultBackground(boolean value) {
        setFlag(Display.TextDisplay.FLAG_USE_DEFAULT_BACKGROUND, value);
    }

    @Override
    default @Nullable ARGBColor getBackgroundColor() {
        int value = getNative().getBackgroundColor();
        if (value == Display.TextDisplay.INITIAL_BACKGROUND) {
            return null;
        }
        return ARGBColor.of(value);
    }

    @Override
    default void setBackgroundColor(@Nullable ARGBColor color) {
        getNative().setBackgroundColor(color != null ? color.value() : Display.TextDisplay.INITIAL_BACKGROUND);
    }

    @Override
    default TextAlignment getAlignment() {
        if (getFlag(Display.TextDisplay.FLAG_ALIGN_LEFT)) {
            return TextAlignment.LEFT;
        }
        if (getFlag(Display.TextDisplay.FLAG_ALIGN_RIGHT)) {
            return TextAlignment.RIGHT;
        }
        return TextAlignment.CENTER;
    }

    @Override
    default void setAlignment(TextAlignment alignment) {
        setFlag(Display.TextDisplay.FLAG_ALIGN_LEFT, alignment == TextAlignment.LEFT);
        setFlag(Display.TextDisplay.FLAG_ALIGN_RIGHT, alignment == TextAlignment.RIGHT);
    }

    @Override
    default int getLineWidth() {
        return getNative().getLineWidth();
    }

    @Override
    default void setLineWidth(int width) {
        getNative().setLineWidth(width);
    }

    @Override
    default boolean isSeeThrough() {
        return getFlag(Display.TextDisplay.FLAG_SEE_THROUGH);
    }

    @Override
    default void setSeeThrough(boolean seeThrough) {
        setFlag(Display.TextDisplay.FLAG_SEE_THROUGH, seeThrough);
    }

    @Override
    default boolean isShadowed() {
        return getFlag(Display.TextDisplay.FLAG_SHADOW);
    }

    @Override
    default void setShadowed(boolean shadow) {
        setFlag(Display.TextDisplay.FLAG_SHADOW, shadow);
    }

    @Override
    default Component getText() {
        return getPlatform().getAdventure().asAdventure(getNative().getText());
    }

    @Override
    default void setText(Component text) {
        getNative().setText(getPlatform().getAdventure().asNative(text));
    }
}
