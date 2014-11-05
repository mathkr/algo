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
                ((CanvasPanel)e.getComponent()).image.removeSelection();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
                end = new Vector3(e.getX(), e.getY(), 1);

                ((CanvasPanel)e.getComponent()).image.setSelection(start, end);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
                end = new Vector3(e.getX(), e.getY(), 1);

                ((CanvasPanel)e.getComponent()).image.setSelection(start, end);
        }
}
