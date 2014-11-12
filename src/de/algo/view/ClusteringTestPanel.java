package de.algo.view;

import de.algo.model.Matrix;
import de.algo.model.MyImage;
import de.algo.model.Vector3;
import de.algo.model.colorquantization.KMeansClustering;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class ClusteringTestPanel extends JPanel {
        private KMeansClustering.Cluster[] clusters;

        private Timer timer;
        private String loadingString;

        private Matrix transformation;
        private Matrix projection;

        public ClusteringTestPanel(MyImage image, int clusterCount) {
                super();

                this.transformation = Matrix.multiply(
                        Matrix.getIsometricRotationMatrix(), Matrix.get3DScalingMatrix(1.5, 1.5, 1.5));
                this.projection = Matrix.getOrthographicProjectionMatrix();

                loadingString = "Creating " + clusterCount + " clusters";

                setBackground(Color.BLACK);
                setForeground(Color.WHITE);
                setFocusable(true);

                new Thread(() -> {
                        Set<Vector3> uniques = new HashSet<>();

                        for (int i = 0; i < image.originalData.length; ++i) {
                                int source = image.originalData[i];
                                int r = (source >> 16) & 0xFF;
                                int g = (source >>  8) & 0xFF;
                                int b =  source        & 0xFF;

                                uniques.add(new Vector3(r, g, b));
                        }

                        Vector3[] colors = uniques.toArray(new Vector3[uniques.size()]);
                        clusters = KMeansClustering.generateCluster(colors, clusterCount);

                        repaint();
                }).start();

                addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyPressed(KeyEvent e) {
                                double angle = 0;
                                switch (e.getKeyCode()) {
                                        case KeyEvent.VK_RIGHT:
                                                angle += 5.0;
                                                break;
                                        case KeyEvent.VK_LEFT:
                                                angle -= 5.0;
                                                break;
                                        default:
                                                return;
                                }
                                transformation = Matrix.multiply(transformation, Matrix.getYRotationMatrix(angle));
                                repaint();
                        }
                });

                timer = new Timer(800, e -> {
                        if (clusters != null) {
                                timer.stop();
                        }

                        loadingString = loadingString + ".";
                        if (loadingString.endsWith("....")) {
                                loadingString = loadingString.substring(0, loadingString.length() - 4);
                        }

                        repaint();
                });
                timer.setRepeats(true);
                timer.start();
        }

        @Override
        public Dimension getPreferredSize() {
                return new Dimension(1000, 800);
        }

        @Override
        protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                if (clusters == null) {
                        g.setFont(g.getFont().deriveFont(18.0f));
                        g.drawString(loadingString, 10, 100);
                } else {
                        g.translate(500, 200);

                        drawAxis(g);

                        for (int i = 0; i < clusters.length; ++i) {
                                KMeansClustering.Cluster c = clusters[i];

                                g.setColor(new Color(c.mean.x, c.mean.y, c.mean.z, 100));
                                for (int j = 0; j < c.points.size(); j += 20) {
                                        Vector3 p = c.points.get(j);
                                        Vector3 t = Matrix.multiply(projection, Matrix.multiply(transformation, p));

                                        g.fillRect(t.x, t.y, 2, 2);
                                }

                                g.setColor(new Color(c.mean.x, c.mean.y, c.mean.z, 255));
                                Vector3 cPoint = Matrix.multiply(projection, Matrix.multiply(transformation, c.mean));
                                g.fillOval(cPoint.x - 5, cPoint.y - 5, 10, 10);
                        }
                }
        }

        private void drawAxis(Graphics g) {
                Vector3 x = new Vector3(255, 0, 0);
                Vector3 y = new Vector3(0, 255, 0);
                Vector3 z = new Vector3(0, 0, 255);

                x = Matrix.multiply(projection, Matrix.multiply(transformation, x));
                y = Matrix.multiply(projection, Matrix.multiply(transformation, y));
                z = Matrix.multiply(projection, Matrix.multiply(transformation, z));

                g.setColor(new Color(255, 255, 255, 100));

                g.drawLine(0, 0, x.x, x.y);
                g.drawLine(0, 0, y.x, y.y);
                g.drawLine(0, 0, z.x, z.y);

                g.setColor(Color.WHITE);

                g.drawString("r", x.x, x.y);
                g.drawString("g", y.x, y.y);
                g.drawString("b", z.x, z.y);
        }
}
