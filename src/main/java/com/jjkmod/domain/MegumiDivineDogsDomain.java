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

public class MegumiDivineDogsDomain extends Domain {
    private final ServerPlayerEntity owner;
    private final ServerWorld world;
    private int tickCounter = 0;

    public MegumiDivineDogsDomain(ServerPlayerEntity owner, BlockPos center) {
        super("divine_dogs_domain", "Divine Dogs Domain", center, 22);
        this.owner = owner;
        this.world = owner.getServerWorld();
    }

    @Override
    public void onEntityEnter(Object entity) {
        if (entity instanceof LivingEntity livingEntity && livingEntity != owner) {
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 150, 1, false, false));
            livingEntity.damage(owner.getWorld().getDamageSources().magic(), 2.0F);
            
            if (livingEntity instanceof ServerPlayerEntity player) {
                player.sendMessage(Text.literal("§5Megumi's Divine Dogs attack!"), false);
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
            if (tickCounter % 30 == 0) {
                onEntityEnter(entity);
            }

            // Tactical strikes
            if (tickCounter % 15 == 0) {
                double angle = Math.random() * Math.PI * 2;
                entity.addVelocity(
                    Math.cos(angle) * 0.3,
                    0.05,
                    Math.sin(angle) * 0.3
                );
            }
        }

        // Purple particle effects (dogs circling)
        if (tickCounter % 6 == 0) {
            for (int dog = 0; dog < 2; dog++) {
                double angle = (tickCounter / 6.0 + dog * Math.PI) * 0.2;
                double x = center.getX() + Math.cos(angle) * (radius - 8);
                double y = center.getY() + 1;
                double z = center.getZ() + Math.sin(angle) * (radius - 8);
                world.spawnParticles(ParticleTypes.MYCELIUM, x, y, z, 1, 0.1, 0.1, 0.1, 0.01);
            }
        }
    }

    @Override
    public void onDomainEnd() {
        owner.sendMessage(Text.literal("§5Divine Dogs disappear."), false);
    }
}
