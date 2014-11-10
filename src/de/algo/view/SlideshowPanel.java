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

package de.algo.view;

import de.algo.model.Blend;
import de.algo.model.MyImage;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.List;

public class SlideshowPanel extends JPanel {
        private List<MyImage> images;
        private List<BufferedImage> scaledImages;
        private List<int[]> scaledImageDates;

        private int transitionTimePassed;
        private int transitionPercent;

        private int currentImage;

        private Timer showTimer;
        private Timer transitionTimer;
        private long lastStepTime;

        private BufferedImage buffer;
        private int[] bufferData;

        public SlideshowPanel(List<MyImage> images, int showTime, int transitionTime, int width, int height) {
                this.images = images;

                this.scaledImages = new ArrayList<>();
                this.scaledImageDates = new ArrayList<>();

                scaleImages(width, height);

                buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                bufferData = getData(buffer);
                System.arraycopy(scaledImageDates.get(0), 0, bufferData, 0, width * height);

                setBackground(Color.BLACK);
                setForeground(Color.WHITE);

                transitionPercent = 0;
                transitionTimePassed = 0;

                currentImage = 0;

                showTimer = new Timer(showTime, e -> {
                        showTimer.stop();
                        lastStepTime = System.currentTimeMillis();
                        transitionTimer.start();
                });

                final int waitTime = 20;
                transitionTimer = new Timer(waitTime, e -> {
                        transitionTimePassed += (int)(System.currentTimeMillis() - lastStepTime);
                        lastStepTime = System.currentTimeMillis();
                        transitionPercent = (100 * transitionTimePassed) / transitionTime;

                        blendImages(transitionPercent > 100 ? 100 : transitionPercent);

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

        private int[] getData(BufferedImage image) {
                return ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
        }

        private void blendImages(int transitionPercent) {
                int[] curr = scaledImageDates.get(currentImage);
                int[] next = scaledImageDates
                        .get((currentImage + 1) >= scaledImageDates.size() ? 0 : currentImage + 1);

                for (int i = 0; i < getData(buffer).length; ++i) {
                        bufferData[i] = Blend.blendPixel(curr[i], next[i], transitionPercent);
                }
        }

        private void scaleImages(int width, int height) {
                for (int i = 0; i < images.size(); ++i) {
                        BufferedImage scaled = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                        Graphics g = scaled.getGraphics();
                        g.setColor(Color.BLACK);
                        g.fillRect(0, 0, width, height);

                        BufferedImage original = images.get(i).getBufferedImage();

                        View.drawImageCentered(original, g, width, height, original.getWidth(), original.getHeight());

                        scaledImages.add(scaled);
                        scaledImageDates.add(getData(scaled));
                }
        }

        public void start() {
                showTimer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                g.drawImage(buffer, 0, 0, this);

                /*
                g.drawString("showTime            : " + showTime, 10, 30);
                g.drawString("transitionTime      : " + transitionTime, 10, 50);
                g.drawString("transitionTimePassed: " + transitionTimePassed, 10, 110);
                g.drawString("transitionPercent   : " + transitionPercent, 10, 130);
                g.drawString("currentImage        : " + currentImage, 10, 150);
                */
        }

        public void stop() {
                showTimer.stop();
                showTimer = null;
                transitionTimer.stop();
                transitionTimer = null;
        }
}
