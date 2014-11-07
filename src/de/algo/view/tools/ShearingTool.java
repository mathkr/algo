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

public class ShearingTool extends MouseInputAdapter {
        public static final double FACTOR = 200.0;

        public Vector3 start;

        private boolean xAxis;

        public ShearingTool(boolean xAxis) {
                this.xAxis = xAxis;
        }

        @Override
        public void mousePressed(MouseEvent e) {
                start = new Vector3(e.getX(), e.getY(), 1);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
                shear(new Vector3(e.getX(), e.getY()), ((CanvasPanel) e.getComponent()).image);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
                shear(new Vector3(e.getX(), e.getY()), ((CanvasPanel) e.getComponent()).image);
                start = new Vector3(e.getX(), e.getY());
        }

        private void shear(Vector3 end, MyImage image) {
                if (start != null) {
                        Vector3 pivot;

                        if (image.hasPivot()) {
                                pivot = image.getPivot();
                        } else if (image.hasSelection()) {
                                pivot = image.getTransformedSelectionCenter();
                        } else {
                                pivot = image.getImageCenter();
                        }

                        double dx = end.x - start.x;
                        double dy = end.y - start.y;

                        int direction;

                        if (xAxis) {
                                direction = start.y <= pivot.y ? 1 : -1;
                        } else {
                                direction = start.x <= pivot.x ? 1 : -1;
                        }

                        Matrix inverse = Matrix.getIdentityMatrix();
                        Matrix regular = Matrix.getIdentityMatrix();

                        inverse = Matrix.multiply(inverse, Matrix.getTranslationMatrix(pivot.x, pivot.y));
                        regular = Matrix.multiply(regular, Matrix.getTranslationMatrix(pivot.x, pivot.y));

                        if (xAxis) {
                                inverse = Matrix.multiply(inverse, Matrix.getShearMatrix(dx / FACTOR * direction, 0));
                                regular = Matrix.multiply(regular, Matrix.getShearMatrix(-dx / FACTOR * direction, 0));
                        } else {
                                inverse = Matrix.multiply(inverse, Matrix.getShearMatrix(0, dy / FACTOR * direction));
                                regular = Matrix.multiply(regular, Matrix.getShearMatrix(0, -dy / FACTOR * direction));
                        }

                        inverse = Matrix.multiply(inverse, Matrix.getTranslationMatrix(-pivot.x, -pivot.y));
                        regular = Matrix.multiply(regular, Matrix.getTranslationMatrix(-pivot.x, -pivot.y));

                        image.addTransformation(inverse, regular);
                }
        }
}
