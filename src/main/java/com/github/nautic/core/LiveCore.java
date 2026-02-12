package com.github.nautic.core;

import com.github.nautic.core.platform.PlatformAdapter;
import com.github.nautic.spigot.LiveCoreSpigot;
import com.github.nautic.velocity.LiveCoreVelocity;
import com.github.nautic.bungeecord.LiveCoreBungeecord;

public final class LiveCore {

    private static PlatformAdapter adapter;
    private static boolean started = false;

    private LiveCore() {}

    public static void start() {

        if (started) {
            CoreLogger.warn("LiveCore is already started. Ignoring duplicate start call.");
            return;
        }

        CoreLogger.info("Starting LiveCore initialization sequence...");

        PlatformType type = PlatformDetector.detect();

        if (type == PlatformType.UNKNOWN) {
            CoreLogger.error("Unsupported platform environment detected. Aborting startup.");
            return;
        }

        try {

            adapter = switch (type) {
                case SPIGOT -> new LiveCoreSpigot();
                case VELOCITY -> new LiveCoreVelocity();
                case BUNGEECORD -> new LiveCoreBungeecord();
                default -> throw new IllegalStateException("Unsupported platform environment");
            };

            CoreLogger.info("Loading platform adapter...");
            adapter.load();

            CoreLogger.info("Enabling LiveCore services...");
            adapter.enable();

            started = true;

            CoreLogger.info("LiveCore successfully enabled on " + type);

        } catch (Exception ex) {

            CoreLogger.error("An error occurred during LiveCore startup: " + ex.getMessage());
            ex.printStackTrace();

        }
    }

    public static void stop() {

        if (!started || adapter == null) {
            CoreLogger.warn("LiveCore stop() called but it was not started.");
            return;
        }

        try {

            CoreLogger.info("Shutting down LiveCore...");
            adapter.disable();

            started = false;

            CoreLogger.info("LiveCore shutdown complete");

        } catch (Exception ex) {

            CoreLogger.error("An error occurred during LiveCore shutdown: " + ex.getMessage());
            ex.printStackTrace();

        }
    }

    public static boolean isStarted() {
        return started;
    }
}