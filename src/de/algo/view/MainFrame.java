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

public class MainFrame extends JFrame {
        public final JPanel CANVASAREA;
        public final MyToolbar TOOLBAR;
        public final GalleryPanel GALLERYAREA;
        public final JTabbedPane CANVASTABS;
        public final ImageSelectorPanel GALLERY;

        public final JMenuBar MENUBAR;

        public final JMenu MENU_FILE;
        public final JMenuItem MENUITEM_OPENIMG;
        public final JMenuItem MENUITEM_EXIT;

        public final JMenu MENU_HISTO;
        public final JMenuItem MENUITEM_HISTO;

        public final JMenu MENU_VIEW;
        public final JMenuItem MENUITEM_SLIDESHOW;

        public MainFrame() {
                super();
                setLayout(new BorderLayout());

                CANVASAREA = new JPanel();
                CANVASAREA.setLayout(new BorderLayout());
                CANVASAREA.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
                CANVASTABS = new JTabbedPane();
                CANVASAREA.add(CANVASTABS, BorderLayout.CENTER);

                TOOLBAR = new MyToolbar();
                TOOLBAR.addChangeListener(e -> CANVASTABS.setCursor(TOOLBAR.getToolCursor()));

                JScrollPane toolbarPane = new JScrollPane(TOOLBAR) {
                        @Override
                        public Dimension getPreferredSize() {
                                return new Dimension(
                                        TOOLBAR.getPreferredSize().width + this.getVerticalScrollBar().getWidth(),
                                        TOOLBAR.getPreferredSize().height + this.getHorizontalScrollBarPolicy());
                        }
                };
                toolbarPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                toolbarPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                toolbarPane.setBorder(null);

                CANVASAREA.add(toolbarPane, BorderLayout.WEST);

                GALLERY = new ImageSelectorPanel(250, 1, 10, false);
                GALLERYAREA = new GalleryPanel(GALLERY);

                add(CANVASAREA, BorderLayout.CENTER);
                add(GALLERYAREA, BorderLayout.EAST);

                InfoBar infoBar = InfoBar.getInfoBar(5);
                add(infoBar, BorderLayout.SOUTH);

                MENUBAR = new JMenuBar();

                MENU_FILE = new JMenu("File");
                MENUITEM_OPENIMG = new JMenuItem("Open images..");
                MENUITEM_EXIT = new JMenuItem("Exit");
                MENU_FILE.add(MENUITEM_OPENIMG);
                MENU_FILE.add(MENUITEM_EXIT);

                MENU_HISTO = new JMenu("Histogram");
                MENUITEM_HISTO = new JMenuItem("Create histogram..");
                MENU_HISTO.add(MENUITEM_HISTO);

                MENU_VIEW = new JMenu("View");
                MENUITEM_SLIDESHOW = new JMenuItem("Start slideshow..");
                MENU_VIEW.add(MENUITEM_SLIDESHOW);

                MENUBAR.add(MENU_FILE);
                MENUBAR.add(MENU_HISTO);
                MENUBAR.add(MENU_VIEW);
                setJMenuBar(MENUBAR);
        }
}
