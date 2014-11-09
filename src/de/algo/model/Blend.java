package de.algo.model;

public class Blend {
        public static int blendPixel(int col1, int col2, int percent) {
                int r = blendComponent((col1 >> 16) & 0xFF, (col2 >> 16) & 0xFF, percent);
                int g = blendComponent((col1 >> 8)  & 0xFF, (col2 >> 8)  & 0xFF, percent);
                int b = blendComponent( col1        & 0xFF,  col2        & 0xFF, percent);

                return 0xFF << 24 | r << 16 | g << 8 | b;
        }


        public static int blendComponent(int a, int b, int transitionPercent) {
                return a + (b - a) * transitionPercent / 100;
        }
}
