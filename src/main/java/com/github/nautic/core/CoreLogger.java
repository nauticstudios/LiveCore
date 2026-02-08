package com.github.nautic.core;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public final class CoreLogger {

    private static final DateTimeFormatter TIME =
            DateTimeFormatter.ofPattern("HH:mm:ss");

    private CoreLogger() {}

    public static void info(String message) {
        System.out.println(format("INFO", message));
    }

    public static void warn(String message) {
        System.out.println(format("WARN", message));
    }

    public static void error(String message) {
        System.err.println(format("ERROR", message));
    }

    private static String format(String level, String message) {
        return "[" + TIME.format(LocalTime.now()) + " | " + level + "] " + message;
    }
}