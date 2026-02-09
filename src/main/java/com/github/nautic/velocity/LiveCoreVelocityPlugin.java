package com.github.nautic.velocity;

import com.github.nautic.core.LiveCore;
import com.github.nautic.velocity.command.CommandLoader;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.bstats.velocity.Metrics;
import org.slf4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@Plugin(
        id = "livecore",
        name = "LiveCore",
        version = "1.0.0"
)
public final class LiveCoreVelocityPlugin {

    private static LiveCoreVelocityPlugin instance;

    private final ProxyServer server;
    private final Logger logger;
    private final Metrics.Factory metricsFactory;
    private final Path dataFolder;

    private Map<String, Object> config;
    private Map<String, Object> messages;

    @Inject
    public LiveCoreVelocityPlugin(
            ProxyServer server,
            Logger logger,
            Metrics.Factory metricsFactory,
            @DataDirectory Path dataFolder
    ) {
        this.server = server;
        this.logger = logger;
        this.metricsFactory = metricsFactory;
        this.dataFolder = dataFolder;
    }

    @Subscribe
    public void onProxyInit(ProxyInitializeEvent event) {
        instance = this;

        try {
            Files.createDirectories(dataFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }

        loadConfig();
        loadMessages();

        CommandLoader.load(this);

        int pluginId = 29435;
        Metrics metrics = metricsFactory.make(this, pluginId);

        LiveCore.start();

        logger.info("LiveCore Velocity enabled successfully.");
    }


    private void loadConfig() {
        Path configFile = dataFolder.resolve("config.yml");

        if (!Files.exists(configFile)) {
            saveResource("config.yml");
        }

        config = loadYaml(configFile);
    }

    private void loadMessages() {
        Path messagesFile = dataFolder.resolve("messages.yml");

        if (!Files.exists(messagesFile)) {
            saveResource("messages.yml");
        }

        messages = loadYaml(messagesFile);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> loadYaml(Path file) {
        try (InputStream in = Files.newInputStream(file)) {
            Yaml yaml = new Yaml();
            return yaml.load(in);
        } catch (IOException e) {
            e.printStackTrace();
            return Map.of();
        }
    }

    private void saveResource(String name) {
        Path out = dataFolder.resolve(name);
        if (Files.exists(out)) return;

        try (InputStream in = getClass().getClassLoader().getResourceAsStream(name)) {
            if (in == null) {
                logger.warn("Resource not found: {}", name);
                return;
            }
            Files.copy(in, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static LiveCoreVelocityPlugin get() {
        return instance;
    }

    public Map<String, Object> getConfig() {
        return config;
    }

    public Map<String, Object> messages() {
        return messages;
    }

    public ProxyServer getServer() {
        return server;
    }

    public Logger getLogger() {
        return logger;
    }

    public void reloadConfig() {
        Path configFile = dataFolder.resolve("config.yml");
        config = loadYaml(configFile);
    }

    public void reloadMessages() {
        Path messagesFile = dataFolder.resolve("messages.yml");
        messages = loadYaml(messagesFile);
    }
}
