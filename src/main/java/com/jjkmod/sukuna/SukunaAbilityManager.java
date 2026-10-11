package com.jjkmod.sukuna;

import com.jjkmod.JJKPlayerData;
import com.jjkmod.JJKProfile;
import net.minecraft.server.network.ServerPlayerEntity;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SukunaAbilityManager {
    private static final Map<UUID, Long> SKILL_COOLDOWNS = new HashMap<>();

    public static void executeDismantleSlash(ServerPlayerEntity player) {
        if (!player.getGameProfile().getName().contains("sukuna")) {
            JJKProfile profile = JJKPlayerData.get(player);
            if (!profile.getSorcererId().equals("sukuna")) {
                return;
            }
        }
        DismantleSlash.execute(player);
    }

    public static void executeCleaveTechnique(ServerPlayerEntity player) {
        if (!player.getGameProfile().getName().contains("sukuna")) {
            JJKProfile profile = JJKPlayerData.get(player);
            if (!profile.getSorcererId().equals("sukuna")) {
                return;
            }
        }
        CleaveTechnique.execute(player);
    }

    public static void tickSukuna(ServerPlayerEntity player) {
        JJKProfile profile = JJKPlayerData.get(player);
        if (!profile.getSorcererId().equals("sukuna")) {
            return;
        }

        // Sukuna has passive energy regeneration bonus
        if (profile.getCursedEnergy() < profile.getMaxCursedEnergy()) {
            profile.setCursedEnergy(Math.min(profile.getCursedEnergy() + 2, profile.getMaxCursedEnergy()));
            JJKPlayerData.save(player, profile);
        }
    }
}
