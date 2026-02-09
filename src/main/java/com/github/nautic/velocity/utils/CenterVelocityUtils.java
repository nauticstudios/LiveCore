package com.github.nautic.velocity.utils;

import com.github.nautic.core.utils.DefaultFontInfo;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public final class CenterVelocityUtils {

    private static final int CENTER_PX = 154;

    private CenterVelocityUtils() {
    }

    public static String centerMessage(String message) {
        return centerMessage(message, true, false);
    }

    public static String centerMessage(
            String message,
            boolean applyColors,
            boolean onlyCompensate
    ) {
        if (message == null || message.trim().isEmpty()) return "";

        boolean containsTags =
                message.contains("<center>") &&
                        message.contains("</center>");

        String content = containsTags
                ? message.replaceAll("(?i)</center>", "")
                .replaceAll("(?i)<center>", "")
                : message;

        if (applyColors) {
            content = translateHex(content);
        }

        String visibleText = stripTags(content);

        int messagePxSize = calculatePixelLength(visibleText);
        int paddingSize = CENTER_PX - (messagePxSize / 2);

        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int spaces = Math.max(0, paddingSize / spaceLength);

        String padding = " ".repeat(spaces);
        return onlyCompensate ? padding : padding + content;
    }

    private static int calculatePixelLength(String text) {
        int length = 0;
        boolean isBold = false;
        boolean colorCode = false;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (c == '§' || c == '&') {
                colorCode = true;
                continue;
            }

            if (colorCode) {
                colorCode = false;
                isBold = (c == 'l' || c == 'L');
                continue;
            }

            DefaultFontInfo dFI =
                    DefaultFontInfo.getDefaultFontInfo(c);

            int charWidth = dFI != null
                    ? (isBold ? dFI.getBoldLength() : dFI.getLength())
                    : estimateCharWidth(c);

            length += charWidth + 1;
        }

        return length;
    }

    private static String stripTags(String message) {
        return message
                .replaceAll("<[^>]+>", "")
                .replaceAll("&#[a-fA-F0-9]{6}", "")
                .replaceAll("&[0-9a-fk-orA-FK-OR]", "")
                .replaceAll("§[0-9a-fk-orA-FK-OR]", "");
    }

    private static int estimateCharWidth(char c) {
        int codePoint = c;
        if (codePoint >= 0x1F300 && codePoint <= 0x1FAFF) {
            return 10;
        }
        if (!Character.isLetterOrDigit(c)) {
            return 5;
        }
        return 4;
    }

    public static String centerComponent(Component component) {
        if (component == null) return "";

        String visibleText =
                PlainTextComponentSerializer.plainText()
                        .serialize(component);

        return centerComponentByText(visibleText);
    }


    public static String centerComponentByText(String visibleText) {
        if (visibleText == null || visibleText.isEmpty()) return "";

        String stripped = stripTags(visibleText);

        int messagePxSize = calculatePixelLength(stripped);
        int paddingSize = CENTER_PX - (messagePxSize / 2);

        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int spaces = Math.max(0, paddingSize / spaceLength);

        return " ".repeat(spaces);
    }

    private static String translateHex(String text) {
        return text.replaceAll(
                "&#([A-Fa-f0-9]{6})",
                "§x§$1§$2§$3§$4§$5§$6"
        );
    }
}
