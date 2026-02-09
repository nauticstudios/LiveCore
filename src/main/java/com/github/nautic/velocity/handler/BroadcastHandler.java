package com.github.nautic.velocity.handler;

import com.github.nautic.velocity.LiveCoreVelocityPlugin;
import com.github.nautic.velocity.utils.CenterVelocityUtils;
import com.github.nautic.velocity.utils.SoundUtil;
import com.github.nautic.velocity.utils.YamlUtil;
import com.github.nautic.velocity.utils.addColorVelocity;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class BroadcastHandler {

    private final LiveCoreVelocityPlugin plugin;

    public BroadcastHandler(LiveCoreVelocityPlugin plugin) {
        this.plugin = plugin;
    }

    public void broadcast(
            Audience executor,
            String executorName,
            String platformId,
            String url
    ) {

        Map<String, Object> messages = plugin.messages();

        Map<String, Object> advertisement =
                YamlUtil.getSection(messages, "advertisement");
        if (advertisement == null) return;

        Map<String, Object> adv =
                YamlUtil.getSection(advertisement, platformId);
        if (adv == null) return;

        if (!Boolean.TRUE.equals(adv.get("enabled"))) return;

        Map<String, Object> broadcast =
                YamlUtil.getSection(adv, "broadcast");

        Map<String, Object> chat =
                broadcast != null
                        ? YamlUtil.getSection(broadcast, "chat")
                        : null;

        if (chat != null && Boolean.TRUE.equals(chat.get("enabled"))) {

            List<String> lines =
                    YamlUtil.getStringList(chat, "lines");

            if (lines != null) {
                for (String line : lines) {

                    if ("<empty>".equalsIgnoreCase(line)) {
                        plugin.getServer().getAllPlayers()
                                .forEach(p -> p.sendMessage(Component.empty()));
                        continue;
                    }

                    if (line.contains("%button_" + platformId + "%")) {

                        Component button =
                                ButtonHandler.build(plugin, platformId, url);

                        String padding =
                                CenterVelocityUtils.centerComponent(button);

                        Component finalLine =
                                Component.text(padding).append(button);

                        plugin.getServer().getAllPlayers()
                                .forEach(p -> p.sendMessage(finalLine));
                        continue;
                    }

                    String centered =
                            CenterVelocityUtils.centerMessage(
                                    line.replace("%player%", executorName)
                            );

                    Component msg =
                            addColorVelocity.Set(centered);

                    plugin.getServer().getAllPlayers()
                            .forEach(p -> p.sendMessage(msg));
                }
            }
        }

        Map<String, Object> soundSection =
                broadcast != null
                        ? YamlUtil.getSection(broadcast, "sound")
                        : null;

        if (soundSection != null && Boolean.TRUE.equals(soundSection.get("enabled"))) {

            String rawId = YamlUtil.getString(soundSection, "id");

            Sound sound = SoundUtil.parse(plugin, rawId);
            if (sound != null) {
                plugin.getServer().getAllPlayers()
                        .forEach(p -> p.playSound(sound));
            }
        }

        Map<String, Object> title =
                broadcast != null
                        ? YamlUtil.getSection(broadcast, "title")
                        : null;

        if (title != null && Boolean.TRUE.equals(title.get("enabled"))) {

            Component up =
                    addColorVelocity.Set(
                            YamlUtil.getString(title, "up")
                    );

            Component down =
                    addColorVelocity.Set(
                            YamlUtil.getString(title, "down")
                                    .replace("%player%", executorName)
                    );

            Title t = Title.title(
                    up,
                    down,
                    Title.Times.times(
                            Duration.ofMillis(500),
                            Duration.ofMillis(3000),
                            Duration.ofMillis(500)
                    )
            );

            plugin.getServer().getAllPlayers()
                    .forEach(p -> p.showTitle(t));
        }
    }
}
