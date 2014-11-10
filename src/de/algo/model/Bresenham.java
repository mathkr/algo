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

import java.util.ArrayList;
import java.util.List;

public class Bresenham {
        public static void drawLine(Vector3 beg, Vector3 end, MyImage image, int colorA, int colorB) {
                final int dx = Math.abs(beg.x - end.x);
                final int dy = Math.abs(beg.y - end.y);

                final int dxSign = beg.x < end.x ? 1 : -1;
                final int dySign = beg.y < end.y ? 1 : -1;

                int distShort, distLong, xIncShort, xIncLong, yIncShort, yIncLong;

                if (dx > dy) {
                        distShort = dy;
                        distLong  = dx;

                        xIncLong  = dxSign;
                        xIncShort = 0;
                        yIncLong  = 0;
                        yIncShort = dySign;
                } else {
                        distShort = dx;
                        distLong  = dy;

                        xIncLong  = 0;
                        xIncShort = dxSign;
                        yIncLong  = dySign;
                        yIncShort = 0;
                }

                int d = distLong / 2;
                int x = beg.x;
                int y = beg.y;

                for (int i = 0; i <= distLong; ++i) {
                        setPixel(image, x, y, Blend.blendPixel(colorA, colorB, (i * 100) / distLong));

                        x += xIncLong;
                        y += yIncLong;
                        d += distShort;

                        if (d >= distLong) {
                                d -= distLong;
                                x += xIncShort;
                                y += yIncShort;
                        }
                }
        }

        public static void drawCircle(Vector3 p, int r, MyImage image, int colorA, int colorB) {
                int y = 0;
                int x = r;
                int F = -r;

                int dy = 1;
                int dyx = -2 * r + 3;

                List<Vector3> section = new ArrayList<>();
                while (y <= x) {
                        section.add(new Vector3(x, y));

                        ++y;
                        dy += 2;
                        dyx += 2;

                        if (F > 0) {
                                F += dyx;
                                --x;
                                dyx += 2;
                        } else {
                                F += dy;
                        }
                }
                setCirclePixels(image, p, section, colorA, colorB);
        }

        public static void drawFilledCircle(Vector3 p, int r, MyImage image, int colorA, int colorB) {
                int y = 0;
                int x = r;
                int F = -r;

                int dy = 1;
                int dyx = -2 * r + 3;

                while (y <= x) {
                        setFilledCirclePixels(image, p, x, y, colorA, colorB, r);

                        ++y;
                        dy += 2;
                        dyx += 2;

                        if (F > 0) {
                                F += dyx;
                                --x;
                                dyx += 2;
                        } else {
                                F += dy;
                        }
                }
        }

        private static void setCirclePixels(MyImage image, Vector3 p, List<Vector3> section, int colorA, int colorB) {
                int i = 0;

                for (int j = 0; j < section.size(); ++j, ++i) {
                        Vector3 v = section.get(i);
                        int percent = (i * 100) / section.size();
                        int color = Blend.blendPixel(colorA, colorB, percent);

                        setPixel(image, p.x + v.x, p.y + v.y, color);
                        setPixel(image, p.x - v.x, p.y + v.y, color);
                        setPixel(image, p.x + v.x, p.y - v.y, color);
                        setPixel(image, p.x - v.x, p.y - v.y, color);

                        setPixel(image, p.x + v.y, p.y + v.x, color);
                        setPixel(image, p.x - v.y, p.y + v.x, color);
                        setPixel(image, p.x + v.y, p.y - v.x, color);
                        setPixel(image, p.x - v.y, p.y - v.x, color);
                }
        }

        private static void setPixel(MyImage image, int x, int y, int color) {
                if (image.isInBounds(x, y)) {
                        image.shapesData[image.coordsToDataIndex(x, y)] = color;
                }
        }

        private static void setFilledCirclePixels(MyImage image, Vector3 p, int x, int y, int colorA, int colorB, int radius) {
                for (int i = 0; i <= x; ++i) {
                        int dist = (int) Math.sqrt(Math.pow(i, 2.0) + Math.pow(y, 2.0));
                        int color = Blend.blendPixel(colorA, colorB, (dist * 100) / radius);

                        setPixel(image, p.x +  i, p.y + y, color);
                        setPixel(image, p.x -  i, p.y + y, color);

                        setPixel(image, p.x + i, p.y - y, color);
                        setPixel(image, p.x - i, p.y - y, color);
                }

                for (int i = 0; i <= y; ++i) {
                        int dist = (int) Math.sqrt(Math.pow(i, 2.0) + Math.pow(x, 2.0));
                        int color = Blend.blendPixel(colorA, colorB, (dist * 100) / radius);

                        setPixel(image, p.x + i, p.y + x, color);
                        setPixel(image, p.x - i, p.y + x, color);

                        setPixel(image, p.x + i, p.y - x, color);
                        setPixel(image, p.x - i, p.y - x, color);
                }
        }
}
