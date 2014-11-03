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
        public final JScrollPane SCROLLPANE_CANVAS;
        public final JScrollPane SCROLLPANE_GALLERY;
        public final JPanel CANVAS;
        public final ImageSelectorPanel GALLERY;

        public final JMenuBar MENUBAR;
        public final JMenu MENU_FILE;
        public final JMenuItem MENUITEM_OPENIMG;
        public final JMenuItem MENUITEM_EXIT;

        public final JMenu MENU_VIEW;
        public final JMenuItem MENUITEM_SLIDESHOW;

        public MainFrame() {
                super();
                setLayout(new BorderLayout());

                CANVAS = new JPanel();

                GALLERY = new ImageSelectorPanel(300, 1, 10, false);

                SCROLLPANE_CANVAS = new JScrollPane(CANVAS);
                SCROLLPANE_GALLERY = new JScrollPane(GALLERY);
                SCROLLPANE_GALLERY.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                SCROLLPANE_GALLERY.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                SCROLLPANE_GALLERY.getVerticalScrollBar().setUnitIncrement(16);

                add(SCROLLPANE_CANVAS, BorderLayout.CENTER);
                add(SCROLLPANE_GALLERY, BorderLayout.EAST);

                MENUBAR = new JMenuBar();

                MENU_FILE = new JMenu("File");
                MENUITEM_OPENIMG = new JMenuItem("Open images..");
                MENUITEM_EXIT = new JMenuItem("Exit");
                MENU_FILE.add(MENUITEM_OPENIMG);
                MENU_FILE.add(MENUITEM_EXIT);

                MENU_VIEW = new JMenu("View");
                MENUITEM_SLIDESHOW = new JMenuItem("Start slideshow..");
                MENU_VIEW.add(MENUITEM_SLIDESHOW);

                MENUBAR.add(MENU_FILE);
                MENUBAR.add(MENU_VIEW);
                setJMenuBar(MENUBAR);
        }
}
