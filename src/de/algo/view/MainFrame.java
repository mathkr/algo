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
        public final JPanel CANVAS;
        public final JPanel GALLERY;

        public final JSplitPane SPLITPANE;

        public final JMenuBar MENUBAR;
        public final JMenu MENU_FILE;
        public final JMenuItem MENUITEM_OPENIMG;
        public final JMenuItem MENUITEM_EXIT;

        public MainFrame() {
                super();
                setLayout(new BorderLayout());

                CANVAS = new JPanel();
                GALLERY = new JPanel();

                SPLITPANE = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, CANVAS, GALLERY);
                SPLITPANE.setOneTouchExpandable(true);
                SPLITPANE.setResizeWeight(1.0);
                add(SPLITPANE, BorderLayout.CENTER);

                MENUBAR = new JMenuBar();
                MENU_FILE = new JMenu("Datei");
                MENUITEM_OPENIMG = new JMenuItem("Bilder oeffnen..");
                MENUITEM_EXIT = new JMenuItem("Beenden");

                MENU_FILE.add(MENUITEM_OPENIMG);
                MENU_FILE.add(MENUITEM_EXIT);
                MENUBAR.add(MENU_FILE);
                setJMenuBar(MENUBAR);
        }
}
