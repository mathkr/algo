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

public class GalleryTilePanel extends JPanel {
        public String identifier;
        private Image image;

        private double aspectRatio;

        public GalleryTilePanel(String identifier, Image image) {
                super();

                this.identifier = identifier;
                this.image = image;
                aspectRatio = (double) image.getWidth(this) / (double) image.getHeight(this);
        }

        @Override
        public Dimension getPreferredSize() {
                int width = getParent().getWidth();
                int height = (int)(width / aspectRatio);
                return new Dimension(width, height);
        }

        @Override
        public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
}
