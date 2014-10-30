package de.algo.model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;

public class MyImage {
        private String identifier;
        private BufferedImage image;
        private int[] data;

        public MyImage(String identifier, Image source, ImageObserver io) {
                this.identifier = identifier;

                image = new BufferedImage(
                        source.getWidth(io),
                        source.getHeight(io),
                        BufferedImage.TYPE_INT_ARGB);

                image.getGraphics().drawImage(source, 0, 0, io);
                data = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
        }
}
