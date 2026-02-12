package com.github.nautic.bungeecord.update.bungee;

import com.github.nautic.bungeecord.utils.addColorBungee;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class BungeeUpdateListener implements Listener {

    private final Plugin plugin;
    private final BungeeUpdateChecker updateChecker;
    private final int resourceId;

    public BungeeUpdateListener(Plugin plugin, int resourceId) {
        this.plugin = plugin;
        this.resourceId = resourceId;
        this.updateChecker = new BungeeUpdateChecker(plugin, resourceId);

        plugin.getProxy().getPluginManager().registerListener(plugin, this);
    }

    @EventHandler
    public void onJoin(PostLoginEvent event) {

        ProxiedPlayer player = event.getPlayer();

        if (!player.hasPermission("livecore.admin")) return;

        updateChecker.getVersion(latestVersion -> {

            String currentVersion = plugin.getDescription().getVersion();

            if (!currentVersion.equalsIgnoreCase(latestVersion)) {

                player.sendMessage(addColorBungee.component("&r"));

                player.sendMessage(addColorBungee.component(
                        "  &#FF2525&lLiveCore &7» &fA new version is available!"
                ));

                player.sendMessage(addColorBungee.component(
                        "  &f[Updated] Your version: &#FF6A6A" + currentVersion
                                + " &f| Latest: &#90FF6A" + latestVersion
                ));

                player.sendMessage(addColorBungee.component(
                        "  &fURL: &#FFBF6Ahttps://www.spigotmc.org/resources/"
                                + resourceId + "/"
                ));

                player.sendMessage(addColorBungee.component("&r"));
            }
        });
    }
}
