package com.jjkmod.domain;

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

public class GojoInfinityDomain extends Domain {
    private final ServerPlayerEntity owner;
    private final ServerWorld world;
    private int tickCounter = 0;

    public GojoInfinityDomain(ServerPlayerEntity owner, BlockPos center) {
        super("infinity_domain", "Infinite Void", center, 30);
        this.owner = owner;
        this.world = owner.getServerWorld();
    }

    @Override
    public void onEntityEnter(Object entity) {
        if (entity instanceof LivingEntity livingEntity && livingEntity != owner) {
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 200, 2, false, false));
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 100, 1, false, false));
            livingEntity.damage(owner.getWorld().getDamageSources().magic(), 2.0F);
            
            if (livingEntity instanceof ServerPlayerEntity player) {
                player.sendMessage(Text.literal("§6You are trapped in Gojo's Infinite Void!"), false);
            }
        }
    }

    @Override
    public void onDomainTick() {
        tickCounter++;
        Box domainBox = Box.of(center.toCenterPos(), radius * 2, radius * 2, radius * 2);
        List<LivingEntity> entities = world.getEntitiesByClass(
                LivingEntity.class,
                domainBox,
                entity -> entity != owner && entity.isAlive()
        );

        for (LivingEntity entity : entities) {
            if (tickCounter % 20 == 0) {
                onEntityEnter(entity);
            }

            // Apply constant pressure effect
            if (entity instanceof ServerPlayerEntity player) {
                player.addVelocity(
                    (Math.random() - 0.5) * 0.1,
                    -0.05,
                    (Math.random() - 0.5) * 0.1
                );
            }
        }

        // Particle effects
        if (tickCounter % 10 == 0) {
            double angle = (tickCounter / 10.0) * 0.1;
            for (int i = 0; i < 8; i++) {
                double x = center.getX() + Math.cos(angle + i * Math.PI / 4) * radius;
                double y = center.getY() + 3;
                double z = center.getZ() + Math.sin(angle + i * Math.PI / 4) * radius;
                world.spawnParticles(ParticleTypes.SOUL, x, y, z, 1, 0.1, 0.1, 0.1, 0.01);
            }
        }
    }

    @Override
    public void onDomainEnd() {
        owner.sendMessage(Text.literal("§cInfinite Void has collapsed."), false);
    }
}
