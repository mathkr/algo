package de.algo.view;

import de.algo.model.MyImage;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class SlideshowPanel extends JPanel {
        private List<MyImage> images;
        private List<MyImage> scaledImages;

        private int showTime;
        private int transitionTime;
        private int transitionTimePassed;
        private int transitionPercent;

        private int currentImage;

        private Timer showTimer;
        private Timer transitionTimer;

        private MyImage buffer;

        public SlideshowPanel(List<MyImage> images, int showTime, int transitionTime, int width, int height) {
                this.images = images;
                this.scaledImages = new ArrayList<>();
                this.showTime = showTime;
                this.transitionTime = transitionTime;

                BufferedImage initial = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                View.drawImageCentered(
                        images.get(0).getBufferedImage(),
                        initial.getGraphics(),
                        width,
                        height,
                        images.get(0).getBufferedImage().getWidth(),
                        images.get(0).getBufferedImage().getHeight()
                );
                buffer = new MyImage("", initial);

                setBackground(Color.BLACK);
                setForeground(Color.WHITE);

                transitionPercent = 0;
                transitionTimePassed = 0;

                currentImage = 0;

                showTimer = new Timer(showTime, e -> {
                        showTimer.stop();
                        transitionTimer.start();
                });

                final int stepTime = 20;
                transitionTimer = new Timer(stepTime, e -> {
                        transitionTimePassed += stepTime;
                        transitionPercent = (100 * transitionTimePassed) / transitionTime;
                        blendImages(transitionPercent);

                        if (transitionTimePassed >= transitionTime) {
                                transitionTimer.stop();
                                transitionPercent = 0;
                                transitionTimePassed = 0;

                                ++currentImage;
                                currentImage %= images.size();

                                showTimer.start();
                        }
                        repaint();
                });
                transitionTimer.setRepeats(true);
        }

        private void blendImages(int transitionPercent) {
                MyImage curr = scaledImages.get(currentImage);
                MyImage next = scaledImages.get((currentImage + 1) >= scaledImages.size() ? 0 : currentImage + 1);
                for (int i = 0; i < buffer.data.length; ++i) {
                        int r = blendSingle((curr.data[i] >> 16) & 0xFF, (next.data[i] >> 16) & 0xFF, transitionPercent);
                        int g = blendSingle((curr.data[i] >> 8 ) & 0xFF, (next.data[i] >> 8 ) & 0xFF, transitionPercent);
                        int b = blendSingle( curr.data[i]        & 0xFF,  next.data[i]        & 0xFF, transitionPercent);
                        buffer.data[i] = 0xFF << 24 | r << 16 | g << 8 | b;
                }
        }

        private int blendSingle(int a, int b, int transitionPercent) {
                return a + (b - a) * transitionPercent / 100;
        }

        private void scaleImages() {
                for (int i = 0; i < images.size(); ++i) {
                        BufferedImage scaled = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
                        Graphics g = scaled.getGraphics();
                        g.setColor(Color.BLACK);
                        g.fillRect(0, 0, getWidth(), getHeight());
                        BufferedImage original = images.get(i).getBufferedImage();
                        View.drawImageCentered(original, g, getWidth(), getHeight(), original.getWidth(), original.getHeight());
                        scaledImages.add(new MyImage("slide" + i, scaled));
                }
        }

        public void start() {
                scaleImages();
                showTimer.start();
                repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                g.drawImage(buffer.getBufferedImage(), 0, 0, this);

                g.drawString("showTime            : " + showTime, 10, 30);
                g.drawString("transitionTime      : " + transitionTime, 10, 50);
                g.drawString("transitionTimePassed: " + transitionTimePassed, 10, 110);
                g.drawString("transitionPercent   : " + transitionPercent, 10, 130);
                g.drawString("currentImage        : " + currentImage, 10, 150);
        }
}
