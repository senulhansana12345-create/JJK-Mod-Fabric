package com.jjkmod.domain;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import java.util.List;

public class YujiBlackFlashDomain extends Domain {
    private final ServerPlayerEntity owner;
    private final ServerWorld world;
    private int tickCounter = 0;

    public YujiBlackFlashDomain(ServerPlayerEntity owner, BlockPos center) {
        super("black_flash_domain", "Black Flash Domain", center, 20);
        this.owner = owner;
        this.world = owner.getServerWorld();
    }

    @Override
    public void onEntityEnter(Object entity) {
        if (entity instanceof LivingEntity livingEntity && livingEntity != owner) {
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 100, 1, false, false));
            livingEntity.damage(owner.getWorld().getDamageSources().magic(), 2.5F);
            
            if (livingEntity instanceof ServerPlayerEntity player) {
                player.sendMessage(Text.literal("§eYuji's Black Flash Domain!"), false);
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
            if (tickCounter % 25 == 0) {
                onEntityEnter(entity);
            }

            // Push enemies around
            if (tickCounter % 10 == 0) {
                entity.addVelocity(
                    (Math.random() - 0.5) * 0.2,
                    0.1,
                    (Math.random() - 0.5) * 0.2
                );
            }
        }

        // Yellow/gold particle effects
        if (tickCounter % 8 == 0) {
            for (int i = 0; i < 6; i++) {
                double angle = (tickCounter / 8.0 + i * Math.PI / 3) * 0.1;
                double x = center.getX() + Math.cos(angle) * radius;
                double y = center.getY() + 2;
                double z = center.getZ() + Math.sin(angle) * radius;
                world.spawnParticles(ParticleTypes.ELECTRIC_SPARK, x, y, z, 2, 0.1, 0.1, 0.1, 0.02);
            }
        }
    }

    @Override
    public void onDomainEnd() {
        owner.sendMessage(Text.literal("§eBlack Flash Domain fades away."), false);
    }
}
