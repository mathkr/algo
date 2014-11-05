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
import de.algo.model.Selection;
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

        public CanvasPanel(MyImage image, MyToolbar toolbar) {
                this.image = image;

                float[] dash = { 5f, 5f };
                selectionStroke = new BasicStroke(1f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1f, dash, 0f);

                image.addObserver(this);

                addMouseMotionListener(new MouseAdapter() {
                        @Override
                        public void mouseDragged(MouseEvent e) {
                                toolbar.getMouseListener().mouseDragged(e);
                        }

                        @Override
                        public void mouseMoved(MouseEvent e) {
                                toolbar.getMouseListener().mouseMoved(e);
                        }
                });

                addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                                toolbar.getMouseListener().mouseClicked(e);
                        }

                        @Override
                        public void mousePressed(MouseEvent e) {
                                toolbar.getMouseListener().mousePressed(e);
                        }

                        @Override
                        public void mouseReleased(MouseEvent e) {
                                toolbar.getMouseListener().mouseReleased(e);
                        }

                        @Override
                        public void mouseEntered(MouseEvent e) {
                                toolbar.getMouseListener().mouseEntered(e);
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                                toolbar.getMouseListener().mouseExited(e);
                        }
                });
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
                g.drawImage(image.getBufferedImage(), 0, 0, this);

                if (image.hasSelection()) {
                        paintSelection(g, image.getSelection());
                }
        }

        private void paintSelection(Graphics g, Selection s) {
                Graphics2D g2d = (Graphics2D)g;
                g2d.setStroke(selectionStroke);
                g2d.setXORMode(Color.LIGHT_GRAY);
                g2d.drawRect(s.topL.x, s.topL.y, s.botR.x - s.topL.x, s.botR.y - s.topL.y);
        }
}
