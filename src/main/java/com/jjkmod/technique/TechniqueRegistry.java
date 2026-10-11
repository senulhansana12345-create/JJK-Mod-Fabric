package com.jjkmod.technique;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class TechniqueRegistry {
    private static final Map<String, TechniqueDefinition> TECHNIQUES = new LinkedHashMap<>();

    public static void registerDefaults() {
        if (!TECHNIQUES.isEmpty()) {
            return;
        }

        register(new TechniqueDefinition("limitless", "Limitless", "Infinite space control and boundless manipulation.", "space", 24, 24, 12));
        register(new TechniqueDefinition("hollow_purple", "Hollow Purple", "An advanced fusion technique of infinite space and destructive force.", "void", 60, 32, 18));
        register(new TechniqueDefinition("six_eyes", "Six Eyes", "A rare ocular blessing that amplifies perception, precision, and cursed energy efficiency.", "vision", 0, 20, 10));
        register(new TechniqueDefinition("dismantle", "Dismantle", "A slicing cursed technique that cuts through targets with precision.", "cleave", 12, 18, 11));
        register(new TechniqueDefinition("cleave", "Cleave", "A devastating slash that compresses cutting power into a fatal strike.", "slash", 18, 20, 15));
        register(new TechniqueDefinition("malevolent_shrine", "Malevolent Shrine", "A shrine-based domain filled with cursed energy and terrifying pressure.", "domain", 70, 28, 20));
        register(new TechniqueDefinition("black_flash", "Black Flash", "A micro-time burst that amplifies impact to catastrophic levels.", "impact", 30, 12, 14));
        register(new TechniqueDefinition("divine_dogs", "Divine Dogs", "Shikigami summons that pressure and attack from all sides.", "summon", 25, 20, 12));
        register(new TechniqueDefinition("soul_shattering", "Soul Shattering", "A highly destructive technique aimed at the core of the target.", "soul", 40, 16, 16));
        register(new TechniqueDefinition("ten_shadows", "Ten Shadows", "The power to summon and command a full arsenal of shikigami.", "summon", 18, 24, 13));
    }

    public static void register(TechniqueDefinition definition) {
        TECHNIQUES.put(definition.id(), definition);
    }

    public static TechniqueDefinition get(String id) {
        return TECHNIQUES.get(id);
    }

    public static List<TechniqueDefinition> getAll() {
        return new ArrayList<>(TECHNIQUES.values());
    }
}
