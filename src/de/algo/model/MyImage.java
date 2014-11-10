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
import java.util.*;

public class MyImage extends Observable {
        public final String IDENTIFIER;

        private BufferedImage originalImage;
        public int[] originalData;

        public BufferedImage transformedImage;
        public int[] transformedData;

        private BufferedImage modifiedImage;
        public int[] modifiedData;

        public BufferedImage shapesImage;
        public int[] shapesData;

        public BufferedImage pastedImage;
        public int[] pastedData;
        private Matrix pastedMatrix;

        private Matrix inverseMatrix;
        private Matrix transformationMatrix;

        private Selection selection;
        private Vector3 pivot;

        public MyImage(String identifier, Image source) {
                this.IDENTIFIER = identifier;

                if (source instanceof BufferedImage
                        && ((BufferedImage)source).getType() == BufferedImage.TYPE_INT_ARGB) {

                        originalImage = (BufferedImage)source;

                        ColorModel colorModel = originalImage.getColorModel();
                        boolean isPreMultiplied = originalImage.isAlphaPremultiplied();
                        WritableRaster raster = originalImage.copyData(null);
                        modifiedImage = new BufferedImage(colorModel, raster, isPreMultiplied, null);
                } else {
                        ImageObserver io = (img, infoflags, x, y, width, height) -> false;

                        originalImage = new BufferedImage(
                                source.getWidth(io),
                                source.getHeight(io),
                                BufferedImage.TYPE_INT_ARGB);

                        modifiedImage = new BufferedImage(
                                source.getWidth(io),
                                source.getHeight(io),
                                BufferedImage.TYPE_INT_ARGB);

                        originalImage.getGraphics().drawImage(source, 0, 0, io);
                        modifiedImage.getGraphics().drawImage(source, 0, 0, io);
                }

                transformedImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
                shapesImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_ARGB);

                originalData = ((DataBufferInt) originalImage.getRaster().getDataBuffer()).getData();
                modifiedData = ((DataBufferInt)modifiedImage.getRaster().getDataBuffer()).getData();
                transformedData = ((DataBufferInt)transformedImage.getRaster().getDataBuffer()).getData();
                shapesData = ((DataBufferInt)shapesImage.getRaster().getDataBuffer()).getData();

                inverseMatrix = Matrix.getIdentityMatrix();
                transformationMatrix = Matrix.getIdentityMatrix();

