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
import java.awt.image.*;
import java.util.Observable;

public class MyImage extends Observable {
        public final String IDENTIFIER;

        private BufferedImage originalImage;
        public int[] originalData;

        private BufferedImage transformedImage;
        public int[] transformedData;

        private BufferedImage modifiedImage;
        public int[] modifiedData;

        private Matrix inverseMatrix;
        private Matrix transformationMatrix;

        private Selection selection;
        private Vector3 pivot;

        public MyImage(String identifier, Image source) {
                this.IDENTIFIER = identifier;

                if (source instanceof BufferedImage
                        && ((BufferedImage)source).getType() == BufferedImage.TYPE_INT_ARGB) {

                        originalImage = (BufferedImage)source;
                } else {
                        ImageObserver io = (img, infoflags, x, y, width, height) -> false;

                        originalImage = new BufferedImage(
                                source.getWidth(io),
                                source.getHeight(io),
                                BufferedImage.TYPE_INT_ARGB);

                        originalImage.getGraphics().drawImage(source, 0, 0, io);
                }

                transformedImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_ARGB);

                resetModifiedImage();

                originalData = ((DataBufferInt) originalImage.getRaster().getDataBuffer()).getData();
                transformedData = ((DataBufferInt)transformedImage.getRaster().getDataBuffer()).getData();
                modifiedData = ((DataBufferInt)modifiedImage.getRaster().getDataBuffer()).getData();

                inverseMatrix = Matrix.getIdentityMatrix();
                transformationMatrix = Matrix.getIdentityMatrix();
        }

        public void resetModifiedImage() {
                ColorModel colorModel = originalImage.getColorModel();
                boolean isPreMultiplied = originalImage.isAlphaPremultiplied();
                WritableRaster raster = originalImage.copyData(null);
                BufferedImage res = new BufferedImage(colorModel, raster, isPreMultiplied, null);
                modifiedImage = res;
        }

        public void setSelection(Vector3 a, Vector3 b) {
                Vector3 topL = new Vector3(Math.min(a.x, b.x), Math.min(a.y, b.y), 1);
                Vector3 botR = new Vector3(Math.max(a.x, b.x), Math.max(a.y, b.y), 1);
                this.selection = new Selection(topL, botR);

                setChanged();
                notifyObservers();
        }

        public boolean hasSelection() {
                return selection != null;
        }

        public void removeSelection() {
                selection = null;

                setChanged();
                notifyObservers();
        }

        public void setPivot(Vector3 pivot) {
                this.pivot = pivot;

                setChanged();
                notifyObservers();
        }

        public boolean hasPivot() {
                return pivot != null;
        }

        public Vector3 getPivot() {
                return pivot;
        }

        public void removePivot() {
                pivot = null;

                setChanged();
                notifyObservers();
        }

        public BufferedImage getBufferedImage() {
                return originalImage;
        }

        private boolean isInSelection(Vector3 p) {
                if (!isInBounds(p)) {
                        return false;
                } else if (selection == null) {
                        return true;
                } else {
                        return selection.contains(p);
                }
        }

        private boolean isInBounds(Vector3 p) {
                return     p.x >= 0
                        && p.y >= 0
                        && p.x < originalImage.getWidth()
                        && p.y < originalImage.getHeight();
        }

        public BufferedImage getTransformedImage() {
                for (int i = 0; i < transformedData.length; ++i) {
                        Vector3 point = dataIndexToCoords(i);
                        Vector3 transformedPoint = Matrix.multiply(inverseMatrix, point);

                        if (isInSelection(transformedPoint)) {
                                transformedData[i] = modifiedData[coordsToDataIndex(transformedPoint)];
                        } else if (isInSelection(point)) {
                                transformedData[i] = 0xFFFFFFFF;
                        } else {
                                transformedData[i] = modifiedData[i];
                        }
                }

                return transformedImage;
        }

        public int[][] getTransformedSelection() {
                Vector3[] points = new Vector3[4];
                int[][] res = new int[2][4];

                points[0] = Matrix.multiply(transformationMatrix, selection.topL);
                points[1] = Matrix.multiply(transformationMatrix, new Vector3(selection.botR.x, selection.topL.y));
                points[2] = Matrix.multiply(transformationMatrix, selection.botR);
                points[3] = Matrix.multiply(transformationMatrix, new Vector3(selection.topL.x, selection.botR.y));

                for (int i = 0; i < 2; ++i) {
                        for (int j = 0; j < 4; j++) {
                                res[i][j] = points[j].get(i);
                        }
                }

                return res;
        }

        public Vector3 getTransformedSelectionCenter() {
                return Matrix.multiply(transformationMatrix, selection.getCenter());
        }

        private Vector3 dataIndexToCoords(int index) {
                int x = index % originalImage.getWidth();
                int y = index / originalImage.getWidth();

                return new Vector3(x, y);
        }

        private int coordsToDataIndex(Vector3 p) {
                return p.y * originalImage.getWidth() + p.x;
        }

        public void addTransformation(Matrix inverse, Matrix transformation) {
                this.inverseMatrix = Matrix.multiply(this.inverseMatrix, inverse);
                this.transformationMatrix = Matrix.multiply(transformation, this.transformationMatrix);

                setChanged();
                notifyObservers();
        }
}
