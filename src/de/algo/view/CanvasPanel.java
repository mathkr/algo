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

import de.algo.model.MyImage;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class CanvasPanel extends JPanel implements Observer {
        private MyImage image;

        public CanvasPanel(MyImage image) {
                this.image = image;
                image.addObserver(this);

                setPreferredSize(new Dimension(image.getBufferedImage().getWidth(),
                        image.getBufferedImage().getHeight()));
        }

        @Override
        public void update(Observable o, Object arg) {
                repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image.getBufferedImage(), 0, 0, this);
        }
}