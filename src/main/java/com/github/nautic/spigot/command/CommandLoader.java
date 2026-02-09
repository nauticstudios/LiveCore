package com.github.nautic.spigot.command;

import com.github.nautic.spigot.LiveCoreSpigotPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

public final class CommandLoader {

    public static void load(LiveCoreSpigotPlugin plugin) {

        try {
            Field field = Bukkit.getServer()
                    .getClass()
                    .getDeclaredField("commandMap");
            field.setAccessible(true);

            CommandMap commandMap = (CommandMap) field.get(Bukkit.getServer());

            register("livecore", plugin, new LiveCoreCommand(plugin), commandMap);

            List<String> aliases =
                    plugin.getConfig().getStringList("commands.aliases");

            for (String alias : aliases) {
                register(alias, plugin, new LiveCommand(plugin), commandMap);
            }

        } catch (Exception e) {
            plugin.getLogger().severe("Failed to load commands");
            e.printStackTrace();
        }
    }

    private static void register(
            String name,
            LiveCoreSpigotPlugin plugin,
            Object executor,
            CommandMap map
    ) {
        try {
            Constructor<PluginCommand> c =
                    PluginCommand.class.getDeclaredConstructor(
                            String.class,
                            org.bukkit.plugin.Plugin.class
                    );
            c.setAccessible(true);

            PluginCommand cmd = c.newInstance(name, plugin);
            cmd.setExecutor((org.bukkit.command.CommandExecutor) executor);
            map.register(plugin.getName(), cmd);

        } catch (Exception ignored) {}
    }
}
