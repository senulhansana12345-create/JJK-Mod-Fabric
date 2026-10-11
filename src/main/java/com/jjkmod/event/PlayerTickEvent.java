package com.jjkmod.event;

import com.jjkmod.JJKPlayerData;
import com.jjkmod.JJKProfile;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerTickEvent {
    public static void onServerTick(ServerPlayerEntity player) {
        JJKProfile profile = JJKPlayerData.get(player);

        // Regenerate cursed energy over time
        if (profile.getCursedEnergy() < profile.getMaxCursedEnergy()) {
            profile.setCursedEnergy(Math.min(profile.getCursedEnergy() + 1, profile.getMaxCursedEnergy()));
            JJKPlayerData.save(player, profile);
        }

        // Gain experience from combat
        profile.addExperience(1);
        if (profile.getExperience() >= 1000 && profile.getLevel() < 100) {
            profile.setExperience(0);
            profile.setLevel(profile.getLevel() + 1);
            profile.setMaxCursedEnergy(100 + (profile.getLevel() * 10));
            profile.setPower(10 + (profile.getLevel() / 2));
            profile.setSpeed(10 + (profile.getLevel() / 3));
            profile.setControl(10 + (profile.getLevel() / 2));
            JJKPlayerData.save(player, profile);
        }
    }
}
