package com.github.nautic.spigot.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;

public class ReflectionAPI {

    private static String getServerVersion() {
        String name = Bukkit.getServer().getClass().getPackage().getName();
        return name.substring(name.lastIndexOf('.') + 1);
    }

    public static Class<?> getNMSClass(String name) throws ClassNotFoundException {
        String version = getServerVersion();
        return Class.forName("net.minecraft.server." + version + "." + name);
    }

    public static Class<?> getCraftBukkitClass(String name) throws ClassNotFoundException {
        String version = getServerVersion();
        return Class.forName("org.bukkit.craftbukkit." + version + "." + name);
    }

    public static Object getHandle(Player player) throws Exception {
        // Usamos reflexión para obtener el handle NMS de un jugador CraftPlayer
        Method getHandle = getCraftBukkitClass("entity.CraftPlayer").getMethod("getHandle");
        return getHandle.invoke(player);
    }

    public static void sendPacket(Player player, Object packet) throws Exception {
        Object handle = getHandle(player);
        Object connection = handle.getClass().getField("playerConnection").get(handle);

        Method sendPacket = connection.getClass().getMethod("sendPacket", getNMSClass("Packet"));
        sendPacket.invoke(connection, packet);
    }
}