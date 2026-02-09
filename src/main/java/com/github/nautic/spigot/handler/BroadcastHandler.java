package com.github.nautic.spigot.handler;

import com.github.nautic.core.utils.CenterUtils;
import com.github.nautic.core.utils.addColor;
import com.github.nautic.spigot.LiveCoreSpigotPlugin;
import com.github.nautic.spigot.utils.SoundUtil;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class BroadcastHandler {

    private final LiveCoreSpigotPlugin plugin;

    public BroadcastHandler(LiveCoreSpigotPlugin plugin) {
        this.plugin = plugin;
    }

    public void broadcast(Player executor, String platformId, String url) {

        ConfigurationSection adv =
                plugin.messages().getConfigurationSection("advertisement." + platformId);

        if (adv == null || !adv.getBoolean("enabled")) return;

        ConfigurationSection chat = adv.getConfigurationSection("broadcast.chat");
        if (chat != null && chat.getBoolean("enabled")) {

            for (String line : chat.getStringList("lines")) {

                if (line.equalsIgnoreCase("<empty>")) {
                    Bukkit.getOnlinePlayers()
                            .forEach(p -> p.sendMessage(""));
                    continue;
                }

                if (line.contains("%button_" + platformId + "%")) {

                    TextComponent button =
                            (TextComponent) ButtonHandler.build(plugin, platformId, url);

                    String visibleText = button.getInsertion();

                    String padding =
                            CenterUtils.centerComponentByText(visibleText);

                    TextComponent lineComponent = new TextComponent(padding);
                    lineComponent.addExtra(button);

                    Bukkit.getOnlinePlayers()
                            .forEach(p -> p.spigot().sendMessage(lineComponent));

                    continue;
                }

                String out = line
                        .replace("%player%", executor.getName());

                String finalText =
                        CenterUtils.centerMessage(addColor.Set(out));

                Bukkit.getOnlinePlayers()
                        .forEach(p -> p.sendMessage(finalText));
            }
        }

        ConfigurationSection sound = adv.getConfigurationSection("broadcast.sound");
        if (sound != null && sound.getBoolean("enabled")) {
            SoundUtil.playAll(sound.getString("id"));
        }

        ConfigurationSection title = adv.getConfigurationSection("broadcast.title");
        if (title != null && title.getBoolean("enabled")) {
            Bukkit.getOnlinePlayers().forEach(p ->
                    p.sendTitle(
                            addColor.Set(title.getString("up")),
                            addColor.Set(
                                    title.getString("down")
                                            .replace("%player%", executor.getName())
                            ),
                            10, 60, 10
                    )
            );
        }
    }
}
