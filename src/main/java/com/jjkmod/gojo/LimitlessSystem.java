package com.jjkmod.gojo;

import com.jjkmod.JJKPlayerData;
import com.jjkmod.JJKProfile;
import com.jjkmod.combat.CombatResolver;
import com.jjkmod.combat.CooldownManager;
import com.jjkmod.combat.DamageInstance;
import com.jjkmod.combat.HitboxSystem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import java.util.List;

public class LimitlessSystem {
    private final ServerPlayerEntity player;
    private boolean infinityActive = false;
    private int infinityDuration = 0;

    public LimitlessSystem(ServerPlayerEntity player) {
        this.player = player;
    }

    public void activateBlue() {
        JJKProfile profile = JJKPlayerData.get(player);
        if (profile.getCursedEnergy() < 30) {
            player.sendMessage(Text.literal("\u00a7cNot enough cursed energy!"), false);
            return;
        }

        if (CooldownManager.isOnCooldown(player.getUuid(), "gojo_blue")) {
            int remaining = CooldownManager.getRemainingCooldown(player.getUuid(), "gojo_blue");
            player.sendMessage(Text.literal("\u00a7cAbility on cooldown: " + remaining + "s"), false);
            return;
        }

        JJKPlayerData.consumeCursedEnergy(player, 30);
        CooldownManager.startCooldown(player.getUuid(), "gojo_blue");

        ServerWorld world = player.getServerWorld();
        List<LivingEntity> targets = HitboxSystem.getEntitiesInCone(player, 25, 60);

        player.sendMessage(Text.literal("\u00a79Blue - Pull!"), false);

        for (LivingEntity target : targets) {
            // Pull entities toward Gojo
            Vec3d direction = player.getPos().subtract(target.getPos()).normalize();
            target.addVelocity(direction.x * 2.0, direction.y * 0.8, direction.z * 2.0);
            target.damage(world.getDamageSources().magic(), 5.0F);

            // Spawn blue energy particles
            for (int i = 0; i < 10; i++) {
                double angle = Math.random() * Math.PI * 2;
                double x = target.getX() + Math.cos(angle) * 0.5;
                double z = target.getZ() + Math.sin(angle) * 0.5;
                world.spawnParticles(ParticleTypes.SOUL, x, target.getY(), z, 1, 0.2, 0.2, 0.2, 0.05);
            }
        }
    }

    public void activateRed() {
        JJKProfile profile = JJKPlayerData.get(player);
        if (profile.getCursedEnergy() < 40) {
            player.sendMessage(Text.literal("\u00a7cNot enough cursed energy!"), false);
            return;
        }

        if (CooldownManager.isOnCooldown(player.getUuid(), "gojo_red")) {
            int remaining = CooldownManager.getRemainingCooldown(player.getUuid(), "gojo_red");
            player.sendMessage(Text.literal("\u00a7cAbility on cooldown: " + remaining + "s"), false);
            return;
        }

        JJKPlayerData.consumeCursedEnergy(player, 40);
        CooldownManager.startCooldown(player.getUuid(), "gojo_red");

        ServerWorld world = player.getServerWorld();
        List<LivingEntity> targets = HitboxSystem.getEntitiesInRadius(player, 15);

        player.sendMessage(Text.literal("\u00a7cRed - Repel!"), false);
        DamageInstance damage = DamageInstance.energyInstance(8.0f, 3.0f);

        for (LivingEntity target : targets) {
            CombatResolver.applyDamageAndEffects(player, target, damage, false);
        }
    }

    public void activateInfinity() {
        JJKProfile profile = JJKPlayerData.get(player);
        if (profile.getCursedEnergy() < 25) {
            player.sendMessage(Text.literal("\u00a7cNot enough cursed energy!"), false);
            return;
        }

        if (CooldownManager.isOnCooldown(player.getUuid(), "gojo_infinity")) {
            int remaining = CooldownManager.getRemainingCooldown(player.getUuid(), "gojo_infinity");
            player.sendMessage(Text.literal("\u00a7cAbility on cooldown: " + remaining + "s"), false);
            return;
        }

        JJKPlayerData.consumeCursedEnergy(player, 25);
        CooldownManager.startCooldown(player.getUuid(), "gojo_infinity");

        infinityActive = !infinityActive;
        if (infinityActive) {
            player.sendMessage(Text.literal("\u00a76Infinity Barrier: \u00a7aACTIVE"), false);
        } else {
            player.sendMessage(Text.literal("\u00a76Infinity Barrier: \u00a7cDEACTIVATED"), false);
        }
    }

    public void activateHollowPurple() {
        JJKProfile profile = JJKPlayerData.get(player);
        if (profile.getCursedEnergy() < 80) {
            player.sendMessage(Text.literal("\u00a7cNot enough cursed energy!"), false);
            return;
        }

        if (CooldownManager.isOnCooldown(player.getUuid(), "gojo_hollow_purple")) {
            int remaining = CooldownManager.getRemainingCooldown(player.getUuid(), "gojo_hollow_purple");
            player.sendMessage(Text.literal("\u00a7cAbility on cooldown: " + remaining + "s"), false);
            return;
        }

        JJKPlayerData.consumeCursedEnergy(player, 80);
        CooldownManager.startCooldown(player.getUuid(), "gojo_hollow_purple");

        ServerWorld world = player.getServerWorld();
        List<LivingEntity> targets = HitboxSystem.getEntitiesInCone(player, 30, 90);

        player.sendMessage(Text.literal("\u00a75Hollow Purple - Annihilation!"), false);
        DamageInstance damage = DamageInstance.energyInstance(15.0f, 4.0f);

        for (LivingEntity target : targets) {
            boolean critical = Math.random() < 0.4;
            CombatResolver.applyDamageAndEffects(player, target, damage, critical);
        }

        // Massive purple explosion effect
        for (int i = 0; i < 50; i++) {
            double angle = Math.random() * Math.PI * 2;
            double distance = Math.random() * 20;
            double x = player.getX() + Math.cos(angle) * distance;
            double y = player.getY() + Math.random() * 10;
            double z = player.getZ() + Math.sin(angle) * distance;
            world.spawnParticles(ParticleTypes.DRAGON_BREATH, x, y, z, 1, 0.5, 0.5, 0.5, 0.1);
        }
    }

    public void tickSixEyes() {
        // Passive: Enhanced perception/regeneration
        JJKProfile profile = JJKPlayerData.get(player);
        if (profile.isSixEyesUnlocked() && profile.getCursedEnergy() < profile.getMaxCursedEnergy()) {
            profile.setCursedEnergy(Math.min(profile.getCursedEnergy() + 1, profile.getMaxCursedEnergy()));
            JJKPlayerData.save(player, profile);
        }
    }
}
