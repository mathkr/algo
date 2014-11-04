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
import javax.swing.border.Border;
import java.awt.*;

public class GalleryPanel extends JPanel {
        public final JScrollPane SCROLLPANE_GALLERY;
        public final ImageSelectorPanel GALLERY;
        public final JButton ADD;
        public final JButton EDIT;

        public GalleryPanel(ImageSelectorPanel gallery) {
                super();

                this.GALLERY = gallery;

                setLayout(new BorderLayout());
                setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 0));

                SCROLLPANE_GALLERY = new JScrollPane(GALLERY);
                SCROLLPANE_GALLERY.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                SCROLLPANE_GALLERY.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                SCROLLPANE_GALLERY.getVerticalScrollBar().setUnitIncrement(16);

                add(SCROLLPANE_GALLERY, BorderLayout.CENTER);

                JPanel buttonsPanel = new JPanel();
                ADD = new JButton("Add..", View.getIcon("folder_open_icon&16"));
                buttonsPanel.add(ADD);

                EDIT = new JButton("Edit", View.getIcon("pencil_icon&16"));
                EDIT.setEnabled(false);
                buttonsPanel.add(EDIT);

                GALLERY.addChangeListener(e -> {
                        EDIT.setEnabled(!GALLERY.getSelected().isEmpty());
                });

                Border border = BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(1, 0, 0, 1),
                        BorderFactory.createLineBorder(Color.GRAY));
                buttonsPanel.setBorder(border);
                add(buttonsPanel, BorderLayout.SOUTH);
        }
}
