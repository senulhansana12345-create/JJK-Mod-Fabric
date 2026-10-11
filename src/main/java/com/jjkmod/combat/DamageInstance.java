package com.jjkmod.combat;

public class DamageInstance {
    public final float baseDamage;
    public final String damageType;  // "slash", "blunt", "energy", "curse"
    public final float knockbackForce;
    public final int duration; // effect duration in ticks
    public final float staggerChance; // 0.0 to 1.0

    public DamageInstance(
            float baseDamage,
            String damageType,
            float knockbackForce,
            int duration,
            float staggerChance
    ) {
        this.baseDamage = baseDamage;
        this.damageType = damageType;
        this.knockbackForce = knockbackForce;
        this.duration = duration;
        this.staggerChance = staggerChance;
    }

    public DamageInstance(
            float baseDamage,
            String damageType,
            float knockbackForce
    ) {
        this(baseDamage, damageType, knockbackForce, 0, 0.0f);
    }

    public static DamageInstance slashInstance(float damage, float knockback) {
        return new DamageInstance(damage, "slash", knockback, 60, 0.3f);
    }

    public static DamageInstance bluntInstance(float damage, float knockback) {
        return new DamageInstance(damage, "blunt", knockback, 40, 0.5f);
    }

    public static DamageInstance energyInstance(float damage, float knockback) {
        return new DamageInstance(damage, "energy", knockback, 80, 0.2f);
    }

    public static DamageInstance curseInstance(float damage, float knockback) {
        return new DamageInstance(damage, "curse", knockback, 100, 0.4f);
    }
}
