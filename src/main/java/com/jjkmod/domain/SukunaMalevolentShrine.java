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
import java.util.ArrayList;
import java.util.List;

public class SukunaMalevolentShrine extends Domain {
    private final ServerPlayerEntity owner;
    private final ServerWorld world;
    private int tickCounter = 0;
    private final List<BlockPos> shrineBlocks = new ArrayList<>();
    private static final int SHRINE_SIZE = 5;

    public SukunaMalevolentShrine(ServerPlayerEntity owner, BlockPos center) {
        super("malevolent_shrine", "\u00a74Malevolent Shrine", center, 35);
        this.owner = owner;
        this.world = owner.getServerWorld();
        buildShrineStructure();
    }

    private void buildShrineStructure() {
        int x = center.getX();
        int y = center.getY();
        int z = center.getZ();

        // Foundation platform
        for (int i = -SHRINE_SIZE; i <= SHRINE_SIZE; i++) {
            for (int j = -SHRINE_SIZE; j <= SHRINE_SIZE; j++) {
                BlockPos pos = new BlockPos(x + i, y - 1, z + j);
                if (Math.abs(i) <= SHRINE_SIZE && Math.abs(j) <= SHRINE_SIZE) {
                    world.setBlockState(pos, Blocks.OBSIDIAN.getDefaultState());
                    shrineBlocks.add(pos);
                }
            }
        }

        // Central altar pillars
        for (int pillar = 0; pillar < 4; pillar++) {
            int px = x + (pillar < 2 ? -3 : 3);
            int pz = z + ((pillar % 2) == 0 ? -3 : 3);
            for (int h = 0; h < 6; h++) {
                BlockPos pos = new BlockPos(px, y + h, pz);
                world.setBlockState(pos, Blocks.DARK_OAK_WOOD.getDefaultState());
                shrineBlocks.add(pos);
            }
        }

        // Shrine decorations
        for (int i = -2; i <= 2; i++) {
            BlockPos pos = new BlockPos(x + i, y + 6, z);
            world.setBlockState(pos, Blocks.BLACKSTONE.getDefaultState());
            shrineBlocks.add(pos);
            pos = new BlockPos(x, y + 6, z + i);
            world.setBlockState(pos, Blocks.BLACKSTONE.getDefaultState());
            shrineBlocks.add(pos);
        }
    }

    @Override
    public void onEntityEnter(Object entity) {
        if (entity instanceof LivingEntity livingEntity && livingEntity != owner) {
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 200, 2, false, false));
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 150, 1, false, false));
            livingEntity.damage(owner.getWorld().getDamageSources().magic(), 4.0F);

            if (livingEntity instanceof ServerPlayerEntity player) {
                player.sendMessage(Text.literal("\u00a74You are trapped in Sukuna's Malevolent Shrine!"), false);
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
            if (tickCounter % 12 == 0) {
                onEntityEnter(entity);
            }

            // Constant slash pressure
            if (tickCounter % 20 == 0) {
                entity.damage(owner.getWorld().getDamageSources().magic(), 1.5F);
                double angle = Math.random() * Math.PI * 2;
                entity.addVelocity(
                    Math.cos(angle) * 0.4,
                    0.1,
                    Math.sin(angle) * 0.4
                );
            }
        }

        // Rotating red pressure aura
        if (tickCounter % 4 == 0) {
            double angle = (tickCounter / 4.0) * 0.15;
            for (int i = 0; i < 16; i++) {
                double x = center.getX() + Math.cos(angle + i * Math.PI / 8) * (radius - 5);
                double y = center.getY() + 3;
                double z = center.getZ() + Math.sin(angle + i * Math.PI / 8) * (radius - 5);
                world.spawnParticles(ParticleTypes.FLAME, x, y, z, 2, 0.3, 0.3, 0.3, 0.08);
            }
        }

        // Occasional slash visual
        if (tickCounter % 30 == 0) {
            Vec3d targetPos = entities.isEmpty() ? center.toCenterPos() : entities.get(0).getPos();
            Vec3d direction = targetPos.subtract(center.toCenterPos()).normalize();
            for (double d = 0; d < radius; d += 2) {
                Vec3d scanPos = center.toCenterPos().add(direction.multiply(d));
                world.spawnParticles(ParticleTypes.FLAME, scanPos.x, scanPos.y, scanPos.z, 1, 0.2, 0.2, 0.2, 0.05);
            }
        }
    }

    @Override
    public void onDomainEnd() {
        for (BlockPos pos : shrineBlocks) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
        shrineBlocks.clear();
        owner.sendMessage(Text.literal("\u00a7cMalevolent Shrine has dissipated."), false);
    }
}
