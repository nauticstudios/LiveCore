package com.github.nautic.spigot.utils;

import com.cryptomorin.xseries.XSound;
import org.bukkit.Bukkit;

public final class SoundUtil {

    public static void playAll(String id) {
        XSound.matchXSound(id)
                .ifPresent(sound ->
                        Bukkit.getOnlinePlayers().forEach(sound::play)
                );
    }
}
