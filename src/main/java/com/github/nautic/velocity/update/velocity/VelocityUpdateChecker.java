package com.github.nautic.velocity.update.velocity;

import com.velocitypowered.api.proxy.ProxyServer;

import java.io.InputStream;
import java.net.URL;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class VelocityUpdateChecker {

    private final ProxyServer proxy;
    private final Object plugin;
    private final int resourceId;

    public VelocityUpdateChecker(ProxyServer proxy, Object plugin, int resourceId) {
        this.proxy = proxy;
        this.plugin = plugin;
        this.resourceId = resourceId;
    }

    public void getVersion(Consumer<String> consumer) {

        CompletableFuture.runAsync(() -> {

            try (InputStream is = new URL(
                    "https://api.spigotmc.org/legacy/update.php?resource=" + resourceId
            ).openStream();
                 Scanner scanner = new Scanner(is)) {

                if (!scanner.hasNext()) return;

                String version = scanner.next();

                proxy.getScheduler()
                        .buildTask(plugin, () -> consumer.accept(version))
                        .schedule();

            } catch (IOException e) {
                proxy.getConsoleCommandSource()
                        .sendMessage(net.kyori.adventure.text.Component.text(
                                "Could not check for updates: " + e.getMessage()
                        ));
            }

        });
    }
}
