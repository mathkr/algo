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
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class View implements Observer {
        private static View view;

        private Controller controller;
        private Model model;

        private JFileChooser fileChooser;
        private SelectDialog slideshowSelectDialog;

        public final MainFrame MAIN_FRAME;
        public final FileNameExtensionFilter FILEFILTER;

        public Map<String, CanvasPanel> openCanvasPanels;

        private View(Model model, Controller controller) {
                this.controller = controller;
                this.model = model;
                model.addObserver(this);

                openCanvasPanels = new HashMap<>();

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

        public static View createView(Model model, Controller controller) {
                if (view == null) {
                        view = new View(model, controller);
                }

                return view;
        }

        public static CanvasPanel getSelectedCanvas() {
                if (view != null) {
                        Component activeTab = view.MAIN_FRAME.CANVASTABS.getSelectedComponent();

                        if (activeTab != null) {
                                return (CanvasPanel)((JScrollPane)activeTab).getViewport().getView();
                        }
                }

                return null;
        }

        private void addListeners() {
                MAIN_FRAME.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                                controller.exit();
                        }
                });

                MAIN_FRAME.MENUITEM_OPENIMG.addActionListener(event -> openFiles());
                MAIN_FRAME.MENUITEM_EXIT.addActionListener(event -> controller.exit());

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

                MAIN_FRAME.GALLERYAREA.ADD.addActionListener(event -> openFiles());

                MAIN_FRAME.GALLERYAREA.EDIT.addActionListener(event -> {
                        if (MAIN_FRAME.GALLERY.getSelected().isEmpty()) {
                                return;
                        }

                        MAIN_FRAME.GALLERY.getSelected().forEach(s -> {
                                if (!openCanvasPanels.containsKey(s)) {
                                        File file = new File(s);
                                        String title = file.getName();

                                        CanvasPanel canvas =
                                                new CanvasPanel(model.loadedImages.get(s), MAIN_FRAME.TOOLBAR);
                                        openCanvasPanels.put(s, canvas);

                                        JScrollPane scrollPane = new JScrollPane();
                                        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
                                        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
                                        scrollPane.getViewport().add(canvas);

                                        MAIN_FRAME.CANVASTABS.add(title, scrollPane);
                                        MAIN_FRAME.CANVASTABS.setSelectedComponent(scrollPane);
                                }
                        });
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
                        SlideshowPanel panel = new SlideshowPanel(images, 2000, 1000, screen.width, screen.height);
                        SlideshowDialog show = new SlideshowDialog(panel, screen, MAIN_FRAME);
                        show.toFront();
                });
        }

        private void openFiles() {
                final int result = fileChooser.showOpenDialog(MAIN_FRAME);
                if (result == JFileChooser.APPROVE_OPTION) {
                        File[] files = fileChooser.getSelectedFiles();
                        controller.processSelectedFiles(files);
                }
        }

        public static ImageIcon getIcon(String path) {
                File file = new File("icons/black/png/" + path + ".png");

                if (file.exists() && file.canRead()) {
                        return new ImageIcon(file.getPath());
                } else {
                        Logger.log(Logger.ERROR, "Icon path does not exist or can not be read: " + file.getPath());
                        return null;
                }
        }

        public static void drawImageCentered(Image img, Graphics g, int width, int height, int iWidth, int iHeight) {
                double aspectRatio = (double) iWidth / (double) iHeight;

                int x, y, w, h;

                if (iWidth < width && iHeight < height) {
                        w = iWidth;
                        h = iHeight;
                        x = (width - iWidth) / 2;
                        y = (height - iHeight) / 2;
                } else if (aspectRatio >= 1.0) {
                        w = width;
                        h = (int)(width / aspectRatio);
                        x = 0;
                        y = (height - h) / 2;
                } else {
                        h = height;
                        w = (int)(h * aspectRatio);
                        x = (width - w) / 2;
                        y = 0;
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
