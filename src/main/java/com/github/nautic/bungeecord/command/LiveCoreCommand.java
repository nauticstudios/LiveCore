package com.github.nautic.bungeecord.command;

import com.github.nautic.bungeecord.LiveCoreBungeePlugin;
import com.github.nautic.bungeecord.utils.addColorBungee;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

public class LiveCoreCommand extends Command {

    private final LiveCoreBungeePlugin plugin;

    public LiveCoreCommand(LiveCoreBungeePlugin plugin) {
        super("livecore");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (args.length == 0) {

            sender.sendMessage(addColorBungee.Set("&r"));
            sender.sendMessage(addColorBungee.Set(
                    "          &#FF2525&lLiveCore &fv1.0.0 &7(Bungee)"
            ));
            sender.sendMessage(addColorBungee.Set(
                    "     &fPowered by &#3BBFFFSenkex @ Nautic Studios"
            ));
            sender.sendMessage(addColorBungee.Set("&r"));
            return;
        }

        if (args[0].equalsIgnoreCase("help")) {

            if (!sender.hasPermission("livecore.admin")
                    || !sender.hasPermission("livecore.help")) {

                sender.sendMessage(addColorBungee.Set(
                        plugin.messages().getString("messages.no-permission")
                ));
                return;
            }

            plugin.messages()
                    .getStringList("messages.help")
                    .forEach(l -> sender.sendMessage(addColorBungee.Set(l)));
            return;
        }

        if (args[0].equalsIgnoreCase("reload")) {

            if (!sender.hasPermission("livecore.admin")
                    || !sender.hasPermission("livecore.reload")) {

                sender.sendMessage(addColorBungee.Set(
                        plugin.messages().getString("messages.no-permission")
                ));
                return;
            }

            plugin.reloadConfig();
            plugin.reloadMessages();
            CommandLoader.load(plugin);

            sender.sendMessage(addColorBungee.Set(
                    plugin.messages().getString("messages.reload")
            ));
            return;
        }

        if (args[0].equalsIgnoreCase("platforms")) {

            if (!sender.hasPermission("livecore.admin")
                    || !sender.hasPermission("livecore.platforms")) {

                sender.sendMessage(addColorBungee.Set(
                        plugin.messages().getString("messages.no-permission")
                ));
                return;
            }

            Configuration platforms = plugin.getConfig().getSection("platforms");
            if (platforms == null) return;

            for (String line : plugin.messages().getStringList("messages.platforms")) {

                if (!line.contains("%avaible_platforms_name%")) {
                    sender.sendMessage(addColorBungee.Set(line));
                    continue;
                }

                for (String platformId : platforms.getKeys()) {

                    Configuration platform = platforms.getSection(platformId);
                    if (platform == null) continue;

                    String domain = platform.getString("domain", "unknown");
                    String perm = platform.getString("permission", "none");

                    sender.sendMessage(addColorBungee.Set(
                            line.replace("%avaible_platforms_name%", platformId)
                                    .replace("%avaible_platforms_domain%", domain)
                                    .replace("%platforms_permission%", perm)
                    ));
                }
            }
        }
    }
}