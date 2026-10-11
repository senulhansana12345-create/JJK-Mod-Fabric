package com.jjkmod.domain;

import net.minecraft.block.Blocks;
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

public class SukunaMalevolentShrine extends Domain {
    private final ServerPlayerEntity owner;
    private final ServerWorld world;
    private int tickCounter = 0;
    private boolean shrineBuilt = false;

    public SukunaMalevolentShrine(ServerPlayerEntity owner, BlockPos center) {
        super("malevolent_shrine", "Malevolent Shrine", center, 25);
        this.owner = owner;
        this.world = owner.getServerWorld();
        buildShrine();
    }

    private void buildShrine() {
        // Build a shrine structure at the domain center
        int x = center.getX();
        int y = center.getY();
        int z = center.getZ();

        // Create a platform
        for (int i = -3; i <= 3; i++) {
            for (int j = -3; j <= 3; j++) {
                world.setBlockState(new BlockPos(x + i, y - 1, z + j), Blocks.OBSIDIAN.getDefaultState());
            }
        }

        // Create shrine pillars
        for (int i = 0; i < 4; i++) {
            int px = x + (i < 2 ? -2 : 2);
            int pz = z + ((i % 2) == 0 ? -2 : 2);
            for (int h = 0; h < 5; h++) {
                world.setBlockState(new BlockPos(px, y + h, pz), Blocks.DARK_OAK_WOOD.getDefaultState());
            }
        }

        shrineBuilt = true;
    }

    @Override
    public void onEntityEnter(Object entity) {
        if (entity instanceof LivingEntity livingEntity && livingEntity != owner) {
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 200, 1, false, false));
            livingEntity.damage(owner.getWorld().getDamageSources().magic(), 3.0F);
            
            if (livingEntity instanceof ServerPlayerEntity player) {
                player.sendMessage(Text.literal("§4You stand before Sukuna's Malevolent Shrine!"), false);
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
            if (tickCounter % 15 == 0) {
                onEntityEnter(entity);
            }

            // Slash attacks from the shrine
            if (tickCounter % 30 == 0) {
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 100, 0, false, false));
            }
        }

        // Red particle effects around the shrine
        if (tickCounter % 5 == 0) {
            double angle = (tickCounter / 5.0) * 0.15;
            for (int i = 0; i < 12; i++) {
                double x = center.getX() + Math.cos(angle + i * Math.PI / 6) * (radius - 5);
                double y = center.getY() + 2;
                double z = center.getZ() + Math.sin(angle + i * Math.PI / 6) * (radius - 5);
                world.spawnParticles(ParticleTypes.FLAME, x, y, z, 1, 0.2, 0.2, 0.2, 0.01);
            }
        }
    }

    @Override
    public void onDomainEnd() {
        // Remove shrine blocks
        int x = center.getX();
        int y = center.getY();
        int z = center.getZ();

        for (int i = -3; i <= 3; i++) {
            for (int j = -3; j <= 3; j++) {
                world.setBlockState(new BlockPos(x + i, y - 1, z + j), Blocks.AIR.getDefaultState());
            }
        }

        for (int i = 0; i < 4; i++) {
            int px = x + (i < 2 ? -2 : 2);
            int pz = z + ((i % 2) == 0 ? -2 : 2);
            for (int h = 0; h < 5; h++) {
                world.setBlockState(new BlockPos(px, y + h, pz), Blocks.AIR.getDefaultState());
            }
        }

        owner.sendMessage(Text.literal("§cMalevolent Shrine has dissipated."), false);
    }
}
