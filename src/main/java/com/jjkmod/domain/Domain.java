package com.jjkmod.domain;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public abstract class Domain {
    protected final String id;
    protected final String displayName;
    protected final BlockPos center;
    protected final int radius;
    protected final long creationTime;
    protected boolean active;

    public Domain(String id, String displayName, BlockPos center, int radius) {
        this.id = id;
        this.displayName = displayName;
        this.center = center;
        this.radius = radius;
        this.creationTime = System.currentTimeMillis();
        this.active = true;
    }

    public abstract void onEntityEnter(Object entity);
    public abstract void onDomainTick();
    public abstract void onDomainEnd();

    public String getId() { return id; }
    public String getDisplayName() { return displayName; }
    public BlockPos getCenter() { return center; }
    public int getRadius() { return radius; }
    public boolean isActive() { return active; }
    public void deactivate() { this.active = false; }
}
