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

package de.algo.controller;

import de.algo.model.Model;
import de.algo.util.Logger;
import de.algo.util.Timer;
import de.algo.view.InfoBar;
import de.algo.view.View;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controller {
        private View view;
        private Model model;

        public Controller(Model model) {
                this.model = model;
                view = View.createView(model, this);
        }

        public void exit() {
                Logger.log(Logger.INFO, "Exiting.");
                System.exit(0);
        }

        public void processSelectedFiles(File[] selected) {
                Logger.log(Logger.DEBUG, "Attempting to load files.");
                InfoBar.publish("Loading images..");

                new Thread(() -> {
                        Timer timer = new Timer();
                        timer.start();

                        List<File> files = new ArrayList<>(Arrays.asList(selected));
                        List<File> directoryFiles = files
                                .stream()
                                .filter(File::isDirectory)
                                .map(File::listFiles)
                                .map(Arrays::stream)
                                .flatMap(Function.<Stream<File>>identity())
                                .collect(Collectors.toList());

                        files.addAll(directoryFiles);

                        files = files
                                .stream()
                                .filter(f -> isValidFile(f))
                                .map(f -> {
                                        Logger.log(Logger.DEBUG, "Loading " + f.getPath());
                                        return f;
                                })
                                .collect(Collectors.toList());

                        if (files.isEmpty()) {
                                Logger.log(Logger.INFO, "No image files found.");
                                SwingUtilities.invokeLater(()
                                        -> InfoBar.publish("No image files found"));
                        } else {
                                Map<String, Image> imageMap = loadImages(files);
                                model.addImages(imageMap);
                                SwingUtilities.invokeLater(() -> view.updateGallery());

                                timer.stop();
                                Logger.log(
                                        Logger.DEBUG, String.format("Loaded images in %.3f seconds.",
                                                timer.getLastTimeInSeconds()));
                        }
                }).start();
        }

        private Map<String, Image> loadImages(List<File> files) {
                Toolkit toolkit = view.MAIN_FRAME.getToolkit();
                MediaTracker mediaTracker = new MediaTracker(view.MAIN_FRAME);
                Map<String, Image> images = new HashMap<>();
                int trackerID = 0;


                for (File file : files) {
                        Image image = toolkit.createImage(file.getPath());
                        mediaTracker.addImage(image, trackerID++);
                        images.put(file.getPath(), image);
                }

                try {
                        mediaTracker.waitForAll();
                } catch (InterruptedException ex) {
                        ex.printStackTrace();
                }

                return images;
        }

        private boolean isValidFile(File file) {
                return  file != null &&
                        file.isFile() &&
                        file.canRead() &&
                        view.FILEFILTER.accept(file);
        }
}
