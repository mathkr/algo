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

package de.algo.model;

public class Blend {
        public static int blendPixel(int col1, int col2, int percent) {
                int a = blendComponent((col1 >> 24) & 0xFF, (col2 >> 24) & 0xFF, percent);
                int r = blendComponent((col1 >> 16) & 0xFF, (col2 >> 16) & 0xFF, percent);
                int g = blendComponent((col1 >> 8)  & 0xFF, (col2 >> 8)  & 0xFF, percent);
                int b = blendComponent( col1        & 0xFF,  col2        & 0xFF, percent);

                return a << 24 | r << 16 | g << 8 | b;
        }


        public static int blendComponent(int a, int b, int transitionPercent) {
                return a + (b - a) * transitionPercent / 100;
        }
}
