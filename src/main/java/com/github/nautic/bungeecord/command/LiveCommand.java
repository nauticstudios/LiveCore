package com.github.nautic.bungeecord.command;

import com.github.nautic.bungeecord.LiveCoreBungeePlugin;
import com.github.nautic.bungeecord.handler.BroadcastHandler;
import com.github.nautic.bungeecord.manager.CooldownManager;
import com.github.nautic.bungeecord.utils.addColorBungee;
import com.github.nautic.core.utils.URLUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

import java.util.stream.Collectors;

public class LiveCommand extends Command {

    private final LiveCoreBungeePlugin plugin;

    public LiveCommand(LiveCoreBungeePlugin plugin, String name) {
        super(name);
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof ProxiedPlayer player)) return;

        String globalPerm =
                plugin.getConfig().getString("commands.permission");

        if (globalPerm != null && !player.hasPermission(globalPerm)) {
            send(player, "messages.no-permission");
            return;
        }

        if (args.length == 0) {
            sendSyntax(player);
            return;
        }

        String normalizedUrl = URLUtil.normalize(args[0]);
        String inputDomain = URLUtil.extractDomain(normalizedUrl);

        Configuration platforms =
                plugin.getConfig().getSection("platforms");

        if (platforms == null) {
            sendSyntax(player);
            return;
        }

        for (String platformId : platforms.getKeys()) {

            Configuration platform =
                    platforms.getSection(platformId);
            if (platform == null) continue;

            String domain = platform.getString("domain");
            if (domain == null) continue;

            if (!inputDomain.endsWith(domain.toLowerCase())) continue;

            String perm = platform.getString("permission");
            if (perm != null && !player.hasPermission(perm)) {
                send(player,
                        "messages.platforms-no-permission",
                        "%platform_permission%",
                        perm
                );
                return;
            }

            String bypass =
                    plugin.getConfig()
                            .getString("permissions.bypass-cooldown");

            int cooldown =
                    platform.getInt("cooldown", 0);

            if (bypass == null || !player.hasPermission(bypass)) {

                if (!CooldownManager.check(
                        player.getUniqueId(),
                        platformId,
                        cooldown
                )) {

                    int remaining =
                            CooldownManager.getRemaining(
                                    player.getUniqueId(),
                                    platformId
                            );

                    send(player,
                            "messages.cooldown",
                            "%cooldown%",
                            String.valueOf(remaining)
                    );
                    return;
                }
            }

            new BroadcastHandler(plugin)
                    .broadcast(player, platformId, normalizedUrl);
            return;
        }

        sendSyntax(player);
    }

    private void sendSyntax(ProxiedPlayer player) {

        plugin.messages()
                .getStringList("syntax.aliases.error")
                .forEach(line ->
                        player.sendMessage(
                                addColorBungee.Set(
                                        line.replace("%aliases%", getName())
                                                .replace("%domain%", "<domain>")
                                                .replace(
                                                        "%platforms_available%",
                                                        getPlatformsAvailable()
                                                )
                                )
                        )
                );
    }

    private String getPlatformsAvailable() {

        Configuration platforms =
                plugin.getConfig().getSection("platforms");

        if (platforms == null) return "";

        return platforms.getKeys()
                .stream()
                .collect(Collectors.joining(", "));
    }

    private void send(ProxiedPlayer player, String path) {

        String msg = plugin.messages().getString(path);
        if (msg != null && !msg.isEmpty()) {
            player.sendMessage(addColorBungee.Set(msg));
        }
    }

    private void send(
            ProxiedPlayer player,
            String path,
            String placeholder,
            String value
    ) {

        String msg = plugin.messages().getString(path);
        if (msg != null && !msg.isEmpty()) {
            player.sendMessage(
                    addColorBungee.Set(
                            msg.replace(placeholder, value)
                    )
            );
        }
    }
}
