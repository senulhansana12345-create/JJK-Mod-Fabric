package com.jjkmod;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.network.ServerPlayerEntity;

public final class JJKPlayerData {
    private static final String TAG = "jjkmod_player_data";

    private JJKPlayerData() {
    }

    public static JJKProfile get(ServerPlayerEntity player) {
        NbtCompound root = player.getPersistentData();
        if (!root.contains(TAG, NbtElement.COMPOUND_TYPE)) {
            root.put(TAG, new NbtCompound());
        }
        return JJKProfile.fromNbt(root.getCompound(TAG));
    }

    public static void save(ServerPlayerEntity player, JJKProfile profile) {
        player.getPersistentData().put(TAG, profile.toNbt());
    }

    public static void initialize(ServerPlayerEntity player) {
        NbtCompound root = player.getPersistentData();
        if (!root.contains(TAG, NbtElement.COMPOUND_TYPE)) {
            root.put(TAG, new NbtCompound());
            save(player, new JJKProfile());
        }
    }

    public static void setSorcerer(ServerPlayerEntity player, String sorcererId) {
        JJKProfile profile = get(player);
        profile.setSorcererId(sorcererId);
        save(player, profile);
    }

    public static void setTechnique(ServerPlayerEntity player, String techniqueId) {
        JJKProfile profile = get(player);
        profile.setTechniqueId(techniqueId);
        save(player, profile);
    }

    public static void setLevel(ServerPlayerEntity player, int level) {
        JJKProfile profile = get(player);
        profile.setLevel(Math.max(1, Math.min(level, 100)));
        save(player, profile);
    }

    public static void addCursedEnergy(ServerPlayerEntity player, int amount) {
        JJKProfile profile = get(player);
        profile.setCursedEnergy(Math.min(profile.getCursedEnergy() + amount, profile.getMaxCursedEnergy()));
        save(player, profile);
    }

    public static void consumeCursedEnergy(ServerPlayerEntity player, int amount) {
        JJKProfile profile = get(player);
        profile.setCursedEnergy(Math.max(0, profile.getCursedEnergy() - amount));
        save(player, profile);
    }

    public static void unlockDomain(ServerPlayerEntity player) {
        JJKProfile profile = get(player);
        profile.setDomainUnlocked(true);
        save(player, profile);
    }

    public static void setDomainActive(ServerPlayerEntity player, boolean active) {
        JJKProfile profile = get(player);
        profile.setDomainActive(active);
        save(player, profile);
    }
}
