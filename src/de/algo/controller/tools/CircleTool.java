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

package de.algo.controller.tools;

import de.algo.model.Bresenham;
import de.algo.model.MyImage;
import de.algo.model.Vector3;
import de.algo.view.CanvasPanel;
import de.algo.view.ColorButton;

import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseEvent;

public class CircleTool extends MouseInputAdapter {
        public Vector3 start;

        @Override
        public void mousePressed(MouseEvent e) {
                start = new Vector3(e.getX(), e.getY(), 1);
                ((CanvasPanel) e.getComponent()).image.removeSelection();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
                circle(new Vector3(e.getX(), e.getY()), ((CanvasPanel) e.getComponent()).image, false);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
                circle(new Vector3(e.getX(), e.getY()), ((CanvasPanel) e.getComponent()).image, true);
        }

        private void circle(Vector3 end, MyImage image, boolean temporary) {
                if (start != null) {
                        int radius = (int) Vector3.getDistance(start, end);
                        if (radius == 0) {
                                return;
                        }

                        image.clearShapes();

                        int colorA = ColorButton.getColor(ColorButton.PRIMARY).getRGB();
                        int colorB = ColorButton.getColor(ColorButton.SECONDARY).getRGB();

                        Bresenham.drawCircle(start, radius, image, colorA, colorB);

                        image.applyShapes(temporary);
                }
        }
}
