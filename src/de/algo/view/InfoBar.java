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

import de.algo.util.Logger;

import javax.swing.*;
import java.awt.*;

public class InfoBar extends JPanel {
        private JLabel label;
        private ImageIcon icon;
        private int defaultShowTime;
        private Timer timer;

        private static InfoBar infoBar;

        private InfoBar(int defaultShowTime) {
                super();

                this.defaultShowTime = defaultShowTime;
                icon = View.getIcon("attention_icon&16");
                label = new JLabel(" ", JLabel.HORIZONTAL);
                timer = new Timer(defaultShowTime, e -> {
                        label.setText(" ");
                        label.setIcon(null);
                });
                add(label);
        }

        @Override
        public Dimension getPreferredSize() {
                return new Dimension(super.getPreferredSize().width, 26);
        }

        public static InfoBar getInfoBar(int defaultShowTime) {
                if (infoBar == null) {
                        infoBar = new InfoBar(defaultShowTime);
                } else {
                        infoBar.setDefaultShowTime(defaultShowTime);
                }

                return infoBar;
        }

        public void setDefaultShowTime(int defaultShowTime) {
                this.defaultShowTime = defaultShowTime;
        }

        public static void publish(String s) {
                publish(s, infoBar.defaultShowTime);
        }

        public static void publish(String s, int showTime) {
                Logger.log(Logger.DEBUG, "INFOBAR: " + s);
                infoBar.label.setText(s);
                infoBar.label.setIcon(infoBar.icon);
                infoBar.repaint();

                infoBar.timer.stop();
                infoBar.timer.setInitialDelay(showTime * 1000);
                infoBar.timer.restart();
        }
}
