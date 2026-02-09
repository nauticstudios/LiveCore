package com.github.nautic.bungeecord.handler;

import com.github.nautic.bungeecord.LiveCoreBungeePlugin;
import com.github.nautic.bungeecord.utils.CenterBungeeUtils;
import com.github.nautic.bungeecord.utils.TitlesAPI;
import com.github.nautic.bungeecord.utils.addColorBungee;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;

public class BroadcastHandler {

    private final LiveCoreBungeePlugin plugin;

    public BroadcastHandler(LiveCoreBungeePlugin plugin) {
        this.plugin = plugin;
    }

    public void broadcast(ProxiedPlayer executor, String platformId, String url) {

        Configuration adv =
                plugin.messages().getSection("advertisement." + platformId);

        if (adv == null || !adv.getBoolean("enabled")) return;

        Configuration chat = adv.getSection("broadcast.chat");
        if (chat != null && chat.getBoolean("enabled")) {

            for (String line : chat.getStringList("lines")) {

                if (line.equalsIgnoreCase("<empty>")) {
                    plugin.getProxy().getPlayers()
                            .forEach(p -> p.sendMessage(""));
                    continue;
                }

                if (line.contains("%button_" + platformId + "%")) {

                    TextComponent button =
                            ButtonHandler.build(plugin, platformId, url);

                    String visibleText = button.getInsertion();
                    String padding =
                            CenterBungeeUtils.centerComponentByText(visibleText);

                    TextComponent lineComponent = new TextComponent(padding);
                    lineComponent.addExtra(button);

                    plugin.getProxy().getPlayers()
                            .forEach(p -> p.sendMessage(lineComponent));
                    continue;
                }

                String out = line.replace(
                        "%player%",
                        executor.getName()
                );

                String finalText =
                        CenterBungeeUtils.centerMessage(addColorBungee.Set(out));

                plugin.getProxy().getPlayers()
                        .forEach(p -> p.sendMessage(finalText));
            }
        }

        Configuration title = adv.getSection("broadcast.title");
        if (title != null && title.getBoolean("enabled")) {

            String up = title.getString("up");
            String down = title.getString("down");

            plugin.getProxy().getPlayers().forEach(p ->
                    TitlesAPI.send(
                            p,
                            up,
                            down != null
                                    ? down.replace("%player%", executor.getName())
                                    : null,
                            10, 60, 10
                    )
            );
        }
    }
}
