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

package de.algo.util;

public class Logger {
        public final static int DEBUG = 3;
        public final static int INFO = 2;
        public final static int WARNING = 1;
        public final static int ERROR = 0;

        private static int logLevel = DEBUG;

        public static void setMaximumLogLevel(int level) {
                logLevel = level;
        }

        public static void log(int level, String msg) {
                if (level <= logLevel) {
                        System.out.println(msg);
                }
        }
}
