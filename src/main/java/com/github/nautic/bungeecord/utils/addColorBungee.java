package com.github.nautic.bungeecord.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class addColorBungee {

    private static final Pattern HEX_PATTERN =
            Pattern.compile("&#([A-Fa-f0-9]{6})");

    private addColorBungee() {
    }

    public static String Set(String text) {
        if (text == null) return "";

        text = translateHex(text);
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static TextComponent component(String text) {
        return new TextComponent(TextComponent.fromLegacyText(Set(text)));
    }

    private static String translateHex(String text) {
        Matcher matcher = HEX_PATTERN.matcher(text);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            String hex = matcher.group(1);
            ChatColor color = ChatColor.of("#" + hex);
            matcher.appendReplacement(buffer, color.toString());
        }

        matcher.appendTail(buffer);
        return buffer.toString();
    }
}
