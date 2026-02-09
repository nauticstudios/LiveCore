package com.github.nautic.spigot.command;

import com.github.nautic.core.utils.URLUtil;
import com.github.nautic.core.utils.addColor;
import com.github.nautic.spigot.LiveCoreSpigotPlugin;
import com.github.nautic.spigot.handler.BroadcastHandler;
import com.github.nautic.spigot.manager.CooldownManager;
import org.bukkit.command.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.stream.Collectors;

public class LiveCommand implements CommandExecutor {

    private final LiveCoreSpigotPlugin plugin;

    public LiveCommand(LiveCoreSpigotPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command cmd,
            String label,
            String[] args
    ) {

        if (!(sender instanceof Player player)) return true;

        String globalPerm =
                plugin.getConfig().getString("commands.permission");

        if (globalPerm != null && !player.hasPermission(globalPerm)) {
            sendMessage(player, "messages.no-permission");
            return true;
        }

        if (args.length == 0) {
            sendSyntax(player, cmd);
            return true;
        }

        String normalizedUrl = URLUtil.normalize(args[0]);
        String inputDomain = URLUtil.extractDomain(normalizedUrl);

        ConfigurationSection platforms =
                plugin.getConfig().getConfigurationSection("platforms");

        if (platforms == null) {
            sendSyntax(player, cmd);
            return true;
        }

        for (String platformId : platforms.getKeys(false)) {

            ConfigurationSection platform =
                    platforms.getConfigurationSection(platformId);
            if (platform == null) continue;

            String domain = platform.getString("domain");
            if (domain == null) continue;

            if (!inputDomain.endsWith(domain.toLowerCase())) continue;

            String perm = platform.getString("permission");
            if (perm != null && !player.hasPermission(perm)) {
                sendMessage(
                        player,
                        "messages.platforms-no-permission",
                        "%platform_permission%",
                        perm
                );
                return true;
            }

            String bypass =
                    plugin.getConfig()
                            .getString("permissions.bypass-cooldown");

            int cooldown =
                    platform.getInt("cooldown", 0);

            if (bypass == null || !player.hasPermission(bypass)) {

                if (!CooldownManager.check(
                        player,
                        platformId,
                        cooldown
                )) {

                    int remaining =
                            CooldownManager.getRemaining(
                                    player,
                                    platformId
                            );

                    sendMessage(
                            player,
                            "messages.cooldown",
                            "%cooldown%",
                            String.valueOf(remaining)
                    );
                    return true;
                }
            }

            new BroadcastHandler(plugin)
                    .broadcast(player, platformId, normalizedUrl);

            return true;
        }

        sendSyntax(player, cmd);
        return true;
    }

    private void sendSyntax(Player player, Command cmd) {

        for (String line :
                plugin.messages().getStringList("syntax.aliases.error")) {

            player.sendMessage(
                    addColor.Set(
                            line.replace("%aliases%", cmd.getName())
                                    .replace("%domain%", "<domain>")
                                    .replace(
                                            "%platforms_available%",
                                            getPlatformsAvailable()
                                    )
                    )
            );
        }
    }

    private String getPlatformsAvailable() {

        ConfigurationSection platforms =
                plugin.getConfig().getConfigurationSection("platforms");

        if (platforms == null) return "";

        Set<String> keys = platforms.getKeys(false);
        return keys.stream().collect(Collectors.joining(", "));
    }

    private void sendMessage(Player player, String path) {

        String msg = plugin.messages().getString(path);
        if (msg != null && !msg.isEmpty()) {
            player.sendMessage(addColor.Set(msg));
        }
    }

    private void sendMessage(
            Player player,
            String path,
            String placeholder,
            String value
    ) {

        String msg = plugin.messages().getString(path);
        if (msg != null && !msg.isEmpty()) {
            player.sendMessage(
                    addColor.Set(
                            msg.replace(placeholder, value)
                    )
            );
        }
    }
}
