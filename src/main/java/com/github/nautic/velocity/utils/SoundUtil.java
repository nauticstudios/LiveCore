package com.github.nautic.velocity.utils;

import com.github.nautic.velocity.LiveCoreVelocityPlugin;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;

public final class SoundUtil {

    private SoundUtil() {}

    public static Sound parse(
            LiveCoreVelocityPlugin plugin,
            String rawId
    ) {

        if (rawId == null || rawId.isBlank()) {
            return null;
        }

        String id = rawId.trim();

        if (id.contains(":")) {
            return build(plugin, id.toLowerCase());
        }

        String adventureId =
                "minecraft:" + id.toLowerCase().replace('_', '.');

        return build(plugin, adventureId);
    }

    private static Sound build(
            LiveCoreVelocityPlugin plugin,
            String key
    ) {
        try {
            return Sound.sound(
                    Key.key(key),
                    Sound.Source.MASTER,
                    1f,
                    1f
            );
        } catch (Exception e) {
            plugin.getLogger().warn(
                    "[LiveCore] Invalid sound '{}' for Velocity. " +
                            "Check messages.yml (Spigot sounds must be valid).",
                    key
            );
            return null;
        }
    }
}
