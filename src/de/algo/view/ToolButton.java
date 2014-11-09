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

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;

public class ToolButton extends JButton {
        private MouseInputListener tool;
        private Color defaultBackground;
        private Cursor cursor;

        public ToolButton(MouseInputListener tool, ImageIcon icon, String tooltip, Cursor cursor) {
                super();

                this.cursor = cursor;
                this.tool = tool;
                this.defaultBackground = getBackground();

                setIcon(icon);
                setToolTipText(tooltip);
        }

        @Override
        public Dimension getPreferredSize() {
                return new Dimension(32, 32);
        }

        public MouseInputListener getTool() {
                return tool;
        }

        public Cursor getToolCursor() {
                return cursor;
        }

        public void setActive(boolean active) {
                setBackground(active ? Color.GRAY: defaultBackground);
        }
}
