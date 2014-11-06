package de.algo.view.tools;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;

public class ToolButton extends JButton {
        private MouseInputListener tool;
        private Color defaultBackground;

        public ToolButton(MouseInputListener tool, ImageIcon icon, String tooltip) {
                super();

                this.tool = tool;
                this.defaultBackground = getBackground();

                setIcon(icon);
                setToolTipText(tooltip);
        }

        @Override
        public Dimension getPreferredSize() {
                return new Dimension(32, 32);
        }

        public MouseInputListener getTool() {
                return tool;
        }

        public void setActive(boolean active) {
                setBackground(active ? Color.GRAY: defaultBackground);
        }
}
