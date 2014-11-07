package de.algo.model;

import java.util.List;

public class Bresenham {
        public static void drawLine(Vector3 beg, Vector3 end, List<Pixel> points) {
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
                        points.add(new Pixel(x, y, 0));

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
}
