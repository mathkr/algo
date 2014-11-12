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

public class Vector3 {
        public int x, y, z;

        public Vector3() {
                this(0, 0, 1);
        }

        public Vector3(int x, int y) {
                this(x, y, 1);
        }

        public Vector3(int x, int y, int z) {
                this.x = x;
                this.y = y;
                this.z = z;
        }

        public Vector3(Vector3 vector) {
                this.x = vector.x;
                this.y = vector.y;
                this.z = vector.z;
        }

        public static Vector3 subtract(Vector3 v1, Vector3 v2) {
                Vector3 res = new Vector3();

                res.x = v1.x - v2.x;
                res.y = v1.y - v2.y;
                res.z = v1.z - v2.z;

                return res;
        }

        public int get(int index) {
                switch (index) {
                        case 0:
                                return x;
                        case 1:
                                return y;
                        case 2:
                                return z;
                        default:
                                throw new IndexOutOfBoundsException("invalid index: " + index);
                }
        }

        public static double getDistance(Vector3 v1, Vector3 v2) {
                Vector3 d = subtract(v1, v2);
                return Math.sqrt(Math.pow(d.x, 2) + Math.pow(d.y, 2) + Math.pow(d.z, 2));
        }

        @Override
        public String toString() {
                return String.format("%5d\n%5d\n%5d\n", x, y, z);
        }

        @Override
        public boolean equals(Object obj) {
                if (obj instanceof Vector3) {
                        Vector3 vec = (Vector3) obj;
                        return     vec.x == x
                                && vec.y == y
                                && vec.z == z;
                }

                return false;
        }

        @Override
        public int hashCode() {
                return (z & 0x3FF) << 22 | (y & 0x3FF) << 12 | x & 0xFFF;
        }
}
