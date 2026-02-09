package com.github.nautic.spigot.manager;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class CooldownManager {

    private static final Map<String, Long> cooldowns = new HashMap<>();

    private CooldownManager() {}

    private static String key(UUID uuid, String platform) {
        return uuid + ":" + platform;
    }

    public static boolean check(Player player, String platform, int seconds) {

        if (seconds <= 0) return true;

        String key = key(player.getUniqueId(), platform);
        long now = System.currentTimeMillis();

        Long expiresAt = cooldowns.get(key);

        if (expiresAt != null && expiresAt > now) {
            return false;
        }

        cooldowns.put(key, now + (seconds * 1000L));
        return true;
    }

    public static int getRemaining(Player player, String platform) {

        String key = key(player.getUniqueId(), platform);
        Long expiresAt = cooldowns.get(key);

        if (expiresAt == null) return 0;

        long remainingMs = expiresAt - System.currentTimeMillis();
        if (remainingMs <= 0) return 0;

        return (int) Math.ceil(remainingMs / 1000.0);
    }
}
