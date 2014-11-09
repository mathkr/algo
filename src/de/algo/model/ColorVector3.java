package de.algo.model;

public class ColorVector3 extends Vector3 {
        public int pixel;

        public ColorVector3(Vector3 vector, int pixel) {
                super(vector);
                this.pixel = pixel;
        }

        public ColorVector3(int x, int y, int pixel) {
                super(x, y);
                this.pixel = pixel;
        }
}
