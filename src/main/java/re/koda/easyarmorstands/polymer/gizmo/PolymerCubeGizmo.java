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

import eu.pb4.polymer.virtualentity.api.elements.BlockDisplayElement;
import com.mojang.math.Transformation;
import net.minecraft.util.Brightness;
import net.minecraft.world.entity.Display;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3dc;
import org.joml.Vector3f;
import re.koda.easyarmorstands.gizmo.AbstractCubeGizmo;
import re.koda.easyarmorstands.gizmo.api.color.GizmoColor;

import java.util.HashMap;
import java.util.Map;

/**
 * A single glowing block display, the building block of every other gizmo.
 * <p>
 * Port of {@code ServerDisplayCubeGizmo} from gizmo-modded (https://github.com/56738/gizmo-modded)
 * to Polymer virtual entities.
 */
class PolymerCubeGizmo extends AbstractCubeGizmo {
    private static final Map<GizmoColor, BlockState> COLORS = new HashMap<>();

    static {
        COLORS.put(GizmoColor.WHITE, Blocks.CONCRETE.white().defaultBlockState());
        COLORS.put(GizmoColor.RED, Blocks.CONCRETE.red().defaultBlockState());
        COLORS.put(GizmoColor.GREEN, Blocks.CONCRETE.lime().defaultBlockState());
        COLORS.put(GizmoColor.BLUE, Blocks.CONCRETE.blue().defaultBlockState());
        COLORS.put(GizmoColor.YELLOW, Blocks.CONCRETE.yellow().defaultBlockState());
        COLORS.put(GizmoColor.GRAY, Blocks.CONCRETE.lightGray().defaultBlockState());
        COLORS.put(GizmoColor.AQUA, Blocks.CONCRETE.lightBlue().defaultBlockState());
    }

    private final PolymerGizmoFactory factory;
    private BlockDisplayElement element;

    PolymerCubeGizmo(PolymerGizmoFactory factory) {
        this.factory = factory;
    }

    @Override
    public void show() {
        if (element != null) {
            return;
        }
        element = new BlockDisplayElement();
        configure(element);
        update(element);
        factory.holder().addElement(element);
        checkAndClearDirty();
    }

    @Override
    public void update() {
        if (element == null) {
            return;
        }
        factory.holder();
        if (checkAndClearDirty()) {
            update(element);
            element.tick();
        }
    }

    @Override
    public void hide() {
        if (element == null) {
            return;
        }
        factory.holder().removeElement(element);
        element = null;
    }

    private void configure(BlockDisplayElement element) {
        element.setBrightness(Brightness.FULL_BRIGHT);
        element.setGlowing(true);
        element.setInterpolationDuration(3);
        element.setTeleportDuration(3);
    }

    private void update(BlockDisplayElement element) {
        Vector3dc position = getPosition();
        GizmoColor color = getColor();
        element.setOverridePos(new Vec3(position.x(), position.y(), position.z()));
        element.setBlockState(COLORS.get(color));
        element.setGlowColorOverride(color.asRGB());
        element.setTransformation(new Transformation(
                new Vector3f(getOffset()),
                new Quaternionf(getRotation()),
                new Vector3f(getScale()),
                new Quaternionf()));
        element.setBillboardMode(isBillboard() ? Display.BillboardConstraints.CENTER : Display.BillboardConstraints.FIXED);
        element.startInterpolation();
    }
}
