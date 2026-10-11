package com.jjkmod.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class JJKModClient implements ClientModInitializer {
    private static final String CATEGORY = "category.jjkmod";

    @Override
    public void onInitializeClient() {
        KeyBinding keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.jjk.menu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_J,
                CATEGORY
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {
                client.setScreen(new SorcererSelectionScreen());
            }
        });
    }
}
