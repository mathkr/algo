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
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SlideshowDialog extends JDialog {
        public SlideshowDialog(SlideshowPanel slideshowPanel, Dimension size, JFrame parent) {
                super(parent, "Slideshow", false);

                addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyReleased(KeyEvent e) {
                                slideshowPanel.stop();
                                dispose();
                        }
                });

                addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                                slideshowPanel.stop();
                                dispose();
                        }
                });

                setLayout(new BorderLayout());
                add(slideshowPanel, BorderLayout.CENTER);

                setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                setUndecorated(true);

                setPreferredSize(size);

                pack();
                setLocationRelativeTo(null);
                setVisible(true);

                slideshowPanel.start();
        }
}
