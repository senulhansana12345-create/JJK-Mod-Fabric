package com.jjkmod.sukuna;

import com.jjkmod.JJKPlayerData;
import com.jjkmod.JJKProfile;
import com.jjkmod.combat.CombatResolver;
import com.jjkmod.combat.CooldownManager;
import com.jjkmod.combat.DamageInstance;
import com.jjkmod.combat.HitboxSystem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import java.util.List;

public class CleaveTechnique {
    public static void execute(ServerPlayerEntity player) {
        JJKProfile profile = JJKPlayerData.get(player);
        if (profile.getCursedEnergy() < 35) {
            player.sendMessage(Text.literal("\u00a7cNot enough energy! Need 35, have " + profile.getCursedEnergy()), false);
            return;
        }

        if (CooldownManager.isOnCooldown(player.getUuid(), "sukuna_cleave")) {
            int remaining = CooldownManager.getRemainingCooldown(player.getUuid(), "sukuna_cleave");
            player.sendMessage(Text.literal("\u00a7cAbility on cooldown: " + remaining + "s"), false);
            return;
        }

        JJKPlayerData.consumeCursedEnergy(player, 35);
        CooldownManager.startCooldown(player.getUuid(), "sukuna_cleave");

        ServerWorld world = player.getServerWorld();
        List<LivingEntity> targets = HitboxSystem.getEntitiesInCone(player, 25, 60);

        player.sendMessage(Text.literal("\u00a74Cleave!"), false);
        DamageInstance damage = DamageInstance.slashInstance(8.0f, 2.0f);

        for (LivingEntity target : targets) {
            boolean critical = Math.random() < 0.25;
            CombatResolver.applyDamageAndEffects(player, target, damage, critical);
        }
    }
}
