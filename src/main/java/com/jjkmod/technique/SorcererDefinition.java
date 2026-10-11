package com.jjkmod.technique;

import java.util.List;

public record SorcererDefinition(
        String id,
        String name,
        String title,
        String domainName,
        String description,
        List<String> techniqueIds,
        int cursedEnergy,
        int power,
        int control
) {}
