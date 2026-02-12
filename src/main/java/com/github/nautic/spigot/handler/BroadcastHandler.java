package com.github.nautic.spigot.handler;

import com.github.nautic.core.utils.CenterUtils;
import com.github.nautic.core.utils.addColor;
import com.github.nautic.spigot.LiveCoreSpigotPlugin;
import com.github.nautic.spigot.utils.PlayerUtil;
import com.github.nautic.spigot.utils.SoundUtil;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;

public class BroadcastHandler {

    private final LiveCoreSpigotPlugin plugin;

    public BroadcastHandler(LiveCoreSpigotPlugin plugin) {
        this.plugin = plugin;
    }

    public void broadcast(Player executor, String platformId, String url) {

        ConfigurationSection adv =
                plugin.messages().getConfigurationSection("advertisement." + platformId);

        if (adv == null || !adv.getBoolean("enabled")) return;

        final String playerName = executor.getName();

        ConfigurationSection chat = adv.getConfigurationSection("broadcast.chat");
        if (chat != null && chat.getBoolean("enabled")) {

            List<String> lines = chat.getStringList("lines");

            for (String line : lines) {

                if (line.equalsIgnoreCase("<empty>")) {
                    PlayerUtil.forEachOnline(p -> p.sendMessage(""));
                    continue;
                }

                if (line.contains("%button_" + platformId + "%")) {

                    TextComponent button =
                            (TextComponent) ButtonHandler.build(plugin, platformId, url);

                    String visibleText = button.getInsertion();
                    String padding = CenterUtils.centerComponentByText(visibleText);

                    TextComponent lineComponent = new TextComponent(padding);
                    lineComponent.addExtra(button);

                    PlayerUtil.forEachOnline(p -> p.spigot().sendMessage(lineComponent));
                    continue;
                }

                String replaced = line.replace("%player%", playerName);
                String colored = addColor.Set(replaced);
                String centered = CenterUtils.centerMessage(colored);

                PlayerUtil.forEachOnline(p -> p.sendMessage(centered));
            }
        }

        ConfigurationSection sound = adv.getConfigurationSection("broadcast.sound");
        if (sound != null && sound.getBoolean("enabled")) {
            SoundUtil.playAll(sound.getString("id"));
        }

        ConfigurationSection title = adv.getConfigurationSection("broadcast.title");
        if (title != null && title.getBoolean("enabled")) {

            String up = addColor.Set(title.getString("up", ""));
            String down = addColor.Set(
                    title.getString("down", "").replace("%player%", playerName)
            );

            PlayerUtil.forEachOnline(p ->
                    p.sendTitle(up, down, 10, 60, 10)
            );
        }
    }
}