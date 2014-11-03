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
import de.algo.model.MyImage;
import de.algo.util.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.ImageObserver;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class View implements Observer {
        private Controller controller;
        private Model model;

        private JFileChooser fileChooser;
        private SelectDialog slideshowSelectDialog;

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

                slideshowSelectDialog = new SelectDialog(MAIN_FRAME, new ImageSelectorPanel(200, 3, 10, true));

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

                MAIN_FRAME.MENUITEM_SLIDESHOW.addActionListener(event -> {
                        if (model.loadedImages.isEmpty()) {
                                JOptionPane.showMessageDialog(MAIN_FRAME,
                                        "No images have been opened. Select File -> Open images..");
                        } else {
                                slideshowSelectDialog.setImages(model.loadedImages.values());
                                slideshowSelectDialog.pack();
                                slideshowSelectDialog.setLocationRelativeTo(null);
                                slideshowSelectDialog.setVisible(true);
                        }
                });

                slideshowSelectDialog.addActionListener(event -> {
                        Logger.log(Logger.DEBUG, "Starting slideshow with:");

                        Set<String> selected = slideshowSelectDialog.SELECTORPANEL.getSelected();
                        List<MyImage> images = selected
                                .stream()
                                .map(s -> {
                                        Logger.log(Logger.DEBUG, s);
                                        return model.loadedImages.get(s);
                                })
                                .collect(Collectors.toList());

                        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
                        SlideshowPanel slideshowPanel = new SlideshowPanel(images, 2000, 1000,
                                screen.width, screen.height);

                        JWindow slideshowWindow = new JWindow();
                        slideshowWindow.setLayout(new BorderLayout());
                        slideshowWindow.add(slideshowPanel);

                        slideshowWindow.setPreferredSize(screen);

                        slideshowWindow.pack();
                        slideshowWindow.setLocationRelativeTo(null);
                        slideshowWindow.setVisible(true);

                        slideshowPanel.start();
                });
        }

        public static void drawImageCentered(Image img, Graphics g, int width, int height, int iWidth, int iHeight) {
                double aspectRatio = (double) iWidth / (double) iHeight;

                int x, y, w, h;
                x = y = w = h = 0;

                if (aspectRatio >= 1.0) {
                        if (iWidth < width) {
                                w = iWidth;
                                h = iHeight;
                                x = (width - iWidth) / 2;
                                y = (height - iHeight) / 2;
                        } else {
                                w = width;
                                h = (int)(width / aspectRatio);
                                x = 0;
                                y = (height - h) / 2;
                        }
                } else {
                        if (iHeight < height) {
                                w = iWidth;
                                h = iHeight;
                                x = (width - iWidth) / 2;
                                y = (height - iHeight) / 2;
                        } else {
                                h = height;
                                w = (int)(h * aspectRatio);
                                x = (width - w) / 2;
                                y = 0;
                        }
                }

                g.drawImage(img, x, y, w, h, (im ,i, a, b, c, d) -> false);
        }

        @Override
        public void update(Observable o, Object arg) {
                Logger.log(Logger.DEBUG, "Model updated.");
                MAIN_FRAME.repaint();
        }

        public void updateGallery() {
                MAIN_FRAME.GALLERY.addTiles(model.loadedImages.values());
        }
}
