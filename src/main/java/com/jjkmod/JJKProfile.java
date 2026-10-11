package com.jjkmod;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.network.ServerPlayerEntity;

public final class JJKProfile {
    private String sorcererId = "gojo";
    private String techniqueId = "limitless";
    private int level = 1;
    private int cursedEnergy = 100;
    private int maxCursedEnergy = 100;
    private int power = 10;
    private int speed = 10;
    private int control = 10;
    private boolean domainUnlocked = false;
    private boolean sixEyesUnlocked = true;

    public static JJKProfile fromNbt(NbtCompound tag) {
        JJKProfile profile = new JJKProfile();
        profile.setSorcererId(tag.contains("sorcererId") ? tag.getString("sorcererId") : "gojo");
        profile.setTechniqueId(tag.contains("techniqueId") ? tag.getString("techniqueId") : "limitless");
        profile.setLevel(tag.contains("level") ? tag.getInt("level") : 1);
        profile.setCursedEnergy(tag.contains("cursedEnergy") ? tag.getInt("cursedEnergy") : 100);
        profile.setMaxCursedEnergy(tag.contains("maxCursedEnergy") ? tag.getInt("maxCursedEnergy") : 100);
        profile.setPower(tag.contains("power") ? tag.getInt("power") : 10);
        profile.setSpeed(tag.contains("speed") ? tag.getInt("speed") : 10);
        profile.setControl(tag.contains("control") ? tag.getInt("control") : 10);
        profile.setDomainUnlocked(tag.contains("domainUnlocked") && tag.getBoolean("domainUnlocked"));
        profile.setSixEyesUnlocked(tag.contains("sixEyesUnlocked") && tag.getBoolean("sixEyesUnlocked"));
        return profile;
    }

    public NbtCompound toNbt() {
        NbtCompound tag = new NbtCompound();
        tag.putString("sorcererId", sorcererId);
        tag.putString("techniqueId", techniqueId);
        tag.putInt("level", level);
        tag.putInt("cursedEnergy", cursedEnergy);
        tag.putInt("maxCursedEnergy", maxCursedEnergy);
        tag.putInt("power", power);
        tag.putInt("speed", speed);
        tag.putInt("control", control);
        tag.putBoolean("domainUnlocked", domainUnlocked);
        tag.putBoolean("sixEyesUnlocked", sixEyesUnlocked);
        return tag;
    }

    public static void initialize(ServerPlayerEntity player) {
        JJKPlayerData.initialize(player);
    }

    public String getSorcererId() { return sorcererId; }
    public void setSorcererId(String sorcererId) { this.sorcererId = sorcererId; }

    public String getTechniqueId() { return techniqueId; }
    public void setTechniqueId(String techniqueId) { this.techniqueId = techniqueId; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public int getCursedEnergy() { return cursedEnergy; }
    public void setCursedEnergy(int cursedEnergy) { this.cursedEnergy = cursedEnergy; }

    public int getMaxCursedEnergy() { return maxCursedEnergy; }
    public void setMaxCursedEnergy(int maxCursedEnergy) { this.maxCursedEnergy = maxCursedEnergy; }

    public int getPower() { return power; }
    public void setPower(int power) { this.power = power; }

    public int getSpeed() { return speed; }
    public void setSpeed(int speed) { this.speed = speed; }

    public int getControl() { return control; }
    public void setControl(int control) { this.control = control; }

    public boolean isDomainUnlocked() { return domainUnlocked; }
    public void setDomainUnlocked(boolean domainUnlocked) { this.domainUnlocked = domainUnlocked; }

    public boolean isSixEyesUnlocked() { return sixEyesUnlocked; }
    public void setSixEyesUnlocked(boolean sixEyesUnlocked) { this.sixEyesUnlocked = sixEyesUnlocked; }
}
