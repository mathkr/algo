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

import de.algo.model.MyImage;
import de.algo.model.Vector3;
import de.algo.view.CanvasPanel;

import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseEvent;

public class SelectionTool extends MouseInputAdapter {
        public Vector3 start;
        public Vector3 end;

        @Override
        public void mousePressed(MouseEvent e) {
                start = new Vector3(e.getX(), e.getY(), 1);
                end = null;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
                if (start != null) {
                        end = new Vector3(e.getX(), e.getY(), 1);

                        select(((CanvasPanel)e.getComponent()).image, end);
                }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
                if (start != null) {
                        end = new Vector3(e.getX(), e.getY(), 1);

                        select(((CanvasPanel)e.getComponent()).image, end);
                }
        }

        private void select(MyImage image, Vector3 end) {
                int w = image.transformedImage.getWidth();
                int h = image.transformedImage.getHeight();

                start.x = start.x <  0 ? 0 : start.x;
                start.x = start.x >= w ? w - 1 : start.x;

                start.y = start.y <  0 ? 0 : start.y;
                start.y = start.y >= h ? h - 1 : start.y;

                end.x = end.x <  0 ? 0 : end.x;
                end.x = end.x >= w ? w - 1 : end.x;

                end.y = end.y <  0 ? 0 : end.y;
                end.y = end.y >= h ? h - 1 : end.y;

                if (Math.abs(start.x - end.x) > 1 && Math.abs(start.y - end.y) > 1) {
                        image.setSelection(start, end);
                }
        }
}
