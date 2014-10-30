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

import de.algo.controller.Controller;
import de.algo.model.Model;
import de.algo.util.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class View implements Observer {
        private Controller controller;
        private Model model;

        private JFileChooser fileChooser;

        public final MainFrame MAIN_FRAME;
        public final FileNameExtensionFilter FILEFILTER;

        public View(Model model, Controller controller) {
                this.controller = controller;
                this.model = model;
                model.addObserver(this);

                MAIN_FRAME = new MainFrame();

                FILEFILTER = new FileNameExtensionFilter("JPG and GIF Images", "jpg", "jpeg", "gif");
                fileChooser = new JFileChooser(".");
                fileChooser.setMultiSelectionEnabled(true);
                fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                fileChooser.setFileFilter(FILEFILTER);

                addListeners();

                MAIN_FRAME.pack();
                MAIN_FRAME.setVisible(true);
        }

        private void addListeners() {
                MAIN_FRAME.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                                controller.exit();
                        }
                });

                MAIN_FRAME.MENUITEM_EXIT.addActionListener(event -> controller.exit());

                MAIN_FRAME.MENUITEM_OPENIMG.addActionListener(event -> {
                        final int result = fileChooser.showOpenDialog(MAIN_FRAME);
                        if (result == JFileChooser.APPROVE_OPTION) {
                                File[] files = fileChooser.getSelectedFiles();
                                controller.processSelectedFiles(files);
                        }
                });
        }

        @Override
        public void update(Observable o, Object arg) {
                Logger.log(Logger.DEBUG, "Model updated.");
                MAIN_FRAME.repaint();
        }

        public void updateGallery() {
                MAIN_FRAME.GALLERY.removeAll();
                model.loadedImages.forEach((identifier, image) -> {
                        GalleryTilePanel panel = new GalleryTilePanel(identifier, image);
                        panel.addMouseListener(new MouseAdapter() {
                                @Override
                                public void mouseClicked(MouseEvent e) {
                                        Logger.log(Logger.DEBUG, identifier + " clicked in gallery.");
                                }
                        });
                        MAIN_FRAME.GALLERY.add(panel);
                });
                MAIN_FRAME.GALLERY.setPreferredSize(new Dimension(300, model.loadedImages.size() * 300));
                MAIN_FRAME.revalidate();
        }
}