                createTransformedImage();
        }

        public void resetModifiedImage() {
                transformationMatrix = Matrix.getIdentityMatrix();
                inverseMatrix = Matrix.getIdentityMatrix();

                pastedImage = null;
                pastedData = null;
                pastedMatrix = Matrix.getIdentityMatrix();

                System.arraycopy(originalData, 0, modifiedData, 0, originalData.length);

                createTransformedImage();
                setChanged();
                notifyObservers();
        }

        private void writeModifications() {
                System.arraycopy(transformedData, 0, modifiedData, 0, transformedData.length);
                transformationMatrix = Matrix.getIdentityMatrix();
                inverseMatrix = Matrix.getIdentityMatrix();

                pastedImage = null;
                pastedData = null;
                pastedMatrix = Matrix.getIdentityMatrix();
        }

        public void setSelection(Vector3 a, Vector3 b) {
                writeModifications();

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
                if (selection != null) {
                        selection = null;
                        writeModifications();

                        setChanged();
                        notifyObservers();
                }
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
                if (pivot != null) {
                        pivot = null;

                        setChanged();
                        notifyObservers();
                }
        }

        public BufferedImage getBufferedImage() {
                return originalImage;
        }

        public Vector3 getImageCenter() {
                return new Vector3(originalImage.getWidth() / 2, originalImage.getHeight() / 2);
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

        public boolean isInBounds(Vector3 p) {
                return     p.x >= 0
                        && p.y >= 0
                        && p.x < originalImage.getWidth()
                        && p.y < originalImage.getHeight();
        }

        public boolean isInBounds(int x, int y) {
                return     x >= 0
                        && y >= 0
                        && x < originalImage.getWidth()
                        && y < originalImage.getHeight();
        }

        public void createTransformedImage() {
                if (pastedImage == null) {
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
                } else {
                        for (int i = 0; i < transformedData.length; ++i) {
                                Vector3 point = dataIndexToCoords(i);
                                Vector3 pastedPoint = Matrix.multiply(pastedMatrix, point);

                                if (isInPasteBounds(pastedPoint)) {
                                        transformedData[i] = pastedData[coordsToPasteDataIndex(pastedPoint)];
                                } else {
                                        transformedData[i] = modifiedData[i];
                                }
                        }
                }
        }

        private boolean isInPasteBounds(Vector3 p) {
                return     p.x >= 0
                        && p.y >= 0
                        && p.x < pastedImage.getWidth()
                        && p.y < pastedImage.getHeight();
        }

        private int coordsToPasteDataIndex(Vector3 p) {
                return p.y * pastedImage.getWidth() + p.x;
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

        public Vector3 dataIndexToCoords(int index) {
                int x = index % originalImage.getWidth();
                int y = index / originalImage.getWidth();

                return new Vector3(x, y);
        }

        public int coordsToDataIndex(Vector3 p) {
                return p.y * originalImage.getWidth() + p.x;
        }

        public int coordsToDataIndex(int x, int y) {
                return y * originalImage.getWidth() + x;
        }

        public void addTransformation(Matrix inverse, Matrix transformation) {
                this.inverseMatrix = Matrix.multiply(this.inverseMatrix, inverse);
                this.transformationMatrix = Matrix.multiply(transformation, this.transformationMatrix);

                if (pastedImage != null) {
                        this.pastedMatrix = Matrix.multiply(this.pastedMatrix, inverse);
                }

                createTransformedImage();
                setChanged();
                notifyObservers();
        }

        public BufferedImage getSelectedArea() {
                BufferedImage selected = modifiedImage.getSubimage(
                        selection.topL.x,
                        selection.topL.y,
                        selection.getWidth(),
                        selection.getHeight());

                BufferedImage res = new BufferedImage(selected.getWidth(), selected.getHeight(), BufferedImage.TYPE_INT_ARGB);
                res.getGraphics().drawImage(selected, 0, 0, (img, infoflags, x, y, width, height) -> false);

                return res;
        }

        public Selection getSelection() {
                return selection;
        }

        public void paste(BufferedImage source, int w, int h) {
                if (selection != null) {
                        writeModifications();
                } else {
                        setSelection(new Vector3(0, 0), new Vector3(w, h));
                }

                pastedImage = source;
                pastedData = ((DataBufferInt) pastedImage.getRaster().getDataBuffer()).getData();

                double factorX = (double) selection.getWidth() / (double) pastedImage.getWidth();
                double factorY = (double) selection.getHeight() / (double) pastedImage.getHeight();

                Matrix inverse = Matrix.getIdentityMatrix();
                inverse = Matrix.multiply(inverse, Matrix.getScalingMatrix(1.0 / factorX, 1.0 / factorY));
                inverse = Matrix.multiply(inverse, Matrix.getTranslationMatrix(-selection.topL.x, -selection.topL.y));

                pastedMatrix = inverse;
                createTransformedImage();

                setChanged();
                notifyObservers();
        }

        public void applyShapes(boolean temporary) {
                if (!temporary) {
                        writeModifications();
                        modifiedImage.getGraphics()
                                .drawImage(shapesImage, 0, 0, (img, infoflags, x, y, width, height) -> false);
                        createTransformedImage();
                        clearShapes();
                }

                setChanged();
                notifyObservers();
        }

        public void clearShapes() {
                for (int i = 0; i < shapesData.length; ++i) {
                        shapesData[i] = 0;
                }
        }
}
