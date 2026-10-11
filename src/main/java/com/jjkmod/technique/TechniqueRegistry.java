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

        // Gojo Techniques
        register(new TechniqueDefinition("limitless", "Limitless", "Infinite space control - stops all movement and forces infinite intervals between you and targets.", "space", 0, 32, 12));
        register(new TechniqueDefinition("infinity", "Infinity", "A protective barrier that repels all attacks and forces enemies away.", "space", 80, 20, 10));
        register(new TechniqueDefinition("hollow_purple", "Hollow Purple", "A devastating fusion of space manipulation and destructive force. Annihilates matter.", "void", 120, 40, 25));
        register(new TechniqueDefinition("six_eyes", "Six Eyes", "Rare ocular power that sees all cursed energy flows and enhances all techniques.", "vision", 0, 50, 15));
        register(new TechniqueDefinition("blue", "Blue", "An attractive cursed technique that pulls targets toward you with immense force.", "space", 60, 25, 18));
        register(new TechniqueDefinition("red", "Red", "A repulsive cursed technique that violently pushes targets away.", "space", 60, 25, 18));

        // Sukuna Techniques
        register(new TechniqueDefinition("dismantle", "Dismantle", "A slicing cursed technique that cuts through targets with precision.", "cleave", 30, 20, 14));
        register(new TechniqueDefinition("cleave", "Cleave", "A devastating slash that compresses cutting power into a fatal strike.", "slash", 40, 22, 18));
        register(new TechniqueDefinition("malevolent_shrine", "Malevolent Shrine", "A shrine-based domain filled with cursed energy and terrifying pressure.", "domain", 100, 35, 22));
        register(new TechniqueDefinition("open_transfiguration", "Open Transfiguration", "Sukuna reshapes his body for combat superiority.", "mutation", 50, 3, 16));

        // Yuji Techniques
        register(new TechniqueDefinition("black_flash", "Black Flash", "A micro-time burst that amplifies impact to catastrophic levels.", "impact", 50, 15, 20));
        register(new TechniqueDefinition("divine_dogs", "Divine Dogs", "Shikigami summons that pressure and attack from all sides.", "summon", 40, 18, 12));
        register(new TechniqueDefinition("soul_shattering", "Soul Shattering", "A highly destructive technique aimed at the core of the target.", "soul", 60, 20, 19));
        register(new TechniqueDefinition("convergence", "Convergence", "Concentrate cursed energy for a powerful strike.", "impact", 35, 12, 15));

        // Megumi Techniques
        register(new TechniqueDefinition("ten_shadows", "Ten Shadows", "The power to summon and command a full arsenal of shikigami.", "summon", 45, 25, 18));
        register(new TechniqueDefinition("divine_dogs_megumi", "Divine Dogs", "Two protective dogs for offense and defense.", "summon", 40, 20, 11));
        register(new TechniqueDefinition("nue", "Nue", "A bird-like shikigami for aerial combat and crowd control.", "summon", 50, 28, 14));
        register(new TechniqueDefinition("max elephant", "Max Elephant", "The most powerful shikigami - a massive elephant for devastating attacks.", "summon", 80, 30, 21));
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
