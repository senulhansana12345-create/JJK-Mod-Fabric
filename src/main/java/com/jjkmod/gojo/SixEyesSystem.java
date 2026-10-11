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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import java.util.List;

public class SixEyesSystem {
    private final ServerPlayerEntity owner;
    private final ServerWorld world;
    private boolean activeSixEyes = false;

    public SixEyesSystem(ServerPlayerEntity owner) {
        this.owner = owner;
        this.world = owner.getServerWorld();
    }

    public void activateSixEyes() {
        JJKProfile profile = JJKPlayerData.get(owner);
        if (!profile.isSixEyesUnlocked()) {
            owner.sendMessage(Text.literal("\u00a7cSix Eyes not unlocked!"), false);
            return;
        }

        if (profile.getCursedEnergy() < 20) {
            owner.sendMessage(Text.literal("\u00a7cNot enough cursed energy!"), false);
            return;
        }

        activeSixEyes = true;
        JJKPlayerData.consumeCursedEnergy(owner, 20);
        owner.sendMessage(Text.literal("\u00a76Six Eyes activated! Perfect perception."), false);
        owner.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 300, 0, false, false));
    }

    public void deactivateSixEyes() {
        activeSixEyes = false;
    }

    public void tickSixEyes() {
        if (!activeSixEyes) {
            return;
        }

        // Glow effect on enemies
        Box detectionBox = Box.of(owner.getPos(), 50, 50, 50);
        List<LivingEntity> enemies = world.getEntitiesByClass(
                LivingEntity.class,
                detectionBox,
                entity -> entity != owner && entity.isAlive() && entity instanceof LivingEntity
        );

        for (LivingEntity enemy : enemies) {
            enemy.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 5, 0, false, false));
        }

        // Vision particles
        if (owner.age % 5 == 0) {
            owner.getWorld().spawnParticles(
                    ParticleTypes.END_ROD,
                    owner.getX() + (Math.random() - 0.5) * 0.5,
                    owner.getEyeY(),
                    owner.getZ() + (Math.random() - 0.5) * 0.5,
                    1,
                    0, 0, 0,
                    0.02
            );
        }
    }

    public boolean isSixEyesActive() {
        return activeSixEyes;
    }
}
