package com.github.nautic.velocity.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class addColorVelocity {

    private static final Pattern HEX_PATTERN =
            Pattern.compile("&#([A-Fa-f0-9]{6})");

    private static final LegacyComponentSerializer LEGACY =
            LegacyComponentSerializer.legacySection();

    private addColorVelocity() {}

    public static Component Set(String text) {
        if (text == null || text.isEmpty()) {
            return Component.empty();
        }

        text = translateHex(text);

        text = text.replace('&', '§');

        return LEGACY.deserialize(text);
    }

    private static String translateHex(String text) {
        Matcher matcher = HEX_PATTERN.matcher(text);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            String hex = matcher.group(1);
            String replacement = "§x§" +
                    hex.charAt(0) + "§" +
                    hex.charAt(1) + "§" +
                    hex.charAt(2) + "§" +
                    hex.charAt(3) + "§" +
                    hex.charAt(4) + "§" +
                    hex.charAt(5);

            matcher.appendReplacement(buffer, replacement);
        }

        matcher.appendTail(buffer);
        return buffer.toString();
    }
}
