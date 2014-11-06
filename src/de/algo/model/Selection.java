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

import java.awt.*;

public class Selection {
        public Vector3 topL;
        public Vector3 botR;

        public Selection(Vector3 topL, Vector3 botR) {
                this.topL = topL;
                this.botR = botR;
        }

        public boolean contains(Vector3 p) {
                return     p.x >= topL.x && p.x <= botR.x
                        && p.y >= topL.y && p.y <= botR.y;
        }

        public void setSelection(Vector3 topL, Vector3 botR) {
                this.topL = topL;
                this.botR = botR;
        }

        public Vector3 getCenter() {
                int x = topL.x + (botR.x - topL.x) / 2;
                int y = topL.y + (botR.y - topL.y) / 2;

                return new Vector3(x, y);
        }
}
