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

package de.algo.model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;

public class MyImage {
        public final String IDENTIFIER;
        public int[] data;
        private BufferedImage image;

        public MyImage(String identifier, Image source) {
                this.IDENTIFIER = identifier;

                ImageObserver io = (img, infoflags, x, y, width, height) -> false;

                image = new BufferedImage(
                        source.getWidth(io),
                        source.getHeight(io),
                        BufferedImage.TYPE_INT_ARGB);

                image.getGraphics().drawImage(source, 0, 0, io);
                data = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
        }

        public BufferedImage getBufferedImage() {
                return image;
        }
}
