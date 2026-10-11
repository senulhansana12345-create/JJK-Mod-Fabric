package com.jjkmod;

import com.jjkmod.command.JJKCommand;
import com.jjkmod.event.PlayerTickEvent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JJKModMain implements ModInitializer {
    public static final String MOD_ID = "jjkmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        JJKRegistries.registerDefaults();
        JJKCommand.register();

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            server.getPlayerManager().getPlayerList().forEach(PlayerTickEvent::onServerTick);
        });

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            LOGGER.info("JJK Premium Mod is running on Fabric 1.21.1");
        });

        LOGGER.info("JJK Premium Mod loaded successfully.");
    }
}
