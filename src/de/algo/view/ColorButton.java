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

import javax.swing.*;
import javax.swing.colorchooser.DefaultColorSelectionModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ColorButton extends JButton {
        public static final int PRIMARY = 0;
        public static final int SECONDARY = 1;

        private static final Map<Integer, Color> COLORS = new HashMap<>();

        private int id;
        private Color color;

        public ColorButton(int id, Color initial, String tooltip) {
                super(View.getIcon("empty_icon&24"));

                this.id = id;
                this.color = initial;
                COLORS.put(id, initial);

                setToolTipText(tooltip);

                addActionListener(e -> {
                        JColorChooser picker = new JColorChooser(color);
                        picker.removeChooserPanel(picker.getChooserPanels()[0]);
                        picker.setSelectionModel(new DefaultColorSelectionModel());
                        picker.setPreviewPanel(new JPanel());
                        picker.setColor(color);

                        JDialog dialog = JColorChooser.createDialog(
                                ColorButton.this,
                                "Choose a color",
                                true,
                                picker,
                                actionEvent -> {
                                        color = picker.getColor();
                                        COLORS.put(id, color);
                                        ColorButton.this.repaint();
                                },
                                null);
                        dialog.setLocationRelativeTo(null);

                        dialog.setVisible(true);
                });
        }

        @Override
        public void setEnabled(boolean enabled) {
                super.setEnabled(enabled);

                if (enabled) {
                        COLORS.put(id, color);
                } else {
                        COLORS.remove(id);
                }
        }

        public static Color getColor(int id) {
                return COLORS.getOrDefault(id, COLORS.get(PRIMARY));
        }

        @Override
        protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                if (isEnabled()) {
                        g.setColor(COLORS.get(id));
                        g.fillRect(5, 5, 25, 25);
                }
        }
}
