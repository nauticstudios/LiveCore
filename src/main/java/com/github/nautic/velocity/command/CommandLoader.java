package com.github.nautic.velocity.command;

import com.github.nautic.velocity.LiveCoreVelocityPlugin;
import com.github.nautic.velocity.utils.YamlUtil;
import com.velocitypowered.api.command.CommandManager;

import java.util.List;
import java.util.Map;

public final class CommandLoader {

    private CommandLoader() {}

    @SuppressWarnings("unchecked")
    public static void load(LiveCoreVelocityPlugin plugin) {

        CommandManager manager = plugin.getServer().getCommandManager();

        manager.unregister("livecore");

        Map<String, Object> config = plugin.getConfig();
        Map<String, Object> commands = YamlUtil.getSection(config, "commands");

        manager.register(
                manager.metaBuilder("livecore").build(),
                new LiveCoreCommand(plugin)
        );

        if (commands == null) return;

        List<String> aliases = YamlUtil.getStringList(commands, "aliases");
        if (aliases == null) return;

        for (String alias : aliases) {
            manager.unregister(alias);
            manager.register(
                    manager.metaBuilder(alias).build(),
                    new LiveCommand(plugin, alias)
            );
        }
    }
}
