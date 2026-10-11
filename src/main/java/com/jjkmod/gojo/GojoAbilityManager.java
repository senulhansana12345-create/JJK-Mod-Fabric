package com.jjkmod.gojo;

import com.jjkmod.JJKPlayerData;
import com.jjkmod.JJKProfile;
import net.minecraft.server.network.ServerPlayerEntity;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GojoAbilityManager {
    private static final Map<UUID, SixEyesSystem> GOJO_EYES = new HashMap<>();
    private static final Map<UUID, LimitlessSystem> GOJO_LIMITLESS = new HashMap<>();

    public static SixEyesSystem getSixEyesSystem(ServerPlayerEntity player) {
        if (!player.getName().asString().contains("gojo")) {
            JJKProfile profile = JJKPlayerData.get(player);
            if (!profile.getSorcererId().equals("gojo")) {
                return null;
            }
        }
        return GOJO_EYES.computeIfAbsent(player.getUuid(), uuid -> new SixEyesSystem(player));
    }

    public static LimitlessSystem getLimitlessSystem(ServerPlayerEntity player) {
        if (!player.getName().asString().contains("gojo")) {
            JJKProfile profile = JJKPlayerData.get(player);
            if (!profile.getSorcererId().equals("gojo")) {
                return null;
            }
        }
        return GOJO_LIMITLESS.computeIfAbsent(player.getUuid(), uuid -> new LimitlessSystem(player));
    }

    public static void tickGojo(ServerPlayerEntity player) {
        JJKProfile profile = JJKPlayerData.get(player);
        if (!profile.getSorcererId().equals("gojo")) {
            return;
        }

        SixEyesSystem eyes = getSixEyesSystem(player);
        if (eyes != null) {
            eyes.tickSixEyes();
        }

        LimitlessSystem limitless = getLimitlessSystem(player);
        if (limitless != null) {
            limitless.tick();
        }
    }
}
