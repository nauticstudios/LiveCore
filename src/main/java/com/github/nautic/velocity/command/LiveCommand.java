package com.github.nautic.velocity.command;

import com.github.nautic.core.utils.URLUtil;
import com.github.nautic.velocity.LiveCoreVelocityPlugin;
import com.github.nautic.velocity.handler.BroadcastHandler;
import com.github.nautic.velocity.manager.CooldownManager;
import com.github.nautic.velocity.utils.CenterVelocityUtils;
import com.github.nautic.velocity.utils.YamlUtil;
import com.github.nautic.velocity.utils.addColorVelocity;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class LiveCommand implements SimpleCommand {

    private final LiveCoreVelocityPlugin plugin;
    private final String alias;

    public LiveCommand(LiveCoreVelocityPlugin plugin, String alias) {
        this.plugin = plugin;
        this.alias = alias;
    }

    @Override
    public void execute(Invocation invocation) {

        if (!(invocation.source() instanceof Player player)) return;

        Map<String, Object> config = plugin.getConfig();

        Map<String, Object> commands =
                YamlUtil.getSection(config, "commands");

        String globalPerm =
                commands != null
                        ? YamlUtil.getString(commands, "permission")
                        : null;

        if (globalPerm != null && !player.hasPermission(globalPerm)) {
            send(player, "no-permission");
            return;
        }

        String[] args = invocation.arguments();
        if (args.length == 0) {
            sendSyntax(player);
            return;
        }

        String normalizedUrl = URLUtil.normalize(args[0]);
        String inputDomain = URLUtil.extractDomain(normalizedUrl);

        Map<String, Object> platforms =
                YamlUtil.getSection(config, "platforms");

        if (platforms == null) {
            sendSyntax(player);
            return;
        }

        for (String platformId : platforms.keySet()) {

            Map<String, Object> platform =
                    YamlUtil.getSection(platforms, platformId);
            if (platform == null) continue;

            String domain = YamlUtil.getString(platform, "domain");
            if (domain == null) continue;

            if (!inputDomain.endsWith(domain.toLowerCase())) continue;

            String perm = YamlUtil.getString(platform, "permission");
            if (perm != null && !player.hasPermission(perm)) {
                send(player,
                        "platforms-no-permission",
                        "%platform_permission%",
                        perm
                );
                return;
            }

            Map<String, Object> perms =
                    YamlUtil.getSection(config, "permissions");

            String bypass =
                    perms != null
                            ? YamlUtil.getString(perms, "bypass-cooldown")
                            : null;

            int cooldown =
                    YamlUtil.getInt(platform, "cooldown", 0);

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
                            "cooldown",
                            "%cooldown%",
                            String.valueOf(remaining)
                    );
                    return;
                }
            }

            new BroadcastHandler(plugin).broadcast(
                    player,
                    player.getUsername(),
                    platformId,
                    normalizedUrl
            );
            return;
        }

        sendSyntax(player);
    }

    private void sendSyntax(Player player) {

        Map<String, Object> syntax =
                YamlUtil.getSection(plugin.messages(), "syntax");

        Map<String, Object> aliases =
                syntax != null
                        ? YamlUtil.getSection(syntax, "aliases")
                        : null;

        List<String> lines =
                aliases != null
                        ? YamlUtil.getStringList(aliases, "error")
                        : null;

        if (lines == null) return;

        String platforms = getPlatformsAvailable();

        String msg = lines.stream()
                .map(l ->
                        CenterVelocityUtils.centerMessage(
                                l.replace("%aliases%", alias)
                                        .replace("%domain%", "<domain>")
                                        .replace("%platforms_available%", platforms)
                        )
                )
                .collect(Collectors.joining("\n"));

        player.sendMessage(addColorVelocity.Set(msg));
    }

    private String getPlatformsAvailable() {

        Map<String, Object> platforms =
                YamlUtil.getSection(plugin.getConfig(), "platforms");

        return platforms == null
                ? ""
                : String.join(", ", platforms.keySet());
    }

    private void send(Player player, String key) {

        Map<String, Object> messages =
                YamlUtil.getSection(plugin.messages(), "messages");

        String msg =
                messages != null
                        ? YamlUtil.getString(messages, key)
                        : null;

        if (msg != null && !msg.isEmpty()) {
            player.sendMessage(addColorVelocity.Set(msg));
        }
    }

    private void send(Player player, String key, String ph, String val) {

        Map<String, Object> messages =
                YamlUtil.getSection(plugin.messages(), "messages");

        String msg =
                messages != null
                        ? YamlUtil.getString(messages, key)
                        : null;

        if (msg != null && !msg.isEmpty()) {
            player.sendMessage(
                    addColorVelocity.Set(msg.replace(ph, val))
            );
        }
    }
}
