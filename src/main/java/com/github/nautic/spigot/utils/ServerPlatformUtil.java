package com.github.nautic.spigot.utils;

import org.bukkit.Bukkit;

public final class ServerPlatformUtil {

    private static final String SERVER_NAME =
            Bukkit.getServer().getName();

    private ServerPlatformUtil() {}

    public static boolean isFolia() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }

    public static boolean isPaper() {
        try {
            Class.forName("com.destroystokyo.paper.PaperConfig");
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }

    public static boolean isPurpur() {
        return SERVER_NAME.toLowerCase().contains("purpur");
    }

    public static boolean isSpigot() {
        return SERVER_NAME.toLowerCase().contains("spigot");
    }

    public static String getPlatformName() {

        if (isFolia()) return "Folia";
        if (isPurpur()) return "Purpur";
        if (isPaper()) return "Paper";
        if (isSpigot()) return "Spigot";

        return SERVER_NAME;
    }
}
