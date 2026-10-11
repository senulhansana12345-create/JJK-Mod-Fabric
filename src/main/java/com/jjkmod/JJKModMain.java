package com.jjkmod;

import com.jjkmod.command.JJKCommand;
import com.jjkmod.technique.JJKRegistries;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JJKModMain implements ModInitializer {
    public static final String MOD_ID = "jjkmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        JJKRegistries.registerDefaults();
        JJKCommand.register();

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            LOGGER.info("JJK Premium Mod is running on Fabric 1.21.1");
        });

        LOGGER.info("JJK Premium Mod loaded successfully.");
    }
}
