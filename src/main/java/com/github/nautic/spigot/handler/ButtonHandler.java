package com.github.nautic.spigot.handler;

import com.github.nautic.core.utils.addColor;
import com.github.nautic.spigot.LiveCoreSpigotPlugin;
import net.md_5.bungee.api.chat.*;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public final class ButtonHandler {

    public static TextComponent build(
            LiveCoreSpigotPlugin plugin,
            String platformId,
            String url
    ) {

        ConfigurationSection btn =
                plugin.messages().getConfigurationSection("buttons." + platformId);

        if (btn == null) return new TextComponent("");

        String textRaw = btn.getString("text", "");
        String textColored = addColor.Set(textRaw);

        TextComponent button = new TextComponent(
                TextComponent.fromLegacyText(textColored)
        );

        List<String> hoverLines = btn.getStringList("hover");
        if (!hoverLines.isEmpty()) {
            String hoverText = String.join("\n", hoverLines);
            button.setHoverEvent(new HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    TextComponent.fromLegacyText(addColor.Set(hoverText))
            ));
        }

        ConfigurationSection click = btn.getConfigurationSection("click");
        if (click != null) {
            String type = click.getString("type", "").toUpperCase();
            String value = click.getString("value", "").replace("%url%", url);

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
