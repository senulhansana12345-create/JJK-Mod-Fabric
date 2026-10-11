package com.jjkmod.combat;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import java.util.Random;

public class CombatResolver {
    private static final Random RANDOM = new Random();

    public static void applyDamageAndEffects(
            LivingEntity attacker,
            LivingEntity target,
            DamageInstance damage,
            boolean critical
    ) {
        if (!(target.getWorld() instanceof ServerWorld world)) return;

        float finalDamage = damage.baseDamage;
        if (critical) {
            finalDamage *= 1.5f;
            spawnCriticalParticles(world, target.getPos());
        }

        // Apply damage
        target.damage(world.getDamageSources().magic(), finalDamage);

        // Apply knockback
        Vec3d direction = target.getPos().subtract(attacker.getPos()).normalize();
        target.addVelocity(
                direction.x * damage.knockbackForce,
                damage.knockbackForce * 0.3,
                direction.z * damage.knockbackForce
        );

        // Apply status effects based on damage type
        switch (damage.damageType) {
            case "slash":
                target.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.SLOWNESS,
                        damage.duration,
                        1,
                        false,
                        false
                ));
                spawnSlashParticles(world, target.getPos());
                break;

            case "blunt":
                if (RANDOM.nextFloat() < damage.staggerChance) {
                    target.addStatusEffect(new StatusEffectInstance(
                            StatusEffects.NAUSEA,
                            damage.duration / 2,
                            0,
                            false,
                            false
                    ));
                }
                spawnBluntParticles(world, target.getPos());
                break;

            case "energy":
                target.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.RESISTANCE,
                        damage.duration,
                        0,
                        false,
                        false
                ));
                spawnEnergyParticles(world, target.getPos());
                break;

            case "curse":
                target.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.WEAKNESS,
                        damage.duration,
                        1,
                        false,
                        false
                ));
                target.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.SLOWNESS,
                        damage.duration / 2,
                        2,
                        false,
                        false
                ));
                spawnCurseParticles(world, target.getPos());
                break;
        }
    }

    private static void spawnCriticalParticles(ServerWorld world, Vec3d pos) {
        world.spawnParticles(ParticleTypes.EXPLOSION_EMITTER, pos.x, pos.y, pos.z, 3, 0.5, 0.5, 0.5, 0.1);
    }

    private static void spawnSlashParticles(ServerWorld world, Vec3d pos) {
        world.spawnParticles(ParticleTypes.FLAME, pos.x, pos.y, pos.z, 5, 0.4, 0.4, 0.4, 0.1);
    }

    private static void spawnBluntParticles(ServerWorld world, Vec3d pos) {
        world.spawnParticles(ParticleTypes.SMOKE, pos.x, pos.y, pos.z, 4, 0.3, 0.3, 0.3, 0.08);
    }

    private static void spawnEnergyParticles(ServerWorld world, Vec3d pos) {
        world.spawnParticles(ParticleTypes.PORTAL, pos.x, pos.y, pos.z, 6, 0.5, 0.5, 0.5, 0.12);
    }

    private static void spawnCurseParticles(ServerWorld world, Vec3d pos) {
        world.spawnParticles(ParticleTypes.DRIPPING_OBSIDIAN, pos.x, pos.y, pos.z, 5, 0.4, 0.4, 0.4, 0.1);
    }
}
