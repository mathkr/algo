package de.algo.model;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public class Model extends Observable {
        public Map<String, Image> loadedImages;

        public Model() {
                loadedImages = new HashMap<>();
        }

        public void addImages(Map<String, Image> images) {
                images.forEach((key, image) -> {
                        if (!loadedImages.containsKey(key)) {
                                loadedImages.put(key, image);
                                setChanged();
                        }
                });

                notifyObservers();
        }
}
