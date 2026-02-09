package com.github.nautic.spigot.utils;

import org.bukkit.entity.Player;

public class TitleAPI {

    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        if (player == null || (!player.isOnline())) return;

        try {
            player.sendTitle(title != null ? title : "", subtitle != null ? subtitle : "", fadeIn, stay, fadeOut);
        } catch (NoSuchMethodError ignored) {
            sendLegacyTitle(player, title, subtitle, fadeIn, stay, fadeOut);
        }
    }

    private static void sendLegacyTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        try {
            Object handle = ReflectionAPI.getHandle(player);
            Object connection = handle.getClass().getField("playerConnection").get(handle);

            Class<?> packetPlayOutTitle = ReflectionAPI.getNMSClass("PacketPlayOutTitle");
            Class<?> iChatBaseComponent = ReflectionAPI.getNMSClass("IChatBaseComponent");
            Class<?> chatComponentText = ReflectionAPI.getNMSClass("ChatComponentText");
            Class<?> enumTitleAction = packetPlayOutTitle.getDeclaredClasses()[0];

            Object titleComponent = chatComponentText.getConstructor(String.class).newInstance(title != null ? title : "");
            Object subtitleComponent = chatComponentText.getConstructor(String.class).newInstance(subtitle != null ? subtitle : "");

            Object timingPacket = packetPlayOutTitle.getConstructor(int.class, int.class, int.class)
                    .newInstance(fadeIn, stay, fadeOut);

            Object titlePacket = packetPlayOutTitle.getConstructor(enumTitleAction, iChatBaseComponent)
                    .newInstance(Enum.valueOf((Class<Enum>) enumTitleAction, "TITLE"), titleComponent);
            Object subtitlePacket = packetPlayOutTitle.getConstructor(enumTitleAction, iChatBaseComponent)
                    .newInstance(Enum.valueOf((Class<Enum>) enumTitleAction, "SUBTITLE"), subtitleComponent);

            ReflectionAPI.sendPacket(player, timingPacket);
            ReflectionAPI.sendPacket(player, titlePacket);
            ReflectionAPI.sendPacket(player, subtitlePacket);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}