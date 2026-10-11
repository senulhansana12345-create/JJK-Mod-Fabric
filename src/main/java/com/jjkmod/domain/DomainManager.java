package com.jjkmod.domain;

import com.jjkmod.JJKPlayerData;
import com.jjkmod.JJKProfile;
import com.jjkmod.technique.JJKRegistries;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import java.util.List;

public final class DomainManager {
    private DomainManager() {
    }

    public static void activateDomain(ServerPlayerEntity player) {
        JJKProfile profile = JJKPlayerData.get(player);
        String sorcererId = profile.getSorcererId();
        var sorcerer = JJKRegistries.getSorcerer(sorcererId);

        if (sorcerer == null) {
            return;
        }

        BlockPos center = player.getBlockPos().add(0, 2, 0);
        ServerWorld world = player.getServerWorld();
        Box box = Box.of(center.toCenterPos(), 26, 20, 26);

        List<Entity> nearby = world.getOtherEntities(player, box, entity -> entity != player && entity.isAlive());

        for (Entity entity : nearby) {
            if (entity instanceof LivingEntity livingEntity) {
                double x = center.getX() + 1.5D;
                double y = center.getY() + 1.5D;
                double z = center.getZ() + 1.5D;
                entity.teleport(world, x, y, z);
                if (entity instanceof ServerPlayerEntity targetPlayer) {
                    targetPlayer.sendMessage(net.minecraft.text.Text.literal("You have been pulled into the domain."), false);
                }
            }
        }

        profile.setDomainUnlocked(true);
        JJKPlayerData.save(player, profile);
        player.sendMessage(net.minecraft.text.Text.literal("Domain activated: " + sorcerer.domainName()), false);
    }
}
