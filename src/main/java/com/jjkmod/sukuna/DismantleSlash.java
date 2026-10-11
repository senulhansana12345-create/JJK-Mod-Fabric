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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import java.util.List;

public class DismantleSlash {
    public static void execute(ServerPlayerEntity player) {
        JJKProfile profile = JJKPlayerData.get(player);
        if (profile.getCursedEnergy() < 25) {
            player.sendMessage(Text.literal("\u00a7cNot enough energy! Need 25, have " + profile.getCursedEnergy()), false);
            return;
        }

        JJKPlayerData.consumeCursedEnergy(player, 25);
        ServerWorld world = player.getServerWorld();
        Vec3d lookDirection = player.getRotationVector();
        Vec3d startPos = player.getEyePos();

        player.sendMessage(Text.literal("\u00a74Dismantle!"), false);

        // Slash cone in front
        for (double distance = 0; distance < 20; distance += 1.0) {
            Vec3d scanPos = startPos.add(lookDirection.multiply(distance));
            Box scanBox = Box.of(scanPos, 4, 4, 4);

            List<LivingEntity> targets = world.getEntitiesByClass(
                    LivingEntity.class,
                    scanBox,
                    entity -> entity != player && entity.isAlive()
            );

            for (LivingEntity target : targets) {
                target.damage(world.getDamageSources().magic(), 6.0F);
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 1, false, false));
                Vec3d knockback = lookDirection.multiply(1.5);
                target.addVelocity(knockback.x, knockback.y * 0.3, knockback.z);
            }

            // Red slash particles
            world.spawnParticles(
                    ParticleTypes.FLAME,
                    scanPos.x,
                    scanPos.y,
                    scanPos.z,
                    3,
                    0.3, 0.3, 0.3,
                    0.05
            );
        }
    }
}
