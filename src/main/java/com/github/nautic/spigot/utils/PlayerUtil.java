package com.github.nautic.spigot.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;

public final class PlayerUtil {

    private PlayerUtil() {}

    public static Collection<? extends Player> onlinePlayers() {
        return Bukkit.getOnlinePlayers();
    }

    public static void forEachOnline(java.util.function.Consumer<Player> action) {
        for (Player player : onlinePlayers()) {
            if (player != null && player.isOnline()) {
                action.accept(player);
            }
        }
    }
}
