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

import de.algo.view.CanvasPanel;
import de.algo.view.InfoBar;
import de.algo.view.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class PasteTool implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
                BufferedImage clipboard = CopyTool.getClipboard();

                CanvasPanel panel = View.getSelectedCanvas();

                if (panel != null) {
                        if (clipboard == null) {
                                InfoBar.publish("Clipboard is empty");
                                return;
                        }

                        panel.image.paste(clipboard, clipboard.getWidth(), clipboard.getHeight());
                        InfoBar.publish("Pasted from clipboard");
                }
        }
}
