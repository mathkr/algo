package de.algo.model;


import java.util.Vector;

public class Matrix {
        private double[][] data;

        public Matrix() {
                data = new double[3][3];
        }

        public static Matrix getIdentityMatrix() {
                Matrix res = new Matrix();

                res.data[0][0] = 1.0;
                res.data[1][1] = 1.0;
                res.data[2][2] = 1.0;

                return res;
        }

        public static Matrix getTranslationMatrix(int x, int y) {
                Matrix res = getIdentityMatrix();

                res.data[0][2] = x;
                res.data[1][2] = y;

                return res;
        }

        public static Matrix getScalingMatrix(double x, double y) {
                Matrix res = new Matrix();

                res.data[0][0] = x;
                res.data[1][1] = y;
                res.data[2][2] = 1.0;

                return res;
        }

        public static Matrix getShearMatrix(double x, double y) {
                Matrix res = getIdentityMatrix();

                res.data[0][1] = x;
                res.data[1][0] = y;

                return res;
        }

        public static Matrix getRotationMatrix(double angle) {
                double rad = Math.toRadians(angle);

                Matrix res = new Matrix();

                res.data[0][0] =  Math.cos(rad);
                res.data[0][1] = -Math.sin(rad);
                res.data[1][0] = -res.data[0][1];
                res.data[1][1] =  res.data[0][0];
                res.data[2][2] =  1.0;

                return res;
        }

        public static Matrix multiply(Matrix a, Matrix b) {
                Matrix res = new Matrix();

                for (int i = 0; i < 3; ++i) {
                        for (int j = 0; j < 3; j++) {
                                for (int k = 0; k < 3; k++) {
                                        res.data[i][j] += a.data[i][k] * b.data[k][j];
                                }
                        }
                }

                return res;
        }

        public static Vector3 multiply(Matrix m, Vector3 v) {
                double[] vec = { v.x, v.y, v.z };
                double[] res = new double[3];

                for (int i = 0; i < 3; ++i) {
                        for (int j = 0; j < 3; j++) {
                                res[i] += m.data[i][j] * vec[j];
                        }
                }

                return new Vector3(
                        (int) Math.rint(res[0]),
                        (int) Math.rint(res[1]),
                        (int) Math.rint(res[2])
                );
        }

        @Override
        public String toString() {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < 3; i++) {
                        sb.append(String.format("%10f | %10f | %10f\n", data[i][0], data[i][1], data[i][2]));
                }
                return sb.toString();
        }
}
