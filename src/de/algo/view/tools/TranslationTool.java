package de.algo.view.tools;

import de.algo.model.Matrix;
import de.algo.model.MyImage;
import de.algo.model.Vector3;
import de.algo.view.CanvasPanel;

import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseEvent;

public class TranslationTool extends MouseInputAdapter {
        public Vector3 start;

        @Override
        public void mousePressed(MouseEvent e) {
                start = new Vector3(e.getX(), e.getY(), 1);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
                if (start != null) {
                        Vector3 translation = Vector3.subtract(new Vector3(e.getX(), e.getY()), start);
                        translate(translation, ((CanvasPanel) e.getComponent()).image);
                        start = null;
                }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
                if (start != null) {
                        Vector3 translation = Vector3.subtract(new Vector3(e.getX(), e.getY()), start);
                        translate(translation, ((CanvasPanel) e.getComponent()).image);
                        start = new Vector3(e.getX(), e.getY());
                }
        }

        public void translate(Vector3 p, MyImage image) {
                Matrix inverse = Matrix.getTranslationMatrix(-p.x, -p.y);
                Matrix selection = Matrix.getTranslationMatrix(p.x, p.y);
                image.addTransformation(inverse, selection);
        }
}
