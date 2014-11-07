/* Programming assignments for 'Algorithmen und Datenstrukturen' at the
 * Hochschule Bremerhaven, GERMANY.
 *
 * Copyright (C) 2014 Matthis Krause
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses/>.
 */

package de.algo.view.tools;

import de.algo.view.View;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import java.awt.event.ActionListener;

public class MyToolbar extends JToolBar {
        private ToolButton selectedTool;

        public MyToolbar() {
                super(JToolBar.VERTICAL);
                setFloatable(false);

                setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

                ActionListener select = e -> {
                        ToolButton newButton =((ToolButton)e.getSource());
                        if (selectedTool != null) {
                                selectedTool.setActive(false);
                        }
                        newButton.setActive(true);
                        selectedTool = newButton;
                };

                ToolButton cursorButton = new ToolButton(
                        new MouseInputAdapter() {},
                        View.getIcon("cursor_arrow_icon&24"),
                        "Cursor tool");
                cursorButton.addActionListener(select);
                add(cursorButton);

                add(Box.createVerticalStrut(20));

                ToolButton selectionButton = new ToolButton(
                        new SelectionTool(),
                        View.getIcon("checkbox_unchecked_icon&24"),
                        "Selection tool");
                selectionButton.addActionListener(select);
                add(selectionButton);

                ToolButton pivotButton = new ToolButton(
                        new PivotTool(),
                        View.getIcon("target_icon&24"),
                        "Pivot tool");
                pivotButton.addActionListener(select);
                add(pivotButton);

                ToolButton translateButton = new ToolButton(
                        new TranslationTool(),
                        View.getIcon("cursor_drag_arrow_icon&24"),
                        "Translation tool");
                translateButton.addActionListener(select);
                add(translateButton);

                ToolButton scaleButton = new ToolButton(
                        new ScalingTool(),
                        View.getIcon("arrow_two_head_icon&24"),
                        "Scale tool");
                scaleButton.addActionListener(select);
                add(scaleButton);

                ToolButton rotateButton = new ToolButton(
                        new RotationTool(),
                        View.getIcon("reload_icon&24"),
                        "Rotation tool");
                rotateButton.addActionListener(select);
                add(rotateButton);

                ToolButton shearXButton = new ToolButton(
                        new ShearingTool(true),
                        View.getIcon("shear_icon&24"),
                        "X-Shear tool");
                shearXButton.addActionListener(select);
                add(shearXButton);

                ToolButton shearYButton = new ToolButton(
                        new ShearingTool(false),
                        View.getIcon("shear_y_icon&24"),
                        "Y-Shear tool");
                shearYButton.addActionListener(select);
                add(shearYButton);

                selectedTool = cursorButton;
                selectedTool.setActive(true);

                add(Box.createVerticalStrut(20));

                JButton copyButton = new JButton(View.getIcon("clipboard_copy_icon&24"));
                copyButton.setToolTipText("Copy tool");
                copyButton.addActionListener(new CopyTool());
                add(copyButton);

                JButton pasteButton = new JButton(View.getIcon("clipboard_past_icon&24"));
                pasteButton.setToolTipText("Paste tool");
                pasteButton.addActionListener(new PasteTool());
                add(pasteButton);
        }

        public MouseInputListener getMouseListener() {
                return selectedTool.getTool();
        }
}
