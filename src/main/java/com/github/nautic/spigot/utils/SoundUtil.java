package com.github.nautic.spigot.utils;

import com.cryptomorin.xseries.XSound;

public final class SoundUtil {

    private SoundUtil() {}

    public static void playAll(String id) {

        if (id == null || id.isEmpty()) return;

        XSound.matchXSound(id).ifPresent(sound ->
                PlayerUtil.forEachOnline(sound::play)
        );
    }
}
