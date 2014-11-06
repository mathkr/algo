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
import de.algo.model.Vector3;
import de.algo.view.tools.MyToolbar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

public class CanvasPanel extends JPanel implements Observer {
        public MyImage image;
        private Stroke selectionStroke;

        private int xTranslation, yTranslation;

        public CanvasPanel(MyImage image, MyToolbar toolbar) {
                this.image = image;

                float[] dash = { 5f, 5f };
                setBackground(Color.DARK_GRAY);
                selectionStroke = new BasicStroke(1f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1f, dash, 0f);

                image.addObserver(this);

                addMouseMotionListener(new MouseAdapter() {
                        @Override
                        public void mouseDragged(MouseEvent e) {
                                toolbar.getMouseListener().mouseDragged(translateMouseEvent(e));
                        }

                        @Override
                        public void mouseMoved(MouseEvent e) {
                                toolbar.getMouseListener().mouseMoved(translateMouseEvent(e));
                        }
                });

                addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                                toolbar.getMouseListener().mouseClicked(translateMouseEvent(e));
                        }

                        @Override
                        public void mousePressed(MouseEvent e) {
                                toolbar.getMouseListener().mousePressed(translateMouseEvent(e));
                        }

                        @Override
                        public void mouseReleased(MouseEvent e) {
                                toolbar.getMouseListener().mouseReleased(translateMouseEvent(e));
                        }

                        @Override
                        public void mouseEntered(MouseEvent e) {
                                toolbar.getMouseListener().mouseEntered(translateMouseEvent(e));
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                                toolbar.getMouseListener().mouseExited(translateMouseEvent(e));
                        }
                });
        }

        private MouseEvent translateMouseEvent(MouseEvent e) {
                return new MouseEvent(
                        (Component)e.getSource(),
                        e.getID(),
                        e.getWhen(),
                        e.getModifiers(),
                        e.getX() - xTranslation,
                        e.getY() - yTranslation,
                        e.getClickCount(),
                        e.isPopupTrigger());
        }

        private void setTranslation(Graphics g) {
                if (image.getBufferedImage().getWidth() < getWidth()) {
                        xTranslation = (getWidth() - image.getBufferedImage().getWidth()) / 2;
                }

                if (image.getBufferedImage().getHeight() < getHeight()) {
                        yTranslation = (getHeight() - image.getBufferedImage().getHeight()) / 2;
                }
        }

        @Override
        public Dimension getPreferredSize() {
                return new Dimension(image.getBufferedImage().getWidth(),
                        image.getBufferedImage().getHeight());
        }

        @Override
        public void update(Observable o, Object arg) {
                repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                setTranslation(g);
                g.translate(xTranslation, yTranslation);

                g.drawImage(image.getTransformedImage(), 0, 0, this);

                if (image.hasSelection()) {
                        paintSelection(g, image.getTransformedSelection());
                }

                if (image.hasPivot()) {
                        paintPivot(g, image.getPivot());
                }
        }

        private void paintPivot(Graphics g, Vector3 pivot) {
                Graphics2D g2d = (Graphics2D)g;
                //g2d.setXORMode(Color.LIGHT_GRAY);
                g2d.setColor(Color.RED);
                g2d.setStroke(new BasicStroke(2f));

                int w = 18;
                g.drawLine(pivot.x - w / 2, pivot.y, pivot.x + w / 2, pivot.y);
                g.drawLine(pivot.x, pivot.y - w / 2, pivot.x, pivot.y + w / 2);
                g.drawOval(pivot.x - w / 2, pivot.y - w / 2, w, w);
        }

        private void paintSelection(Graphics g, int[][] selection) {
                Graphics2D g2d = (Graphics2D)g;
                g2d.setStroke(selectionStroke);
                //g2d.setXORMode(Color.LIGHT_GRAY);
                g2d.setColor(Color.RED);
                g2d.drawPolygon(selection[0], selection[1], 4);
        }
}
