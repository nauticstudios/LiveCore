package com.github.nautic.spigot.command;

import com.github.nautic.core.utils.addColor;
import com.github.nautic.spigot.LiveCoreSpigotPlugin;
import com.github.nautic.spigot.utils.ServerPlatformUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

public class LiveCoreCommand implements CommandExecutor {

    private final LiveCoreSpigotPlugin plugin;

    public LiveCoreCommand(LiveCoreSpigotPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 0) {

            sender.sendMessage(addColor.Set("&r"));
            sender.sendMessage(addColor.Set(
                    "          &#FF2525&lLiveCore &fv1.1.0 &7("
                            + ServerPlatformUtil.getPlatformName() + ")"
            ));
            sender.sendMessage(addColor.Set(
                    "     &fPowered by &#3BBFFFSenkex @ Nautic Studios"
            ));
            sender.sendMessage(addColor.Set("&r"));

            return true;
        }

        if (args[0].equalsIgnoreCase("help")) {

            if (!sender.hasPermission("livecore.admin")
                    || !sender.hasPermission("livecore.help")) {

                sender.sendMessage(addColor.Set(
                        plugin.messages().getString("messages.no-permission")
                ));
                return true;
            }

            plugin.messages()
                    .getStringList("messages.help")
                    .forEach(l -> sender.sendMessage(addColor.Set(l)));

            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {

            if (!sender.hasPermission("livecore.admin")
                    || !sender.hasPermission("livecore.reload")) {

                sender.sendMessage(addColor.Set(
                        plugin.messages().getString("messages.no-permission")
                ));
                return true;
            }

            plugin.reloadConfig();
            plugin.reloadMessages();
            CommandLoader.load(plugin);

            sender.sendMessage(addColor.Set(
                    plugin.messages().getString("messages.reload")
            ));
            return true;
        }

        if (args[0].equalsIgnoreCase("platforms")) {

            if (!sender.hasPermission("livecore.admin")
                    || !sender.hasPermission("livecore.platforms")) {

                sender.sendMessage(addColor.Set(
                        plugin.messages().getString("messages.no-permission")
                ));
                return true;
            }

            ConfigurationSection platforms =
                    plugin.getConfig().getConfigurationSection("platforms");

            if (platforms == null) return true;

            for (String line : plugin.messages().getStringList("messages.platforms")) {

                if (!line.contains("%avaible_platforms_name%")) {
                    sender.sendMessage(addColor.Set(line));
                    continue;
                }

                for (String platformId : platforms.getKeys(false)) {

                    ConfigurationSection platform =
                            platforms.getConfigurationSection(platformId);
                    if (platform == null) continue;

                    String domain = platform.getString("domain", "unknown");
                    String perm = platform.getString("permission", "none");

                    sender.sendMessage(addColor.Set(
                            line
                                    .replace("%avaible_platforms_name%", platformId)
                                    .replace("%avaible_platforms_domain%", domain)
                                    .replace("%platforms_permission%", perm)
                    ));
                }
            }
            return true;
        }

        return true;
    }
}