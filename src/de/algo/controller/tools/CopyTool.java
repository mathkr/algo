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

package de.algo.controller.tools;

import de.algo.model.MyImage;
import de.algo.view.CanvasPanel;
import de.algo.view.InfoBar;
import de.algo.view.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class CopyTool implements ActionListener {
        private static BufferedImage clipboard;

        public static BufferedImage getClipboard() {
                return clipboard;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
                CanvasPanel panel = View.getSelectedCanvas();
                if (panel != null) {
                        MyImage source = panel.image;

                        if (source.hasSelection()) {
                                clipboard = source.getSelectedArea();

                                String msg = String.format(
                                        "Selection copied to clipboard: %dx%d at (%d, %d)",
                                        clipboard.getWidth(),
                                        clipboard.getHeight(),
                                        source.getSelection().topL.x,
                                        source.getSelection().topL.y);

                                InfoBar.publish(msg);
                        } else {
                                InfoBar.publish("Create a selection to copy it to the clipboard.");
                        }
                }
        }
}
