package com.github.nautic.core;

import com.github.nautic.core.platform.PlatformAdapter;
import com.github.nautic.spigot.LiveCoreSpigot;
import com.github.nautic.velocity.LiveCoreVelocity;
import com.github.nautic.bungeecord.LiveCoreBungeecord;

public final class LiveCore {

    private static PlatformAdapter adapter;

    public static void start() {

        CoreLogger.info("Starting LiveCore initialization sequence...");

        PlatformType type = PlatformDetector.detect();

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

        CoreLogger.info("LiveCore successfully enabled on " + type);
    }

    public static void stop() {
        if (adapter != null) {
            CoreLogger.info("Shutting down LiveCore...");
            adapter.disable();
            CoreLogger.info("LiveCore shutdown complete");
        }
    }
}