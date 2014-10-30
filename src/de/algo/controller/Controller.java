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

import de.algo.view.*;
import de.algo.util.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controller {
        private View view;

        public Controller() {
                view = new View(this);
        }

        public void exit() {
                Logger.log(Logger.INFO, "Exiting.");
                System.exit(0);
        }

        public void processSelectedFiles(File[] selected) {
                Logger.log(Logger.DEBUG, "Attempting to load files.");

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
                        .parallelStream()
                        .filter(f -> isValidFile(f))
                        .map(f -> {
                                Logger.log(Logger.DEBUG, "Loading " + f.getPath());
                                return f;
                        })
                        .collect(Collectors.toList());

                if (files.isEmpty()) {
                        Logger.log(Logger.INFO, "No image files found.");
                }
        }

        private boolean isValidFile(File file) {
                return  file != null &&
                        file.isFile() &&
                        file.canRead() &&
                        view.FILEFILTER.accept(file);
        }
}
