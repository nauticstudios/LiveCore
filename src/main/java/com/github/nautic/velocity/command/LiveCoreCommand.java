package com.github.nautic.velocity.command;

import com.github.nautic.velocity.LiveCoreVelocityPlugin;
import com.github.nautic.velocity.utils.YamlUtil;
import com.github.nautic.velocity.utils.addColorVelocity;
import com.velocitypowered.api.command.SimpleCommand;

import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class LiveCoreCommand implements SimpleCommand {

    private final LiveCoreVelocityPlugin plugin;

    public LiveCoreCommand(LiveCoreVelocityPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Invocation invocation) {

        var sender = invocation.source();
        String[] args = invocation.arguments();

        if (args.length == 0) {

            sender.sendMessage(addColorVelocity.Set("&r"));

            sender.sendMessage(addColorVelocity.Set(
                    "     &#FF3B3B&lLiveCore &#CDCDCD| &fVersion: &#38FF351.0.0"
            ));
            sender.sendMessage(addColorVelocity.Set("     &fMode: &bVelocity"));
            sender.sendMessage(addColorVelocity.Set(
                    "       &fPowered by &#3F92FFNautic Studios"
            ));

            sender.sendMessage(addColorVelocity.Set("&r"));
            return;
        }

        Map<String, Object> messages =
                YamlUtil.getSection(plugin.messages(), "messages");

        if (args[0].equalsIgnoreCase("help")) {

            List<String> help =
                    messages != null
                            ? YamlUtil.getStringList(messages, "help")
                            : null;

            if (help != null) {
                help.forEach(l ->
                        sender.sendMessage(addColorVelocity.Set(l))
                );
            }
            return;
        }

        if (args[0].equalsIgnoreCase("reload")) {

            if (!sender.hasPermission("livecore.admin")) {
                sender.sendMessage(addColorVelocity.Set(
                        messages != null
                                ? YamlUtil.getString(messages, "no-permission")
                                : "&cNo permission"
                ));
                return;
            }

            plugin.reloadConfig();
            plugin.reloadMessages();
            CommandLoader.load(plugin);

            sender.sendMessage(addColorVelocity.Set(
                    messages != null
                            ? YamlUtil.getString(messages, "reload")
                            : "&aReloaded"
            ));
            return;
        }

        if (args[0].equalsIgnoreCase("platforms")) {

            Map<String, Object> platforms =
                    YamlUtil.getSection(plugin.getConfig(), "platforms");

            List<String> lines =
                    messages != null
                            ? YamlUtil.getStringList(messages, "platforms")
                            : null;

            if (platforms == null || lines == null) return;

            for (String line : lines) {

                if (!line.contains("%avaible_platforms_name%")) {
                    sender.sendMessage(addColorVelocity.Set(line));
                    continue;
                }

                for (String id : platforms.keySet()) {

                    Map<String, Object> p =
                            YamlUtil.getSection(platforms, id);
                    if (p == null) continue;

                    String domain =
                            YamlUtil.getString(p, "domain");
                    String perm =
                            YamlUtil.getString(p, "permission");

                    sender.sendMessage(addColorVelocity.Set(
                            line.replace("%avaible_platforms_name%", id)
                                    .replace("%avaible_platforms_domain%", domain)
                                    .replace("%platforms_permission%", perm)
                    ));
                }
            }
        }
    }
}
