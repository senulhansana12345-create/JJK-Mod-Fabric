package com.jjkmod.combat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {
    private static final Map<UUID, Map<String, Long>> ABILITY_COOLDOWNS = new HashMap<>();
    private static final Map<String, Integer> COOLDOWN_TIMES = new HashMap<>();

    static {
        // Gojo cooldowns (in ticks)
        COOLDOWN_TIMES.put("gojo_six_eyes", 300);      // 15 seconds
        COOLDOWN_TIMES.put("gojo_infinity", 200);      // 10 seconds
        COOLDOWN_TIMES.put("gojo_blue", 400);          // 20 seconds
        COOLDOWN_TIMES.put("gojo_red", 500);           // 25 seconds
        COOLDOWN_TIMES.put("gojo_hollow_purple", 1200); // 60 seconds

        // Sukuna cooldowns
        COOLDOWN_TIMES.put("sukuna_dismantle", 350);   // 17.5 seconds
        COOLDOWN_TIMES.put("sukuna_cleave", 450);      // 22.5 seconds
        COOLDOWN_TIMES.put("sukuna_malevolent", 1400); // 70 seconds

        // Yuji cooldowns
        COOLDOWN_TIMES.put("yuji_black_flash", 300);   // 15 seconds
        COOLDOWN_TIMES.put("yuji_convergence", 250);   // 12.5 seconds

        // Megumi cooldowns
        COOLDOWN_TIMES.put("megumi_shadow", 280);      // 14 seconds
        COOLDOWN_TIMES.put("megumi_divine_dogs", 400); // 20 seconds
    }

    public static boolean isOnCooldown(UUID playerId, String abilityId) {
        if (!ABILITY_COOLDOWNS.containsKey(playerId)) {
            return false;
        }
        Map<String, Long> playerCooldowns = ABILITY_COOLDOWNS.get(playerId);
        if (!playerCooldowns.containsKey(abilityId)) {
            return false;
        }
        long remainingTime = playerCooldowns.get(abilityId) - System.currentTimeMillis();
        return remainingTime > 0;
    }

    public static int getRemainingCooldown(UUID playerId, String abilityId) {
        if (!ABILITY_COOLDOWNS.containsKey(playerId)) {
            return 0;
        }
        Map<String, Long> playerCooldowns = ABILITY_COOLDOWNS.get(playerId);
        if (!playerCooldowns.containsKey(abilityId)) {
            return 0;
        }
        long remainingMs = playerCooldowns.get(abilityId) - System.currentTimeMillis();
        return remainingMs > 0 ? (int) (remainingMs / 1000) : 0;
    }

    public static void startCooldown(UUID playerId, String abilityId) {
        Map<String, Long> playerCooldowns = ABILITY_COOLDOWNS.computeIfAbsent(playerId, k -> new HashMap<>());
        int cooldownTicks = COOLDOWN_TIMES.getOrDefault(abilityId, 100);
        long cooldownMs = (cooldownTicks * 50L); // Convert ticks to milliseconds (20 ticks/sec)
        playerCooldowns.put(abilityId, System.currentTimeMillis() + cooldownMs);
    }

    public static void resetCooldown(UUID playerId, String abilityId) {
        if (ABILITY_COOLDOWNS.containsKey(playerId)) {
            ABILITY_COOLDOWNS.get(playerId).remove(abilityId);
        }
    }

    public static void clearAllCooldowns(UUID playerId) {
        ABILITY_COOLDOWNS.remove(playerId);
    }
}
