package com.github.nautic.bungeecord;

import com.github.nautic.core.CoreLogger;
import com.github.nautic.core.platform.PlatformAdapter;

public final class LiveCoreBungeecord implements PlatformAdapter {

    @Override
    public void load() {
        CoreLogger.info("Initializing BungeeCord adapter");
    }

    @Override
    public void enable() {
        CoreLogger.info("BungeeCord proxy environment ready");
    }

    @Override
    public void disable() {
        CoreLogger.info("BungeeCord adapter shutdown completed");
    }
}
