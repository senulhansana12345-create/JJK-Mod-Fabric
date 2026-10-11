package com.jjkmod.domain;

import com.jjkmod.JJKPlayerData;
import com.jjkmod.JJKProfile;
import com.jjkmod.technique.JJKRegistries;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class DomainManager {
    private static final Map<UUID, Domain> ACTIVE_DOMAINS = new HashMap<>();
    private static final int DOMAIN_COOLDOWN = 1200; // 60 seconds in ticks
    private static final Map<UUID, Long> DOMAIN_COOLDOWNS = new HashMap<>();

    private DomainManager() {
    }

    public static void activateDomain(ServerPlayerEntity player) {
        UUID playerId = player.getUuid();
        JJKProfile profile = JJKPlayerData.get(player);
        String sorcererId = profile.getSorcererId();
        var sorcerer = JJKRegistries.getSorcerer(sorcererId);

        if (sorcerer == null) {
            player.sendMessage(Text.literal("§cSorcerer not found!"), false);
            return;
        }

        if (!profile.isDomainUnlocked()) {
            player.sendMessage(Text.literal("§cYou have not unlocked a domain yet!"), false);
            return;
        }

        if (profile.getCursedEnergy() < 50) {
            player.sendMessage(Text.literal("§cInsufficient cursed energy! Need 50, have " + profile.getCursedEnergy()), false);
            return;
        }

        if (DOMAIN_COOLDOWNS.containsKey(playerId)) {
            long lastUse = DOMAIN_COOLDOWNS.get(playerId);
            long elapsed = System.currentTimeMillis() - lastUse;
            if (elapsed < DOMAIN_COOLDOWN * 50) { // 50ms per tick
                player.sendMessage(Text.literal("§cDomain on cooldown for " + ((DOMAIN_COOLDOWN * 50 - elapsed) / 1000) + " more seconds."), false);
                return;
            }
        }

        BlockPos center = player.getBlockPos().add(0, 2, 0);
        Domain domain = createDomainByType(sorcererId, player, center);

        if (domain != null) {
            ACTIVE_DOMAINS.put(playerId, domain);
            JJKPlayerData.consumeCursedEnergy(player, 50);
            JJKPlayerData.setDomainActive(player, true);
            DOMAIN_COOLDOWNS.put(playerId, System.currentTimeMillis());

            player.sendMessage(Text.literal("§6Domain activated: " + domain.getDisplayName()), false);

            // Teleport nearby entities into the domain
            ServerWorld world = player.getServerWorld();
            Box box = Box.of(center.toCenterPos(), 40, 30, 40);
            List<Entity> nearby = world.getOtherEntities(player, box, entity -> entity instanceof LivingEntity && entity != player && entity.isAlive());

            for (Entity entity : nearby) {
                double x = center.getX() + 0.5;
                double y = center.getY() + 1.5;
                double z = center.getZ() + 0.5;
                entity.teleport(world, x, y, z);
                if (entity instanceof ServerPlayerEntity targetPlayer) {
                    targetPlayer.sendMessage(Text.literal("§cYou have been pulled into the domain!"), false);
                }
            }
        }
    }

    public static Domain createDomainByType(String sorcererId, ServerPlayerEntity owner, BlockPos center) {
        return switch (sorcererId) {
            case "gojo" -> new GojoInfinityDomain(owner, center);
            case "sukuna" -> new SukunaMalevolentShrine(owner, center);
            case "yuji" -> new YujiBlackFlashDomain(owner, center);
            case "megumi" -> new MegumiDivineDogsDomain(owner, center);
            default -> null;
        };
    }

    public static void tickDomains() {
        ACTIVE_DOMAINS.forEach((uuid, domain) -> {
            if (domain.isActive()) {
                domain.onDomainTick();
            } else {
                ACTIVE_DOMAINS.remove(uuid);
            }
        });
    }

    public static void endDomain(UUID playerId) {
        Domain domain = ACTIVE_DOMAINS.get(playerId);
        if (domain != null) {
            domain.deactivate();
            domain.onDomainEnd();
            ACTIVE_DOMAINS.remove(playerId);
        }
    }

    public static Domain getPlayerDomain(UUID playerId) {
        return ACTIVE_DOMAINS.get(playerId);
    }
}
