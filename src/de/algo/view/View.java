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
import de.algo.model.colorquantization.ColorQuantizer;
import de.algo.util.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Loosely represents the View as specified by the MVC concept.
 */
public class View implements Observer {
        private static View view;

        private Controller controller;
        private Model model;

        private JFileChooser imageChooser;
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

                ToolTipManager.sharedInstance().setInitialDelay(100);

                FILEFILTER = new FileNameExtensionFilter("JPG and GIF Images", "jpg", "jpeg", "gif");
                imageChooser = new JFileChooser(".");
                imageChooser.setMultiSelectionEnabled(true);
                imageChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                imageChooser.setFileFilter(FILEFILTER);

                slideshowSelectDialog = new SelectDialog(MAIN_FRAME, new ImageSelectorPanel(200, 3, 10, true));

                addListeners();

                MAIN_FRAME.setExtendedState(MAIN_FRAME.getExtendedState() | JFrame.MAXIMIZED_BOTH);
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

                MAIN_FRAME.MENUITEM_HISTO.addActionListener(event -> {
                        if (model.loadedImages.isEmpty()) {
                                JOptionPane.showMessageDialog(MAIN_FRAME,
                                        "No images have been opened. Select File -> Open images..");
                        } else if (getSelectedCanvas() == null) {
                                JOptionPane.showMessageDialog(MAIN_FRAME,
                                        "Select an image for editing before outputting its histogram.");
                        } else {
                                JFileChooser chooser = new JFileChooser(".");
                                chooser.setMultiSelectionEnabled(false);
                                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                                chooser.setDialogType(JFileChooser.SAVE_DIALOG);
                                
                                final int result = chooser.showDialog(MAIN_FRAME, "Create histogram");

                                if (result == JFileChooser.APPROVE_OPTION) {
                                        File file = chooser.getSelectedFile();

                                        if (file.exists()) {
                                                final int overWrite = JOptionPane.showConfirmDialog(MAIN_FRAME,
                                                        "Really overwrite existing file '" + file.getName() + "'?",
                                                        "Overwrite file?",
                                                        JOptionPane.YES_NO_OPTION);

                                                if (overWrite == JOptionPane.OK_OPTION) {
                                                        controller.createHistogram(
                                                                getSelectedCanvas().image,
                                                                file);
                                                }
                                        } else {
                                                controller.createHistogram(getSelectedCanvas().image, file);
                                        }
                                }
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

                MAIN_FRAME.MENUITEM_CLUSTER.addActionListener(event -> {
                        if (getSelectedCanvas() == null) {
                                JOptionPane.showMessageDialog(MAIN_FRAME,
                                        "No images have been opened for editing.");
                        } else {
                                int count = 16;

                                JDialog countDialog = new JDialog(MAIN_FRAME, "Number of clusters", true);
                                JPanel countPanel = new JPanel();
                                countPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
                                countDialog.add(countPanel);

                                countPanel.setLayout(new BoxLayout(countPanel, BoxLayout.Y_AXIS));

                                JLabel label = new JLabel("Put in the number of clusters to calculate:");
                                countPanel.add(label);

                                countPanel.add(Box.createVerticalStrut(10));

                                JTextField input = new JTextField(Integer.toString(count));
                                countPanel.add(input);

                                countPanel.add(Box.createVerticalStrut(10));

                                JButton confirm = new JButton("Confirm");
                                countPanel.add(confirm);

                                ActionListener confirmAction = e -> countDialog.setVisible(false);

                                confirm.addActionListener(confirmAction);
                                input.addActionListener(confirmAction);

                                countDialog.pack();
                                countDialog.setLocationRelativeTo(null);
                                countDialog.setVisible(true);

                                try {
                                        count = Integer.decode(input.getText());
                                } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                }

                                JDialog visualizationDialog = new JDialog(MAIN_FRAME, false);
                                visualizationDialog.setLayout(new BorderLayout());
                                visualizationDialog.add(new ClusteringTestPanel(getSelectedCanvas().image, count));
                                visualizationDialog.addWindowListener(new WindowAdapter() {
                                        @Override
                                        public void windowClosing(WindowEvent e) {
                                                visualizationDialog.dispose();
                                        }
                                });
                                visualizationDialog.pack();
                                visualizationDialog.setLocationRelativeTo(null);
                                visualizationDialog.setVisible(true);
                        }
                });

                MAIN_FRAME.MENUITEM_QUANTIZE.addActionListener(event -> {
                        if (getSelectedCanvas() == null) {
                                JOptionPane.showMessageDialog(MAIN_FRAME,
                                        "No images have been opened for editing.");
                        } else {
                                int percent = 50;

                                JDialog quantizeDialog = new JDialog(MAIN_FRAME, "Color quantization", true);
                                JPanel quantizePanel = new JPanel();
                                quantizePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
                                quantizeDialog.add(quantizePanel);

                                quantizePanel.setLayout(new BoxLayout(quantizePanel, BoxLayout.Y_AXIS));

                                JLabel label = new JLabel("Put in the percentage of colors you want to keep:");
                                quantizePanel.add(label);

                                quantizePanel.add(Box.createVerticalStrut(10));

                                //JTextField input = new JTextField(Float.toString(percent));
                                JTextField input = new JTextField(Integer.toString(percent));
                                quantizePanel.add(input);

                                quantizePanel.add(Box.createVerticalStrut(10));

                                JButton confirm = new JButton("Confirm");
                                quantizePanel.add(confirm);

                                ActionListener confirmAction = e -> quantizeDialog.setVisible(false);

                                confirm.addActionListener(confirmAction);
                                input.addActionListener(confirmAction);

                                quantizeDialog.pack();
                                quantizeDialog.setLocationRelativeTo(null);
                                quantizeDialog.setVisible(true);

                                try {
                                        percent = Integer.decode(input.getText());
                                        if (percent < 0  ) { //|| percent > 100.0f) {
                                                InfoBar.publish("Error: invalid percentage. Input a number between 0 and 100!");
                                                return;
                                        } else {
                                                // Quantize colors!
                                                MyImage image = getSelectedCanvas().image;
                                                // ColorQuantizer.quantizeColors(image, percent / 100.0f);
                                                // ColorQuantizer.quantizeColors(image, percent);
                                                ColorQuantizer.quantizeColorsWithClustering(image, 16);
                                                image.refresh();
                                        }
                                } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                }
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

                                        int index = MAIN_FRAME.CANVASTABS.getTabCount();

                                        CanvasPanel canvas =
                                                new CanvasPanel(model.loadedImages.get(s), MAIN_FRAME.TOOLBAR, index);
                                        openCanvasPanels.put(s, canvas);

                                        JScrollPane scrollPane = new JScrollPane();
                                        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
                                        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
                                        scrollPane.getViewport().add(canvas);

                                        MAIN_FRAME.CANVASTABS.add(title, scrollPane);
                                        MAIN_FRAME.CANVASTABS.setSelectedComponent(scrollPane);
                                } else {
                                        MAIN_FRAME.CANVASTABS.setSelectedIndex(openCanvasPanels.get(s).index);
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
                        new SlideshowDialog(panel, screen, MAIN_FRAME);
                });
        }

        private void openFiles() {
                final int result = imageChooser.showOpenDialog(MAIN_FRAME);
                if (result == JFileChooser.APPROVE_OPTION) {
                        File[] files = imageChooser.getSelectedFiles();
                        controller.processSelectedFiles(files);
                }
        }

        public static ImageIcon getIcon(String path) {
                File file = new File("resources/icons/" + path + ".png");

                if (file.exists() && file.canRead()) {
                        return new ImageIcon(file.getPath());
                } else {
                        Logger.log(Logger.ERROR, "Icon path does not exist or can not be read: " + file.getPath());
                        return null;
                }
        }

        public static void drawImageCentered(Image img, Graphics g, int width, int height, int iWidth, int iHeight) {
                drawImageCenteredMargin(img, g, width, height, iWidth, iHeight, 0);
        }

        public static void drawImageCenteredMargin(Image img, Graphics g, int width, int height,
                                                   int iWidth, int iHeight, int margin)
        {
                double aspectRatio = (double) iWidth / (double) iHeight;
                 width -= margin;
                height -= margin;

                int x, y, w, h;

                if (iWidth < width && iHeight < height) {
                        w = iWidth;
                        h = iHeight;
                        x = (width - iWidth) / 2 + margin / 2;
                        y = (height - iHeight) / 2 + margin / 2;
                } else if (aspectRatio >= 1.0) {
                        w = width;
                        h = (int)(width / aspectRatio);
                        x = margin / 2;
                        y = (height - h) / 2 + margin / 2;
                } else {
                        h = height;
                        w = (int)(h * aspectRatio);
                        x = (width - w) / 2 + margin / 2;
                        y = margin / 2;
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
