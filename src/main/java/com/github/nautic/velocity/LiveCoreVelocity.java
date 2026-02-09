package com.github.nautic.velocity;

import com.github.nautic.core.CoreLogger;
import com.github.nautic.core.platform.PlatformAdapter;

public final class LiveCoreVelocity implements PlatformAdapter {

    @Override
    public void load() {
        CoreLogger.info("Initializing Velocity adapter");
    }

    @Override
    public void enable() {
        CoreLogger.info("Velocity proxy environment ready");
    }

    @Override
    public void disable() {
        CoreLogger.info("Velocity adapter shutdown completed");
    }
}
