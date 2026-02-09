package com.github.nautic.bungeecord.utils;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public final class TitlesAPI {

    private TitlesAPI() {
    }

    public static void send(
            ProxiedPlayer player,
            String title,
            String subtitle,
            int fadeIn,
            int stay,
            int fadeOut
    ) {
        if (player == null) return;

        Title t = ProxyServer.getInstance()
                .createTitle()
                .reset()
                .fadeIn(fadeIn)
                .stay(stay)
                .fadeOut(fadeOut);

        if (title != null && !title.isEmpty()) {
            t.title(TextComponent.fromLegacyText(
                    addColorBungee.Set(title)
            ));
        }

        if (subtitle != null && !subtitle.isEmpty()) {
            t.subTitle(TextComponent.fromLegacyText(
                    addColorBungee.Set(subtitle)
            ));
        }

        t.send(player);
    }

    public static void clear(ProxiedPlayer player) {
        if (player == null) return;

        ProxyServer.getInstance()
                .createTitle()
                .clear()
                .send(player);
    }
}
