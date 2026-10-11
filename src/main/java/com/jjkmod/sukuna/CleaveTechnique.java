package com.jjkmod.sukuna;

import com.jjkmod.JJKPlayerData;
import com.jjkmod.JJKProfile;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import java.util.List;

public class CleaveTechnique {
    public static void execute(ServerPlayerEntity player) {
        JJKProfile profile = JJKPlayerData.get(player);
        if (profile.getCursedEnergy() < 35) {
            player.sendMessage(Text.literal("\u00a7cNot enough energy! Need 35, have " + profile.getCursedEnergy()), false);
            return;
        }

        JJKPlayerData.consumeCursedEnergy(player, 35);
        ServerWorld world = player.getServerWorld();
        Vec3d lookDirection = player.getRotationVector();
        Vec3d startPos = player.getEyePos();

        player.sendMessage(Text.literal("\u00a74Cleave!"), false);

        // Wider, more devastating slash
        for (double distance = 0; distance < 25; distance += 1.0) {
            Vec3d scanPos = startPos.add(lookDirection.multiply(distance));
            Box scanBox = Box.of(scanPos, 6, 6, 6);

            List<LivingEntity> targets = world.getEntitiesByClass(
                    LivingEntity.class,
                    scanBox,
                    entity -> entity != player && entity.isAlive()
            );

            for (LivingEntity target : targets) {
                target.damage(world.getDamageSources().magic(), 8.0F);
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 80, 1, false, false));
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 2, false, false));
                Vec3d knockback = lookDirection.multiply(2.0);
                target.addVelocity(knockback.x, knockback.y * 0.4, knockback.z);
            }

            // More intense red particles
            world.spawnParticles(
                    ParticleTypes.FLAME,
                    scanPos.x,
                    scanPos.y,
                    scanPos.z,
                    5,
                    0.5, 0.5, 0.5,
                    0.08
            );
        }
    }
}
