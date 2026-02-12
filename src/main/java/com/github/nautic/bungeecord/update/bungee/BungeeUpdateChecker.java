package com.github.nautic.bungeecord.update.bungee;

import net.md_5.bungee.api.plugin.Plugin;

import java.io.InputStream;
import java.net.URL;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class BungeeUpdateChecker {

    private final Plugin plugin;
    private final int resourceId;

    public BungeeUpdateChecker(Plugin plugin, int resourceId) {
        this.plugin = plugin;
        this.resourceId = resourceId;
    }

    public void getVersion(final Consumer<String> consumer) {

        CompletableFuture.runAsync(() -> {

            try (InputStream is = new URL(
                    "https://api.spigotmc.org/legacy/update.php?resource=" + resourceId
            ).openStream();
                 Scanner scanner = new Scanner(is)) {

                if (!scanner.hasNext()) return;

                String version = scanner.next();

                plugin.getProxy().getScheduler().runAsync(plugin,
                        () -> consumer.accept(version));

            } catch (IOException e) {
                plugin.getLogger().warning("Could not check for updates: " + e.getMessage());
            }

        });
    }
}
