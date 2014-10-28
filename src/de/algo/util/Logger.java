package de.algo.util;

public class Logger {
        public final static int DEBUG = 3;
        public final static int INFO = 2;
        public final static int WARNING = 1;
        public final static int ERROR = 0;

        private static int logLevel = DEBUG;

        public static void setMaximumLogLevel(int level) {
                logLevel = level;
        }

        public static void log(int level, String msg) {
                if (level <= logLevel) {
                        System.out.println(msg);
                }
        }
}
