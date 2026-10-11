package com.jjkmod.technique;

public record TechniqueDefinition(
        String id,
        String name,
        String description,
        String element,
        int cooldownTicks,
        int range,
        int power
) {}
