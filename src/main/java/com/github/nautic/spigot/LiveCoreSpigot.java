package com.github.nautic.spigot;

import com.github.nautic.core.CoreLogger;
import com.github.nautic.core.platform.PlatformAdapter;

public final class LiveCoreSpigot implements PlatformAdapter {

    @Override
    public void load() {
        CoreLogger.info("Initializing Spigot adapter");
    }

    @Override
    public void enable() {
        CoreLogger.info("Spigot environment ready");
    }

    @Override
    public void disable() {
        CoreLogger.info("Spigot adapter shutdown completed");
    }
}
