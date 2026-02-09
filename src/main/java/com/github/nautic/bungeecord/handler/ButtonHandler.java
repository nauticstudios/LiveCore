package com.github.nautic.bungeecord.handler;

import com.github.nautic.bungeecord.LiveCoreBungeePlugin;
import com.github.nautic.bungeecord.utils.addColorBungee;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.config.Configuration;

import java.util.List;

public final class ButtonHandler {

    public static TextComponent build(
            LiveCoreBungeePlugin plugin,
            String platformId,
            String url
    ) {

        Configuration btn =
                plugin.messages().getSection("buttons." + platformId);

        if (btn == null) return new TextComponent("");

        String textRaw = btn.getString("text", "");
        String textColored = addColorBungee.Set(textRaw);

        TextComponent button = new TextComponent(
                TextComponent.fromLegacyText(textColored)
        );

        List<String> hoverLines = btn.getStringList("hover");
        if (!hoverLines.isEmpty()) {
            String hoverText = String.join("\n", hoverLines);
            button.setHoverEvent(new HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    TextComponent.fromLegacyText(addColorBungee.Set(hoverText))
            ));
        }

        Configuration click = btn.getSection("click");
        if (click != null) {
            String type = click.getString("type", "").toUpperCase();
            String value = click.getString("value", "")
                    .replace("%url%", url);

            if (type.equals("URL")) {
                button.setClickEvent(new ClickEvent(
                        ClickEvent.Action.OPEN_URL,
                        value
                ));
            }
        }

        button.setInsertion(textColored);

        return button;
    }
}
