package com.jjkmod.gojo;

import com.jjkmod.JJKPlayerData;
import com.jjkmod.JJKProfile;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SixEyesSystem {
    private final ServerPlayerEntity player;
    private int activationCounter = 0;

    public SixEyesSystem(ServerPlayerEntity player) {
        this.player = player;
    }

    public void activateSixEyes() {
        JJKProfile profile = JJKPlayerData.get(player);
        if (!profile.isSixEyesUnlocked()) {
            player.sendMessage(Text.literal("\u00a7cSix Eyes not unlocked!"), false);
            return;
        }

        if (profile.getCursedEnergy() < 15) {
            player.sendMessage(Text.literal("\u00a7cNot enough cursed energy!"), false);
            return;
        }

        JJKPlayerData.consumeCursedEnergy(player, 15);
        player.sendMessage(Text.literal("\u00a76Six Eyes Perception: \u00a7aACTIVATED"), false);
        
        // Gives speed and regeneration boost
        activationCounter = 200; // 10 seconds
    }

    public void tickSixEyes() {
        if (activationCounter > 0) {
            activationCounter--;
        }
    }
}
