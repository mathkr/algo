package de.algo.model.colorquantization;

import de.algo.model.MyImage;
import de.algo.model.Vector3;

import java.util.*;

public class ColorQuantizer {
        enum Mode {
                MOST_FREQUENT,
                CLUSTERS
        }

        private static float getDistance(int left, int right) {
                int r_left = (left >> 16) & 0xFF;
                int g_left = (left >>  8) & 0xFF;
                int b_left = (left)       & 0xFF;

                int r_right = (right >> 16) & 0xFF;
                int g_right = (right >>  8) & 0xFF;
                int b_right = (right)       & 0xFF;

                int d_r = r_left - r_right;
                int d_g = g_left - g_right;
                int d_b = b_left - b_right;

                float res = (float)Math.sqrt(d_r * d_r + d_g * d_g + d_b * d_b);
                return res;
        }

        public static void quantizeColorsWithClustering(MyImage image, int k) {
                List<MyImage.ColorEntry> allColorsSorted = image.getSortedColors();

                List<Vector3> colorPoints = new ArrayList<>();
                for (int i = 0; i < allColorsSorted.size(); ++i) {
                        int r = (allColorsSorted.get(i).color >> 16) & 0xFF;
                        int g = (allColorsSorted.get(i).color >>  8) & 0xFF;
                        int b = (allColorsSorted.get(i).color)       & 0xFF;
                        colorPoints.add(new Vector3(r, g, b));
                }

                Vector3[] colorPointsArray = colorPoints.toArray(new Vector3[colorPoints.size()]);
                KMeansClustering.Cluster[] clusters = KMeansClustering.generateCluster(colorPointsArray, k);
                Map<Integer, Integer> resultMap = new HashMap<>(allColorsSorted.size() * 2);

                for (int i = 0; i < clusters.length; ++i) {
                        Vector3 meanVector = clusters[i].mean;
                        int meanPixel = (0xFF << 24) | (meanVector.x << 16) | (meanVector.y << 8) | meanVector.z;
                        for (int j = 0; j < clusters[i].points.size(); ++j) {
                                Vector3 sourceVector = clusters[i].points.get(j);
                                int sourcePixel = (0xFF << 24) | (sourceVector.x << 16) | (sourceVector.y << 8) | sourceVector.z;
                                resultMap.put(sourcePixel, meanPixel);
                        }
                }

                // replace all colors in the image
                for (int i = 0; i < image.modifiedData.length; ++i) {
                        if (resultMap.containsKey((0xFF << 24) | image.modifiedData[i])) {
                                image.modifiedData[i] = resultMap.get(image.modifiedData[i]);
                        }
                }
        }

