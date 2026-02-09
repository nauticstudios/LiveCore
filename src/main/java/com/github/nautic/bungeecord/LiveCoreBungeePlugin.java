package com.github.nautic.bungeecord;

import com.github.nautic.bungeecord.command.CommandLoader;
import com.github.nautic.core.LiveCore;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.bstats.bungeecord.Metrics;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public final class LiveCoreBungeePlugin extends Plugin {

    private static LiveCoreBungeePlugin instance;

    private Configuration config;
    private Configuration messages;

    private File configFile;
    private File messagesFile;

    @Override
    public void onEnable() {
        instance = this;

        int pluginId = 29434;
        Metrics metrics = new Metrics(this, pluginId);

        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        loadConfigFile();
        loadMessagesFile();

        LiveCore.start();
        CommandLoader.load(this);

        getLogger().info("LiveCore Bungee enabled successfully.");
    }

    @Override
    public void onDisable() {
        LiveCore.stop();
        getLogger().info("LiveCore Bungee disabled.");
    }


    private void loadConfigFile() {
        configFile = new File(getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            saveResource("config.yml");
        }

        reloadConfig();
    }

    public void reloadConfig() {
        try {
            config = ConfigurationProvider
                    .getProvider(YamlConfiguration.class)
                    .load(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Configuration getConfig() {
        return config;
    }

    private void loadMessagesFile() {
        messagesFile = new File(getDataFolder(), "messages.yml");

        if (!messagesFile.exists()) {
            saveResource("messages.yml");
        }

        reloadMessages();
    }

    public void reloadMessages() {
        try {
            messages = ConfigurationProvider
                    .getProvider(YamlConfiguration.class)
                    .load(messagesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Configuration messages() {
        return messages;
    }

    private void saveResource(String name) {
        File out = new File(getDataFolder(), name);
        if (out.exists()) return;

        try (InputStream in = getResourceAsStream(name)) {
            if (in == null) {
                getLogger().warning("Resource not found: " + name);
                return;
            }
            Files.copy(in, out.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static LiveCoreBungeePlugin get() {
        return instance;
    }
}
