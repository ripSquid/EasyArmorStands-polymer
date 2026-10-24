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
package re.koda.easyarmorstands.editor.node;

import re.koda.easyarmorstands.api.editor.button.Button;
import re.koda.easyarmorstands.api.editor.button.ButtonHandler;
import re.koda.easyarmorstands.api.editor.context.ClickContext;
import re.koda.easyarmorstands.api.editor.input.Input;
import re.koda.easyarmorstands.api.editor.node.Node;
import re.koda.easyarmorstands.api.editor.node.NodeHideContext;
import re.koda.easyarmorstands.api.editor.node.NodeShowContext;
import re.koda.easyarmorstands.api.editor.node.NodeUpdateContext;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;

import java.util.ArrayList;
import java.util.List;

public class SwitcherNode implements Node {
    private final List<Entry> entries;
    private final Input input = new SwitchNodeInput();
    private final List<Button> buttons = new ArrayList<>();
    private int index;
    private int lastIndex;

    public SwitcherNode(List<Entry> entries) {
        this.entries = List.copyOf(entries);
    }

    private void switchNode() {
        index++;
        if (index >= entries.size()) {
            index = 0;
        }
    }

    private Entry getEntry() {
        if (entries.isEmpty()) {
            throw new IllegalStateException("Switcher node is empty");
        }
        return entries.get(index);
    }

    @Override
    public void onShow(NodeShowContext context) {
        getEntry().node().onShow(new NodeShowContext() {
            @Override
            public void addButton(Button button, ButtonHandler handler) {
                context.addButton(button, handler);
                buttons.add(button);
            }
        });
    }

    @Override
    public void onHide(NodeHideContext context) {
        getEntry().node().onHide(context);
        buttons.clear();
    }

    @Override
    public void onUpdate(NodeUpdateContext context) {
        if (index != lastIndex) {
            entries.get(lastIndex).node().onHide(new NodeHideContext() {
            });
            for (Button button : buttons) {
                context.removeButton(button);
            }
            buttons.clear();
            entries.get(index).node().onShow(new NodeShowContext() {
                @Override
                public void addButton(Button button, ButtonHandler handler) {
                    context.addButton(button, handler);
                    buttons.add(button);
                }
            });
            lastIndex = index;
        }
        getEntry().node().onUpdate(context);
        context.addInput(input);
    }

    public record Entry(Component name, Node node) {
    }

    class SwitchNodeInput implements Input {
        private static final Style STYLE = Style.style(NamedTextColor.AQUA);

        @Override
        public Component name() {
            return getEntry().name();
        }

        @Override
        public Style style() {
            return STYLE;
        }

        @Override
        public ClickContext.Type clickType() {
            return ClickContext.Type.DROP;
        }

        @Override
        public void execute(ClickContext context) {
            switchNode();
        }
    }
}
