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

import de.algo.controller.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import java.io.File;

public class View {
        private final Controller CONTROLLER;

        public final MainFrame MAIN_FRAME;
        public final JFileChooser FILECHOOSER;
        public final FileNameExtensionFilter FILEFILTER;

        public View(Controller controller) {
                CONTROLLER = controller;

                MAIN_FRAME = new MainFrame();

                FILECHOOSER = new JFileChooser(".");
                FILECHOOSER.setMultiSelectionEnabled(true);
                FILECHOOSER.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                FILEFILTER = new FileNameExtensionFilter("JPG and GIF Images", "jpg", "jpeg", "gif");
                FILECHOOSER.setFileFilter(FILEFILTER);

                addListeners();

                MAIN_FRAME.setVisible(true);
        }

        private void addListeners() {
                MAIN_FRAME.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                                CONTROLLER.exit();
                        }
                });

                MAIN_FRAME.MENUITEM_EXIT.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                CONTROLLER.exit();
                        }
                });

                MAIN_FRAME.MENUITEM_OPENIMG.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                final int result = FILECHOOSER.showOpenDialog(MAIN_FRAME);
                                if (result == JFileChooser.APPROVE_OPTION) {
                                        File[] files = FILECHOOSER.getSelectedFiles();
                                        CONTROLLER.processSelectedFiles(files);
                                }
                        }
                });
        }
}
