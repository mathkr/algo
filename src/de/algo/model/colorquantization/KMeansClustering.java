package de.algo.model.colorquantization;

import de.algo.model.Vector3;
import de.algo.util.Logger;

import java.util.*;

public class KMeansClustering {
        public static Cluster[] generateCluster(Vector3[] points, int k) {
                /*
                 * Make sure that we don't create more clusters than points.
                 */
                k = k < points.length ? k : points.length;

                Logger.log(Logger.INFO, "Starting clustering with " + points.length + " points and " + k + " means..");

                /*
                 * Initialize k clusters with random points from the point set.
                 */
                Random random = new Random();
                Cluster[] clusters = new Cluster[k];
                Set<Integer> pickedIndices = new HashSet<>();

                for (int i = 0; i < k; ++i) {
                        int randomIndex;
                        while (pickedIndices.contains(randomIndex = random.nextInt(points.length))) {}
                        clusters[i] = new Cluster(points[randomIndex]);
                        pickedIndices.add(randomIndex);
                }

                int i = 0;
                while (!allConverging(clusters)) {
                        iterate(points, clusters);
                        ++i;
                }

                Logger.log(Logger.INFO, "Finished approximating clusters after " + i + " iterations");

                return clusters;
        }

        private static boolean allConverging(Cluster[] clusters) {
                boolean res = true;
                for (int i = 0; i < clusters.length; ++i) {
                        res &= clusters[i].converging;
                }
                return res;
        }

        private static void iterate(Vector3[] points, Cluster[] clusters) {
                /*
                 * Clear each clusters points.
                 */
                for (int i = 0; i < clusters.length; ++i) {
                        clusters[i].points.clear();
                }

                /*
                 * Assign each point to the cluster with the nearest mean.
                 */
                for (int i = 0; i < points.length; ++i) {
                        int minCluster = 0;
                        double minDist = getSquaredDist(clusters[minCluster].mean, points[i]);

                        for (int j = 1; j < clusters.length; j++) {
                                double dist = getSquaredDist(clusters[j].mean, points[i]);
                                if (dist < minDist) {
                                        minDist = dist;
                                        minCluster = j;
                                }
                        }

                        clusters[minCluster].addPoint(points[i]);
                }

                /*
                 * Recalculate each clusters mean to be the mean point of all its points.
                 */
                for (int i = 0; i < clusters.length; ++i) {
                        clusters[i].generateMean();
                }
        }

        private static double getSquaredDist(Vector3 p1, Vector3 p2) {
                return Math.pow(p1.x - p2.x, 2.0) + Math.pow(p1.y - p2.y, 2.0) + Math.pow(p1.z - p2.z, 2.0);
        }

        public static class Cluster {
                public Vector3 mean;
                public List<Vector3> points;

                public boolean converging;

                Cluster(Vector3 mean) {
                        this.mean = mean;
                        this.points = new ArrayList<>();
                        this.converging = false;
                }

                public void addPoint(Vector3 p) {
                        points.add(p);
                }

                public void generateMean() {
                        double x = 0;
                        double y = 0;
                        double z = 0;

                        int count = points.size();

                        for (int i = 0; i < count; ++i) {
                                x += points.get(i).x;
                                y += points.get(i).y;
                                z += points.get(i).z;
                        }

                        x /= count;
                        y /= count;
                        z /= count;

                        Vector3 newMean = new Vector3(
                                (int)Math.rint(x),
                                (int)Math.rint(y),
                                (int)Math.rint(z)
                        );

                        double squaredDist = getSquaredDist(newMean, mean);

                        if (squaredDist <= 2.0) {
                                converging = true;
                        }

                        mean = newMean;
                }
        }
}
