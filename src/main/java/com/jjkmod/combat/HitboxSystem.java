package com.jjkmod.combat;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import java.util.List;

public class HitboxSystem {
    public static final int DEFAULT_HITBOX_RANGE = 20;
    public static final double DEFAULT_HITBOX_WIDTH = 4.0;

    public static List<LivingEntity> getEntitiesInHitbox(
            LivingEntity source,
            double range,
            double width,
            double height
    ) {
        Vec3d sourcePos = source.getEyePos();
        Vec3d direction = source.getRotationVector().normalize();
        Vec3d forward = direction.multiply(range);
        Vec3d perpendicular = new Vec3d(-direction.z, 0, direction.x).normalize().multiply(width / 2);

        Box box = Box.of(
                sourcePos.add(forward.multiply(0.5)),
                width,
                height,
                range
        );

        return source.getWorld().getEntitiesByClass(
                LivingEntity.class,
                box,
                entity -> entity != source && entity.isAlive()
        );
    }

    public static List<LivingEntity> getEntitiesInRadius(
            LivingEntity source,
            double radius
    ) {
        Box box = Box.of(source.getPos(), radius * 2, radius * 2, radius * 2);
        return source.getWorld().getEntitiesByClass(
                LivingEntity.class,
                box,
                entity -> entity != source && entity.isAlive()
        );
    }

    public static List<LivingEntity> getEntitiesInCone(
            LivingEntity source,
            double range,
            double angle
    ) {
        Vec3d sourcePos = source.getEyePos();
        Vec3d direction = source.getRotationVector().normalize();

        Box box = Box.of(
                sourcePos.add(direction.multiply(range / 2)),
                range,
                range,
                range
        );

        return source.getWorld().getEntitiesByClass(
                LivingEntity.class,
                box,
                entity -> {
                    if (entity == source || !entity.isAlive()) return false;
                    Vec3d toEntity = entity.getEyePos().subtract(sourcePos).normalize();
                    double dot = direction.dotProduct(toEntity);
                    return dot > Math.cos(Math.toRadians(angle / 2));
                }
        );
    }
}
