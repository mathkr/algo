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

package de.algo.view.tools;

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

                        ((CanvasPanel)e.getComponent()).image.setSelection(start, end);
                }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
                if (start != null) {
                        end = new Vector3(e.getX(), e.getY(), 1);

                        ((CanvasPanel)e.getComponent()).image.setSelection(start, end);
                }
        }
}
