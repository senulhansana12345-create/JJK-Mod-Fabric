package com.jjkmod.event;

import com.jjkmod.JJKPlayerData;
import com.jjkmod.JJKProfile;
import com.jjkmod.gojo.GojoAbilityManager;
import com.jjkmod.sukuna.SukunaAbilityManager;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerTickEvent {
    public static void onServerTick(ServerPlayerEntity player) {
        JJKProfile profile = JJKPlayerData.get(player);

        // Regenerate cursed energy over time based on sorcerer
        if (profile.getCursedEnergy() < profile.getMaxCursedEnergy()) {
            int regenRate = profile.isSixEyesUnlocked() ? 3 : 1;
            if (profile.getSorcererId().equals("sukuna")) {
                regenRate = 2; // Sukuna has decent regen
            } else if (profile.getSorcererId().equals("yuji")) {
                regenRate = 1;
            } else if (profile.getSorcererId().equals("megumi")) {
                regenRate = 1;
            }
            profile.setCursedEnergy(Math.min(profile.getCursedEnergy() + regenRate, profile.getMaxCursedEnergy()));
            JJKPlayerData.save(player, profile);
        }

        // Gain experience from combat and gameplay
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

        // Sorcerer-specific ability ticking
        if (profile.getSorcererId().equals("gojo")) {
            GojoAbilityManager.tickGojo(player);
        } else if (profile.getSorcererId().equals("sukuna")) {
            SukunaAbilityManager.tickSukuna(player);
        }
    }
}
