package com.github.nautic.spigot;

import com.github.nautic.core.LiveCore;
import com.github.nautic.spigot.command.CommandLoader;
import com.github.nautic.spigot.update.spigot.SpigotUpdateListener;
import org.bstats.bukkit.Metrics;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class LiveCoreSpigotPlugin extends JavaPlugin {

    private static LiveCoreSpigotPlugin instance;
    private FileConfiguration messages;
    private File messagesFile;

    @Override
    public void onEnable() {
        instance = this;

        int pluginId = 29436;
        Metrics metrics = new Metrics(this, pluginId);

        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        loadConfigFile();
        loadMessagesFile();

        CommandLoader.load(this);
        LiveCore.start();

        new SpigotUpdateListener(this, 132482);

        getLogger().info("LiveCore enabled successfully.");
    }

    @Override
    public void onDisable() {
        getLogger().info("LiveCore disabled.");
    }

    private void loadConfigFile() {
        File configFile = new File(getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            saveResource("config.yml", false);
        }

        reloadConfig();
    }

    private void loadMessagesFile() {
        messagesFile = new File(getDataFolder(), "messages.yml");

        if (!messagesFile.exists()) {
            saveResource("messages.yml", false);
        }

        messages = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public void reloadMessages() {
        if (messagesFile == null) {
            messagesFile = new File(getDataFolder(), "messages.yml");
        }
        messages = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public static LiveCoreSpigotPlugin get() {
        return instance;
    }

    public FileConfiguration messages() {
        return messages;
    }
}