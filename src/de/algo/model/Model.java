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

import de.algo.view.InfoBar;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public class Model extends Observable {
        public Map<String, MyImage> loadedImages;

        public Model() {
                loadedImages = new HashMap<>();
        }

        public void addImages(Map<String, Image> images) {
                int diff = loadedImages.size();

                images.forEach((key, image) -> {
                        if (!loadedImages.containsKey(key)) {
                                loadedImages.put(key, new MyImage(key, image));
                                setChanged();
                        }
                });

                diff = loadedImages.size() - diff;
                InfoBar.publish(diff + " images added.");

                notifyObservers();
        }
}
