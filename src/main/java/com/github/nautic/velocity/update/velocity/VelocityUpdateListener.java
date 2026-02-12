package com.github.nautic.velocity.update.velocity;

import com.github.nautic.velocity.utils.addColorVelocity;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.Player;

public class VelocityUpdateListener {

    private final ProxyServer proxy;
    private final Object plugin;
    private final VelocityUpdateChecker updateChecker;
    private final int resourceId;

    public VelocityUpdateListener(ProxyServer proxy, Object plugin, int resourceId) {
        this.proxy = proxy;
        this.plugin = plugin;
        this.resourceId = resourceId;
        this.updateChecker = new VelocityUpdateChecker(proxy, plugin, resourceId);

        proxy.getEventManager().register(plugin, this);
    }

    @Subscribe
    public void onJoin(PostLoginEvent event) {

        Player player = event.getPlayer();

        if (!player.hasPermission("livecore.admin")) return;

        updateChecker.getVersion(latestVersion -> {

            String currentVersion = proxy.getPluginManager()
                    .getPlugin("livecore")
                    .flatMap(p -> p.getDescription().getVersion())
                    .orElse("Unknown");

            if (!currentVersion.equalsIgnoreCase(latestVersion)) {

                player.sendMessage(addColorVelocity.Set("&r"));

                player.sendMessage(addColorVelocity.Set(
                        "  &#FF2525&lLiveCore &7» &fA new version is available!"
                ));

                player.sendMessage(addColorVelocity.Set(
                        "  &f[Updated] Your version: &#FF6A6A" + currentVersion
                                + " &f| Latest: &#90FF6A" + latestVersion
                ));

                player.sendMessage(addColorVelocity.Set(
                        "  &fURL: &#FFBF6Ahttps://www.spigotmc.org/resources/"
                                + resourceId + "/"
                ));

                player.sendMessage(addColorVelocity.Set("&r"));
            }
        });
    }
}
