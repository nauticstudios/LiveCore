package com.github.nautic.core;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.function.BiConsumer;

public final class CoreLogger {

    private static final DateTimeFormatter TIME =
            DateTimeFormatter.ofPattern("HH:mm:ss");

    private static BiConsumer<Level, String> logger;

    public enum Level {
        INFO, WARN, ERROR
    }

    private CoreLogger() {}

    public static void setLogger(BiConsumer<Level, String> logFunction) {
        logger = logFunction;
    }

    public static void info(String message) {
        log(Level.INFO, message);
    }

    public static void warn(String message) {
        log(Level.WARN, message);
    }

    public static void error(String message) {
        log(Level.ERROR, message);
    }

    private static void log(Level level, String message) {

        String formatted = "[" + TIME.format(LocalTime.now())
                + " | " + level + "] " + message;

        if (logger != null) {
            logger.accept(level, formatted);
        }
    }
}
