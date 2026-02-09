package com.github.nautic.bungeecord.command;

import com.github.nautic.bungeecord.LiveCoreBungeePlugin;

import java.util.List;

public final class CommandLoader {

    private CommandLoader() {}

    public static void load(LiveCoreBungeePlugin plugin) {

        plugin.getProxy().getPluginManager()
                .registerCommand(plugin, new LiveCoreCommand(plugin));

        List<String> aliases =
                plugin.getConfig().getStringList("commands.aliases");

        for (String alias : aliases) {
            plugin.getProxy().getPluginManager()
                    .registerCommand(plugin, new LiveCommand(plugin, alias));
        }
    }
}