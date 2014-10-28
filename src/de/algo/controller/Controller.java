package de.algo.controller;

import de.algo.view.*;
import de.algo.util.*;

import java.awt.event.*;

public class Controller {
        private View view;

        public Controller(View view) {
                this.view = view;

                initView();
        }

        private void initView() {
                view.MAIN_FRAME.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                                exit();
                        }
                });

                view.MAIN_FRAME.setVisible(true);
        }

        private void exit() {
                Logger.log(Logger.INFO, "Exiting...");
                System.exit(0);
        }
}
