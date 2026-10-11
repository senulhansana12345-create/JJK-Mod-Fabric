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
import net.minecraft.util.math.Vec3d;
import java.util.List;

public class DismantleSlash {
    public static void execute(ServerPlayerEntity player) {
        JJKProfile profile = JJKPlayerData.get(player);
        if (profile.getCursedEnergy() < 25) {
            player.sendMessage(Text.literal("\u00a7cNot enough energy! Need 25, have " + profile.getCursedEnergy()), false);
            return;
        }

        if (CooldownManager.isOnCooldown(player.getUuid(), "sukuna_dismantle")) {
            int remaining = CooldownManager.getRemainingCooldown(player.getUuid(), "sukuna_dismantle");
            player.sendMessage(Text.literal("\u00a7cAbility on cooldown: " + remaining + "s"), false);
            return;
        }

        JJKPlayerData.consumeCursedEnergy(player, 25);
        CooldownManager.startCooldown(player.getUuid(), "sukuna_dismantle");

        ServerWorld world = player.getServerWorld();
        List<LivingEntity> targets = HitboxSystem.getEntitiesInCone(player, 20, 45);

        player.sendMessage(Text.literal("\u00a74Dismantle!"), false);
        DamageInstance damage = DamageInstance.slashInstance(6.0f, 1.5f);

        for (LivingEntity target : targets) {
            CombatResolver.applyDamageAndEffects(player, target, damage, false);
        }
    }
}
