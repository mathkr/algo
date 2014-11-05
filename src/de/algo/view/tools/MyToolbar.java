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

package de.algo.view.tools;

import de.algo.view.View;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import java.awt.event.ActionListener;

public class MyToolbar extends JToolBar {
        private ToolButton selectedTool;

        public MyToolbar() {
                super(JToolBar.VERTICAL);

                ActionListener select = e -> {
                        ToolButton newButton =((ToolButton)e.getSource());
                        if (selectedTool != null) {
                                selectedTool.setActive(false);
                        }
                        newButton.setActive(true);
                        selectedTool = newButton;
                };

                SelectionTool selectionTool = new SelectionTool();
                ToolButton selectionButton = new ToolButton(
                        selectionTool,
                        View.getIcon("checkbox_unchecked_icon&24"),
                        "Selection tool");
                selectionButton.addActionListener(select);
                add(selectionButton);

                MouseInputAdapter cursorTool = new MouseInputAdapter() {};
                ToolButton cursorButton = new ToolButton(
                        cursorTool,
                        View.getIcon("cursor_arrow_icon&24"),
                        "Cursor tool");
                cursorButton.addActionListener(select);
                add(cursorButton);

                selectedTool = cursorButton;
                selectedTool.setActive(true);
        }

        public MouseInputListener getMouseListener() {
                return selectedTool.getTool();
        }
}
