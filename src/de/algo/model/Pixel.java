package de.algo.model;

public class Pixel extends Vector3 {
        public int pixel;

        public Pixel(Vector3 vector, int pixel) {
                super(vector);
                this.pixel = pixel;
        }

        public Pixel(int x, int y, int pixel) {
                super(x, y);
                this.pixel = pixel;
        }
}
