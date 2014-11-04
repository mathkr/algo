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

import de.algo.model.MyImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Collection;

public class SelectDialog extends JDialog {
        public final ImageSelectorPanel SELECTORPANEL;
        private JButton select;
        private JButton accept;
        private JButton cancel;
        private ActionListener actionListener;

        public SelectDialog(JFrame owner, ImageSelectorPanel selectorPanel) {
                super(owner, "Select images..", true);

                this.SELECTORPANEL = selectorPanel;
                SELECTORPANEL.addChangeListener(e -> {
                        if (!SELECTORPANEL.getSelected().isEmpty()) {
                                accept.setEnabled(true);
                        } else {
                                accept.setEnabled(false);
                        }
                });

                JScrollPane scrollpane = new JScrollPane(selectorPanel);
                scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                scrollpane.getVerticalScrollBar().setUnitIncrement(16);

                setLayout(new BorderLayout());
                add(scrollpane, BorderLayout.CENTER);

                JPanel buttonsPanel = new JPanel();
                buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));

                select = new JButton("Select all");
                select.addActionListener(e -> {
                        SELECTORPANEL.selectAll();
                        accept.setEnabled(true);
                });
                buttonsPanel.add(select);

                buttonsPanel.add(Box.createHorizontalStrut(10));
                buttonsPanel.add(new JSeparator(JSeparator.VERTICAL));
                buttonsPanel.add(Box.createHorizontalStrut(10));

                accept = new JButton("Accept");
                accept.setEnabled(false);
                accept.addActionListener(e -> submit(true));
                buttonsPanel.add(accept);

                buttonsPanel.add(Box.createHorizontalStrut(10));

                cancel = new JButton("Cancel");
                cancel.addActionListener(e -> submit(false));
                buttonsPanel.add(cancel);

                buttonsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

                add(buttonsPanel, BorderLayout.SOUTH);

                addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                                submit(false);
                        }
                });
        }

        public void addActionListener(ActionListener actionListener) {
                this.actionListener = actionListener;
        }

        private void submit(boolean success) {
                if (success) {
                        if (actionListener != null) {
                                actionListener.actionPerformed(new ActionEvent(this, 0, ""));
                        }
                }

                setVisible(false);
        }

        public void setImages(Collection<MyImage> images) {
                SELECTORPANEL.clear();
                SELECTORPANEL.addTiles(images);
                accept.setEnabled(false);
        }
}
