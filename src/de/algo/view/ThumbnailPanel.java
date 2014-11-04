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
import java.awt.image.BufferedImage;

public class ThumbnailPanel extends JPanel {
        public MyImage image;

        private boolean selected;

        public ThumbnailPanel(int size, MyImage image) {
                super();

                selected = false;

                int iWidth = image.getBufferedImage().getWidth(this);
                int iHeight = image.getBufferedImage().getHeight(this);

                BufferedImage buffer = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
                View.drawImageCentered(image.getBufferedImage(), buffer.getGraphics(),
                        size, size, iWidth, iHeight);
                this.image = new MyImage(image.IDENTIFIER, buffer);

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

                g.drawImage(image.getBufferedImage(), 0, 0, this);

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
