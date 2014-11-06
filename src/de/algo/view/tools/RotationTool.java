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

import de.algo.model.Matrix;
import de.algo.model.MyImage;
import de.algo.model.Vector3;
import de.algo.view.CanvasPanel;

import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseEvent;

public class RotationTool extends MouseInputAdapter {
        public Vector3 start;

        @Override
        public void mousePressed(MouseEvent e) {
                start = new Vector3(e.getX(), e.getY(), 1);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
                rotate(new Vector3(e.getX(), e.getY()), ((CanvasPanel) e.getComponent()).image);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
                rotate(new Vector3(e.getX(), e.getY()), ((CanvasPanel) e.getComponent()).image);
                start = new Vector3(e.getX(), e.getY());
        }

        private void rotate(Vector3 end, MyImage image) {
                if (start != null) {
                        Vector3 pivot;

                        if (image.hasPivot()) {
                                pivot = image.getPivot();
                        } else if (image.hasSelection()) {
                                pivot = image.getSelection().getCenter();
                        } else {
                                pivot = new Vector3(0, 0);
                        }

                        double dy = end.y - start.y;

                        Matrix inverse = Matrix.getIdentityMatrix();
                        inverse = Matrix.multiply(inverse, Matrix.getTranslationMatrix(pivot.x, pivot.y));

                        Matrix selection = Matrix.multiply(inverse, Matrix.getRotationMatrix(dy));
                        inverse = Matrix.multiply(inverse, Matrix.getRotationMatrix(-dy));

                        selection = Matrix.multiply(selection, Matrix.getTranslationMatrix(-pivot.x, -pivot.y));
                        inverse = Matrix.multiply(inverse, Matrix.getTranslationMatrix(-pivot.x, -pivot.y));

                        image.addTransformation(inverse, selection);
                }
        }
}
