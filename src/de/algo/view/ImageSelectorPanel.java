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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

public class ImageSelectorPanel extends JPanel {
        public Map<String, ThumbnailPanel> tiles;
        private Map<String, ThumbnailPanel> selected;

        private boolean multiSelect;

        private int tileSize;
        private int cols;
        private int spacing;

        private ChangeListener changeListener;

        public ImageSelectorPanel(int tileSize, int cols, int spacing, boolean multiSelect) {
                this.tiles = new HashMap<>();
                this.selected = new HashMap<>();
                this.multiSelect = multiSelect;

                this.tileSize = tileSize;
                this.cols = cols;
                this.spacing = spacing;

                setLayout(new FlowLayout(FlowLayout.CENTER, spacing, spacing));
                setBackground(Color.WHITE);

                addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                                if (e.getComponent() instanceof ThumbnailPanel) {
                                        processClick((ThumbnailPanel) e.getComponent());
                                        repaint();
                                }
                        }
                });
        }

        public void addChangeListener(ChangeListener changeListener) {
                this.changeListener = changeListener;
        }

        private void processClick(ThumbnailPanel tile) {
                tile.setSelected(!tile.isSelected());

                if (tile.isSelected()) {
                        if (!multiSelect) {
                                selected.values().forEach(img -> img.setSelected(false));
                                selected.clear();
                        }
                        selected.put(tile.image.IDENTIFIER, tile);
                } else {
                        selected.remove(tile.image.IDENTIFIER);
                }

                if (changeListener != null) {
                        changeListener.stateChanged(new ChangeEvent(this));
                }
        }

        public void addTiles(Collection<MyImage> images) {
                images.forEach(image -> {
                        if (!tiles.containsKey(image.IDENTIFIER)) {
                                ThumbnailPanel tile = new ThumbnailPanel(tileSize, image);
                                tiles.put(image.IDENTIFIER, tile);
                                add(tile);
                        }
                });
                revalidate();
        }

        public void clear() {
                removeAll();
                tiles.clear();
                selected.clear();
                revalidate();
        }

        @Override
        public Dimension getPreferredSize() {
                int w = cols * tileSize + cols * spacing;
                int currentCols = getWidth() == 0 ? cols : getWidth() / (tileSize + spacing);
                int h = tiles.isEmpty()
                        ? tileSize
                        : (tileSize + spacing) * (int)Math.ceil((double)tiles.size() / currentCols) + spacing;

                return new Dimension(w, h);
        }

        public Set<String> getSelected() {
                return new HashSet<>(selected.keySet());
        }

        public void selectAll() {
                if (multiSelect) {
                        tiles.forEach(selected::put);
                        selected.forEach((s, t) -> t.setSelected(true));
                        repaint();
                }
        }
}