        public static void quantizeColors(MyImage image, int absolute) {
                List<MyImage.ColorEntry> allColorsSorted = image.getSortedColors();

                int allColorsCount = allColorsSorted.size();
                int resultingColorsCount = absolute < allColorsCount ? absolute : allColorsCount; //(int)(percent * allColorsCount);

                //System.out.println("resultingColorsCount = " + resultingColorsCount);
                //System.out.println("allColorsCount = " + allColorsCount);

                List<Integer> targetColors = new ArrayList<>(resultingColorsCount);
                for (int i = 0; i < resultingColorsCount; ++i) {
                        targetColors.add(allColorsSorted.get(i).color);
                }

                List<Integer> sourceColors = new ArrayList<>(allColorsCount - resultingColorsCount);
                for (int i = resultingColorsCount; i < allColorsCount; ++i) {
                        sourceColors.add(allColorsSorted.get(i).color);
                }

                Map<Integer, Integer> resultMap = new HashMap<>(sourceColors.size() * 2);

                Comparator<Integer> redComparator = new Comparator<Integer>() {
                        @Override
                        public int compare(Integer left, Integer right) {
                                int r_left = (left.intValue() >> 16) & 0xFF;
                                int r_right = (right.intValue() >> 16) & 0xFF;
                                int r_comp = Integer.compare(r_left, r_right);

                                if (r_comp != 0) {
                                        return r_comp;
                                }

                                int g_left = (left.intValue() >>  8) & 0xFF;
                                int g_right = (right.intValue() >>  8) & 0xFF;
                                int g_comp = Integer.compare(g_left, g_right);

                                if (g_comp != 0) {
                                        return g_comp;
                                }

                                int b_left = (left.intValue())       & 0xFF;
                                int b_right = (right.intValue())       & 0xFF;
                                int b_comp = Integer.compare(b_left, b_right);
                                return b_comp;
                        }
                };

                Comparator<Integer> greenComparator = new Comparator<Integer>() {
                        @Override
                        public int compare(Integer left, Integer right) {
                                int g_left = (left.intValue() >>  8) & 0xFF;
                                int g_right = (right.intValue() >>  8) & 0xFF;
                                int g_comp = Integer.compare(g_left, g_right);

                                if (g_comp != 0) {
                                        return g_comp;
                                }

                                int r_left = (left.intValue() >> 16) & 0xFF;
                                int r_right = (right.intValue() >> 16) & 0xFF;
                                int r_comp = Integer.compare(r_left, r_right);

                                if (r_comp != 0) {
                                        return r_comp;
                                }

                                int b_left = (left.intValue())       & 0xFF;
                                int b_right = (right.intValue())       & 0xFF;
                                int b_comp = Integer.compare(b_left, b_right);
                                return b_comp;
                        }
                };

                Comparator<Integer> blueComparator = new Comparator<Integer>() {
                        @Override
                        public int compare(Integer left, Integer right) {
                                int b_left = (left.intValue())       & 0xFF;
                                int b_right = (right.intValue())       & 0xFF;
                                int b_comp = Integer.compare(b_left, b_right);

                                if (b_comp != 0) {
                                        return b_comp;
                                }

                                int r_left = (left.intValue() >> 16) & 0xFF;
                                int r_right = (right.intValue() >> 16) & 0xFF;
                                int r_comp = Integer.compare(r_left, r_right);

                                if (r_comp != 0) {
                                        return r_comp;
                                }

                                int g_left = (left.intValue() >>  8) & 0xFF;
                                int g_right = (right.intValue() >>  8) & 0xFF;
                                int g_comp = Integer.compare(g_left, g_right);
                                return g_comp;
                        }
                };

                List<Integer> redList = new ArrayList<>(targetColors);
                redList.sort(redComparator);

                List<Integer> greenList = new ArrayList<>(targetColors);
                greenList.sort(greenComparator);

                List<Integer> blueList = new ArrayList<>(targetColors);
                blueList.sort(blueComparator);

                //System.out.println("iterating over image");

                List<Integer> candidates = new ArrayList<>();
                for (int i = 0; i < sourceColors.size(); ++i) {
                        candidates.clear();
                        int sourceColor = sourceColors.get(i);

                        //System.out.println("i = " + i);
                        //System.out.println("sourceColor = " + sourceColor);

                        // red candidates
                        int max = targetColors.size() - 1;
                        int min = 0;
                        int colorAtIndex = 0;
                        int index = 0;
                        //System.out.println("before while");
                        while (min < max) {
                                //System.out.println("min = " + min + ", max = " + max);
                                index = min + ((max - min) / 2);
                                colorAtIndex = redList.get(index);
                                if (redComparator.compare(colorAtIndex, sourceColor) > 0) {
                                        // color at index is bigger: search to the left
                                        max = index - 1;
                                } else if (redComparator.compare(colorAtIndex, sourceColor) < 0) {
                                        // color at index is smaller: search to the right
                                        min = index + 1;
                                } else {
                                        // found the perfect match!
                                        break;
                                }
                        }
                        //System.out.println("afterwhile");

                        // add our first candidate
                        candidates.add(colorAtIndex);
                        if (redComparator.compare(colorAtIndex, sourceColor) > 0) {
                                // also take the color to the left
                                if (index > 0) {
                                        candidates.add(redList.get(index - 1));
                                }
                        } else if (redComparator.compare(colorAtIndex, sourceColor) < 0) {
                                // also take the color to the right
                                if (index < targetColors.size() - 1) {
                                        candidates.add(redList.get(index + 1));
                                }
                        } else {
                                // take the color to the left, we don't really care
                                if (index > 0) {
                                        candidates.add(redList.get(index - 1));
                                }
                        }

                        // green candidates
                        max = targetColors.size() - 1;
                        min = 0;
                        colorAtIndex = 0;
                        index = 0;
                        while (min < max) {
                                index = min + ((max - min) / 2);
                                colorAtIndex = greenList.get(index);
                                if (greenComparator.compare(colorAtIndex, sourceColor) > 0) {
                                        // color at index is bigger: search to the left
                                        max = index - 1;
                                } else if (greenComparator.compare(colorAtIndex, sourceColor) < 0) {
                                        // color at index is smaller: search to the right
                                        min = index + 1;
                                } else {
                                        // found the perfect match!
                                        break;
                                }
                        }

                        /*
                        int min = 0;
                        int max = a.length - 1;
                        while (min < max) {
                                int mid = min + ((max - min) / 2);
                                if      (comparator.compare(key, a[mid]) < 0) max = mid - 1;
                                else if (comparator.compare(key, a[mid]) > 0) min = mid + 1;
                                else return mid;
                        }
                        return min + (max - min) / 2;
                        */

                        // add our first candidate
                        candidates.add(colorAtIndex);
                        if (greenComparator.compare(colorAtIndex, sourceColor) > 0) {
                                // also take the color to the left
                                if (index > 0) {
                                        candidates.add(greenList.get(index - 1));
                                }
                        } else if (greenComparator.compare(colorAtIndex, sourceColor) < 0) {
                                // also take the color to the right
                                if (index < targetColors.size() - 1) {
                                        candidates.add(greenList.get(index + 1));
                                }
                        } else {
                                // take the color to the left, we don't really care
                                if (index > 0) {
                                        candidates.add(greenList.get(index - 1));
                                }
                        }

                        // blue candidates
                        max = targetColors.size() - 1;
                        min = 0;
                        colorAtIndex = 0;
                        index = 0;
                        while (min < max) {
                                index = min + ((max - min) / 2);
                                colorAtIndex = blueList.get(index);
                                if (blueComparator.compare(colorAtIndex, sourceColor) > 0) {
                                        // color at index is bigger: search to the left
                                        max = index - 1;
                                } else if (blueComparator.compare(colorAtIndex, sourceColor) < 0) {
                                        // color at index is smaller: search to the right
                                        min = index + 1;
                                } else {
                                        // found the perfect match!
                                        break;
                                }
                        }

                        // add our first candidate
                        candidates.add(colorAtIndex);
                        if (blueComparator.compare(colorAtIndex, sourceColor) > 0) {
                                // also take the color to the left
                                if (index > 0) {
                                        candidates.add(blueList.get(index - 1));
                                }
                        } else if (blueComparator.compare(colorAtIndex, sourceColor) < 0) {
                                // also take the color to the right
                                if (index < targetColors.size() - 1) {
                                        candidates.add(blueList.get(index + 1));
                                }
                        } else {
                                // take the color to the left, we don't really care
                                if (index > 0) {
                                        candidates.add(blueList.get(index - 1));
                                }
                        }

                        // find the closest of our candidates
                        float minDist = getDistance(candidates.get(0), sourceColor);
                        int minColor = candidates.get(0);
                        for (int j = 1; j < candidates.size(); ++j) {
                                float dist = getDistance(candidates.get(j), sourceColor);
                                if (dist < minDist) {
                                        minDist = dist;
                                        minColor = candidates.get(j);
                                }
                        }

                        // check if there's a closer candidate in our calculated maximum range.
                        // we take the blue projection here, because that's the last one we checked and we still have
                        // the computed index.

                        // first search to the left
                        int checkingIndex = index;
                        float currentBlueDelta = Math.abs((blueList.get(checkingIndex) & 0xFF) - (sourceColor & 0xFF));

                        while (currentBlueDelta <= minDist) {
                                float dist = getDistance(blueList.get(checkingIndex), sourceColor);
                                if (dist < minDist) {
                                        minDist = dist;
                                        minColor = blueList.get(checkingIndex);
                                }

                                --checkingIndex;
                                if (checkingIndex <= 0) {
                                        break;
                                } else {
                                        currentBlueDelta = Math.abs((blueList.get(checkingIndex) & 0xFF) - (sourceColor & 0xFF));
                                }
                        }

                        // then search to the right
                        checkingIndex = index;
                        currentBlueDelta = Math.abs((blueList.get(checkingIndex) & 0xFF) - (sourceColor & 0xFF));

                        while (currentBlueDelta <= minDist) {
                                float dist = getDistance(blueList.get(checkingIndex), sourceColor);
                                if (dist < minDist) {
                                        minDist = dist;
                                        minColor = blueList.get(checkingIndex);
                                }

                                ++checkingIndex;
                                if (checkingIndex >= blueList.size()) {
                                        break;
                                } else {
                                        currentBlueDelta = Math.abs((blueList.get(checkingIndex) & 0xFF) - (sourceColor & 0xFF));
                                }
                        }

                        // minColor should now hold our optimal color?
                        resultMap.put(sourceColor, minColor);
                }

                // replace all colors in the image
                for (int i = 0; i < image.modifiedData.length; ++i) {
                        if (resultMap.containsKey(image.modifiedData[i])) {
                                image.modifiedData[i] = resultMap.get(image.modifiedData[i]);
                        }
                }
        }
}
