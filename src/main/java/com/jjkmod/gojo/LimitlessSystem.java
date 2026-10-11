package com.jjkmod.gojo;

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

public class LimitlessSystem {
    private final ServerPlayerEntity owner;
    private final ServerWorld world;
    private boolean infinityBarrierActive = false;
    private int infinityBarrierDuration = 0;

    public LimitlessSystem(ServerPlayerEntity owner) {
        this.owner = owner;
        this.world = owner.getServerWorld();
    }

    /**
     * Activates the Infinity barrier - repels all damage and forces enemies away
     */
    public void activateInfinity() {
        JJKProfile profile = JJKPlayerData.get(owner);
        if (profile.getCursedEnergy() < 30) {
            owner.sendMessage(Text.literal("\u00a7cNot enough cursed energy for Infinity! Need 30, have " + profile.getCursedEnergy()), false);
            return;
        }

        JJKPlayerData.consumeCursedEnergy(owner, 30);
        infinityBarrierActive = true;
        infinityBarrierDuration = 200; // 10 seconds
        owner.sendMessage(Text.literal("\u00a76Infinity activated! All damage negated."), false);
    }

    /**
     * Activates Blue - pulls nearby enemies toward Gojo with immense force
     */
    public void activateBlue() {
        JJKProfile profile = JJKPlayerData.get(owner);
        if (profile.getCursedEnergy() < 40) {
            owner.sendMessage(Text.literal("\u00a7cNot enough cursed energy for Blue! Need 40, have " + profile.getCursedEnergy()), false);
            return;
        }

        JJKPlayerData.consumeCursedEnergy(owner, 40);
        owner.sendMessage(Text.literal("\u00a76Blue activated! Attractive force engaged."), false);

        Box pullBox = Box.of(owner.getPos(), 30, 30, 30);
        List<LivingEntity> targets = world.getEntitiesByClass(
                LivingEntity.class,
                pullBox,
                entity -> entity != owner && entity.isAlive()
        );

        Vec3d ownerPos = owner.getPos();
        for (LivingEntity target : targets) {
            Vec3d targetPos = target.getPos();
            Vec3d direction = ownerPos.subtract(targetPos).normalize();
            double distance = ownerPos.distanceTo(targetPos);
            double pullStrength = Math.max(1.0, (30 - distance) / 10.0);

            target.addVelocity(
                    direction.x * pullStrength * 0.8,
                    direction.y * pullStrength * 0.3,
                    direction.z * pullStrength * 0.8
            );

            // Damage on collision
            if (distance < 5) {
                target.damage(owner.getWorld().getDamageSources().magic(), 3.0F);
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 2, false, false));
            }

            // Visual effect - blue particles
            world.spawnParticles(
                    ParticleTypes.SOUL,
                    target.getX(),
                    target.getY() + 1,
                    target.getZ(),
                    3,
                    0.2, 0.2, 0.2,
                    0.05
            );
        }
    }

    /**
     * Activates Red - violently repels all nearby entities away from Gojo
     */
    public void activateRed() {
        JJKProfile profile = JJKPlayerData.get(owner);
        if (profile.getCursedEnergy() < 40) {
            owner.sendMessage(Text.literal("\u00a7cNot enough cursed energy for Red! Need 40, have " + profile.getCursedEnergy()), false);
            return;
        }

        JJKPlayerData.consumeCursedEnergy(owner, 40);
        owner.sendMessage(Text.literal("\u00a7cRed activated! Repulsive force engaged."), false);

        Box repelBox = Box.of(owner.getPos(), 25, 25, 25);
        List<LivingEntity> targets = world.getEntitiesByClass(
                LivingEntity.class,
                repelBox,
                entity -> entity != owner && entity.isAlive()
        );

        Vec3d ownerPos = owner.getPos();
        for (LivingEntity target : targets) {
            Vec3d targetPos = target.getPos();
            Vec3d direction = targetPos.subtract(ownerPos).normalize();
            double distance = ownerPos.distanceTo(targetPos);
            double repelStrength = Math.max(2.0, (25 - distance) / 5.0);

            target.addVelocity(
                    direction.x * repelStrength,
                    direction.y * repelStrength * 0.5,
                    direction.z * repelStrength
            );

            // Damage
            target.damage(owner.getWorld().getDamageSources().magic(), 4.0F);
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 40, 0, false, false));

            // Visual effect - red particles
            world.spawnParticles(
                    ParticleTypes.FLAME,
                    target.getX(),
                    target.getY() + 1,
                    target.getZ(),
                    3,
                    0.3, 0.3, 0.3,
                    0.05
            );
        }
    }

    /**
     * Activates Hollow Purple - annihilates matter in a cone in front of Gojo
     */
    public void activateHollowPurple() {
        JJKProfile profile = JJKPlayerData.get(owner);
        if (profile.getCursedEnergy() < 100) {
            owner.sendMessage(Text.literal("\u00a7cNot enough cursed energy for Hollow Purple! Need 100, have " + profile.getCursedEnergy()), false);
            return;
        }

        JJKPlayerData.consumeCursedEnergy(owner, 100);
        owner.sendMessage(Text.literal("\u00a75Hollow Purple! Matter annihilation!"), false);

        Vec3d lookDirection = owner.getRotationVector();
        Vec3d startPos = owner.getEyePos();

        // Scan a cone in front of Gojo
        for (double distance = 0; distance < 40; distance += 1.0) {
            Vec3d scanPos = startPos.add(lookDirection.multiply(distance));
            Box scanBox = Box.of(scanPos, 6, 6, 6);

            List<LivingEntity> targets = world.getEntitiesByClass(
                    LivingEntity.class,
                    scanBox,
                    entity -> entity != owner && entity.isAlive()
            );

            for (LivingEntity target : targets) {
                target.damage(owner.getWorld().getDamageSources().magic(), 8.0F);
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 100, 0, false, false));
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 100, 2, false, false));

                // Knockback
                Vec3d knockback = lookDirection.multiply(2.0);
                target.addVelocity(knockback.x, knockback.y * 0.5, knockback.z);
            }

            // Visual effect - purple void particles
            world.spawnParticles(
                    ParticleTypes.DRAGON_BREATH,
                    scanPos.x,
                    scanPos.y,
                    scanPos.z,
                    2,
                    0.5, 0.5, 0.5,
                    0.03
            );
        }
    }

    /**
     * Tickable barrier damage negation
     */
    public void tickInfinityBarrier() {
        if (!infinityBarrierActive) {
            return;
        }

        infinityBarrierDuration--;
        if (infinityBarrierDuration <= 0) {
            infinityBarrierActive = false;
            owner.sendMessage(Text.literal("\u00a7cInfinity barrier faded."), false);
            return;
        }

        // Repel damage attempts
        if (owner.getHealth() < owner.getMaxHealth() && infinityBarrierDuration > 0) {
            owner.heal(0.5F);
        }

        // Barrier particles
        if (owner.age % 2 == 0) {
            double angle = (owner.age / 2.0) * 0.1;
            for (int i = 0; i < 8; i++) {
                double x = owner.getX() + Math.cos(angle + i * Math.PI / 4) * 2.5;
                double y = owner.getY() + 1;
                double z = owner.getZ() + Math.sin(angle + i * Math.PI / 4) * 2.5;
                owner.getWorld().spawnParticles(
                        ParticleTypes.END_ROD,
                        x, y, z,
                        1,
                        0.1, 0.1, 0.1,
                        0.02
                );
            }
        }
    }

    public boolean isInfinityBarrierActive() {
        return infinityBarrierActive;
    }

    public void tick() {
        tickInfinityBarrier();
    }
}
