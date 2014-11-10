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

package de.algo.view;

import de.algo.controller.tools.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.ActionListener;

public class MyToolbar extends JToolBar {
        private ToolButton selectedTool;
        private ChangeListener changeListener;

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

                        if (changeListener != null) {
                                changeListener.stateChanged(new ChangeEvent(this));
                        }
                };

                ToolButton cursorButton = new ToolButton(
                        new MouseInputAdapter() {},
                        View.getIcon("cursor_arrow_icon&24"),
                        "Cursor tool",
                        Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                cursorButton.addActionListener(select);
                add(cursorButton);

                add(Box.createVerticalStrut(20));

                ToolButton selectionButton = new ToolButton(
                        new SelectionTool(),
                        View.getIcon("selection_icon&24"),
                        "Selection tool",
                        Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                selectionButton.addActionListener(select);
                add(selectionButton);

                ToolButton pivotButton = new ToolButton(
                        new PivotTool(),
                        View.getIcon("target_icon&24"),
                        "Pivot tool",
                        Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                pivotButton.addActionListener(select);
                add(pivotButton);

                ToolButton translateButton = new ToolButton(
                        new TranslationTool(),
                        View.getIcon("cursor_drag_arrow_icon&24"),
                        "Translation tool",
                        Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                translateButton.addActionListener(select);
                add(translateButton);

                ToolButton scaleButton = new ToolButton(
                        new ScalingTool(),
                        View.getIcon("expand_icon&24"),
                        "Scale tool",
                        Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                scaleButton.addActionListener(select);
                add(scaleButton);

                ToolButton rotateButton = new ToolButton(
                        new RotationTool(),
                        View.getIcon("reload_icon&24"),
                        "Rotation tool",
                        Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                rotateButton.addActionListener(select);
                add(rotateButton);

                ToolButton shearXButton = new ToolButton(
                        new ShearingTool(true),
                        View.getIcon("shear_icon&24"),
                        "X-Shear tool",
                        Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                shearXButton.addActionListener(select);
                add(shearXButton);

                ToolButton shearYButton = new ToolButton(
                        new ShearingTool(false),
                        View.getIcon("shear_y_icon&24"),
                        "Y-Shear tool",
                        Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                shearYButton.addActionListener(select);
                add(shearYButton);

                add(Box.createVerticalStrut(20));

                ColorButton colorButtonPrimary = new ColorButton(
                        ColorButton.PRIMARY,
                        Color.BLACK,
                        "Primary color");
                add(colorButtonPrimary);

                JButton toggleGradient = new JButton(View.getIcon("gradient_icon&24"));
                toggleGradient.setToolTipText("Toggle color gradient");
                add(toggleGradient);

                ColorButton colorButtonSecondary = new ColorButton(
                        ColorButton.SECONDARY,
                        Color.LIGHT_GRAY,
                        "Secondary color");
                colorButtonSecondary.setEnabled(false);
                add(colorButtonSecondary);

                toggleGradient.addActionListener(
                        e -> colorButtonSecondary.setEnabled(!colorButtonSecondary.isEnabled()));

                add(Box.createVerticalStrut(20));

                ToolButton lineButton = new ToolButton(
                        new LineTool(),
                        View.getIcon("line_icon&24"),
                        "Line tool",
                        Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                lineButton.addActionListener(select);
                add(lineButton);

                ToolButton circleButton = new ToolButton(
                        new CircleTool(),
                        View.getIcon("round_line_icon&24"),
                        "Circle tool",
                        Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                circleButton.addActionListener(select);
                add(circleButton);

                ToolButton filledCircleTool = new ToolButton(
                        new FilledCircleTool(),
                        View.getIcon("round_icon&24"),
                        "Filled circle tool",
                        Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                filledCircleTool.addActionListener(select);
                add(filledCircleTool);

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

                add(Box.createVerticalStrut(20));

                JButton resetImageButton = new JButton(View.getIcon("shapes_remove_icon&24"));
                resetImageButton.setToolTipText("Reset image");
                resetImageButton.addActionListener(event ->  {
                        CanvasPanel canvas = View.getSelectedCanvas();
                        if (canvas != null) {
                                canvas.image.resetModifiedImage();
                        }
                });
                add(resetImageButton);

                JButton resetSelectionButton = new JButton(View.getIcon("selection_remove_icon&24"));
                resetSelectionButton.setToolTipText("Reset selection");
                resetSelectionButton.addActionListener(event ->  {
                        CanvasPanel canvas = View.getSelectedCanvas();
                        if (canvas != null) {
                                canvas.image.removeSelection();
                        }
                });
                add(resetSelectionButton);

                JButton resetPivotButton = new JButton(View.getIcon("target_remove_icon&24"));
                resetPivotButton.setToolTipText("Reset pivot");
                resetPivotButton.addActionListener(event ->  {
                        CanvasPanel canvas = View.getSelectedCanvas();
                        if (canvas != null) {
                                canvas.image.removePivot();
                        }
                });
                add(resetPivotButton);
        }

        public MouseInputListener getMouseListener() {
                return selectedTool.getTool();
        }

        public void addChangeListener(ChangeListener listener) {
                this.changeListener = listener;
        }

        public Cursor getToolCursor() {
                return selectedTool.getToolCursor();
        }
}
