package de.algo.view.tools;

import de.algo.model.Matrix;
import de.algo.model.MyImage;
import de.algo.model.Vector3;
import de.algo.view.CanvasPanel;

import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseEvent;

public class ScalingTool extends MouseInputAdapter {
        public Vector3 start;

        @Override
        public void mousePressed(MouseEvent e) {
                start = new Vector3(e.getX(), e.getY(), 1);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
                scale(new Vector3(e.getX(), e.getY()), ((CanvasPanel)e.getComponent()).image);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
                scale(new Vector3(e.getX(), e.getY()), ((CanvasPanel)e.getComponent()).image);
                start = new Vector3(e.getX(), e.getY());
        }

        private void scale(Vector3 end, MyImage image) {
                if (start != null) {
                        Vector3 pivot;

                        if (image.hasPivot()) {
                                pivot = image.getPivot();
                        } else if (image.hasSelection()) {
                                pivot = image.getSelection().getCenter();
                        } else {
                                pivot = new Vector3(0, 0);
                        }

                        double dxStart = start.x - pivot.x;
                        double dyStart = start.y - pivot.y;

                        double dxEnd = end.x - pivot.x;
                        double dyEnd = end.y - pivot.y;

                        double scaleX;
                        double scaleY;

                        if (dxEnd == 0) {
                                scaleX = 1.0 / dxStart;
                        } else if (dxStart == 0) {
                                scaleX = dxEnd / 1.0;
                        } else {
                                scaleX = dxEnd / dxStart;
                        }

                        if (dyEnd == 0) {
                                scaleY = 1.0 / dyStart;
                        } else if (dyStart == 0) {
                                scaleY = dyEnd / 1.0;
                        } else {
                                scaleY = dyEnd / dyStart;
                        }

                        Matrix inverse = Matrix.getIdentityMatrix();
                        Matrix regular = Matrix.getIdentityMatrix();

                        inverse = Matrix.multiply(inverse, Matrix.getTranslationMatrix(pivot.x, pivot.y));
                        regular = Matrix.multiply(regular, Matrix.getTranslationMatrix(pivot.x, pivot.y));

                        inverse = Matrix.multiply(inverse, Matrix.getScalingMatrix(1 / scaleX, 1 / scaleY));
                        regular = Matrix.multiply(regular, Matrix.getScalingMatrix(scaleX, scaleY));

                        inverse = Matrix.multiply(inverse, Matrix.getTranslationMatrix(-pivot.x, -pivot.y));
                        regular = Matrix.multiply(regular, Matrix.getTranslationMatrix(-pivot.x, -pivot.y));

                        image.addTransformation(inverse, regular);
                }
        }
}
