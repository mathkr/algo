package de.algo.util;

public class Timer {
        private long startTime;
        private long stopTime;
        private long lastTime;

        public Timer() {
                startTime = 0;
                stopTime = 0;
                lastTime = 0;
        }

        public void start() {
                startTime = System.currentTimeMillis();
        }

        public void stop() {
                stopTime = System.currentTimeMillis();
                lastTime = stopTime - startTime;
        }

        public long getLastTime() {
                return lastTime;
        }

        public double getLastTimeInSeconds() {
                return lastTime / 1000.0;
        }
}
