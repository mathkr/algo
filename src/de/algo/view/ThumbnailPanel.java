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

import de.algo.model.MyImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ThumbnailPanel extends JPanel {
        public MyImage image;

        private double aspectRatio;
        private int iWidth;
        private int iHeight;
        private boolean selected;

        public ThumbnailPanel(int size, MyImage image) {
                super();

                this.image = image;
                this.selected = false;

                iWidth = image.getBufferedImage().getWidth(this);
                iHeight = image.getBufferedImage().getHeight(this);
                aspectRatio = (double) iWidth / (double) iHeight;

                setBackground(Color.DARK_GRAY);
                setForeground(Color.WHITE);
                setPreferredSize(new Dimension(size, size));

                addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                                Component parent = getParent();
                                if (parent != null) {
                                        parent.dispatchEvent(e);
                                }
                        }
                });
        }

        @Override
        public void paintComponent(Graphics g) {
                super.paintComponent(g);

                /*
                if (aspectRatio >= 1.0) {
                        int w = getWidth();
                        int h = (int)(w / aspectRatio);
                        g.drawImage(image.getBufferedImage(), 0, (getHeight() - h) / 2, w, h, this);
                } else {
                        int h = getHeight();
                        int w = (int)(h * aspectRatio);
                        g.drawImage(image.getBufferedImage(), (getWidth() - w) / 2, 0, w, h, this);
                }*/
                View.drawImageCentered(image.getBufferedImage(), g, getWidth(), getHeight(), iWidth, iHeight);

                if (selected) {
                        Graphics2D g2d = (Graphics2D)g;
                        g2d.setStroke(new BasicStroke(10));
                        g2d.setColor(Color.BLUE);
                        g.drawRect(0, 0, getWidth(), getHeight());
                }
        }

        public void setSelected(boolean selected) {
                this.selected = selected;
        }

        public boolean isSelected() {
                return selected;
        }
}
