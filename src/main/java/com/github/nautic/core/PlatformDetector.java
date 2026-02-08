package com.github.nautic.core;

public final class PlatformDetector {

    private PlatformDetector() {}

    public static PlatformType detect() {

        CoreLogger.info("Detecting runtime platform environment...");

        if (exists("org.bukkit.Bukkit")) {
            CoreLogger.info("Platform detected: Spigot/Paper");
            return PlatformType.SPIGOT;
        }

        if (exists("com.velocitypowered.api.proxy.ProxyServer")) {
            CoreLogger.info("Platform detected: Velocity");
            return PlatformType.VELOCITY;
        }

        if (exists("net.md_5.bungee.api.ProxyServer")) {
            CoreLogger.info("Platform detected: BungeeCord");
            return PlatformType.BUNGEECORD;
        }

        CoreLogger.error("Unable to detect a supported platform");
        return PlatformType.UNKNOWN;
    }

    private static boolean exists(String clazz) {
        try {
            Class.forName(clazz);
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }
}