package de.algo.view;

import javax.swing.*;
import java.awt.*;

public class GalleryTilePanel extends JPanel {
        public String identifier;
        private Image image;

        private double aspectRatio;

        public GalleryTilePanel(String identifier, Image image) {
                super();

                this.identifier = identifier;
                this.image = image;
                aspectRatio = (double) image.getWidth(this) / (double) image.getHeight(this);
        }

        @Override
        public Dimension getPreferredSize() {
                int width = getParent().getWidth();
                int height = (int)(width / aspectRatio);
                return new Dimension(width, height);
        }

        @Override
        public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
}
