package de.algo.view.tools;

import de.algo.model.Vector3;
import de.algo.view.CanvasPanel;

import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseEvent;

public class PivotTool extends MouseInputAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
                Vector3 pivot = new Vector3(e.getX(), e.getY(), 1);
                ((CanvasPanel)e.getComponent()).image.setPivot(pivot);
        }
}
