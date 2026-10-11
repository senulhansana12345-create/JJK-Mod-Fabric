package com.jjkmod;

import com.jjkmod.technique.SorcererDefinition;
import com.jjkmod.technique.TechniqueDefinition;
import com.jjkmod.technique.TechniqueRegistry;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class JJKRegistries {
    private static final Map<String, SorcererDefinition> SORCERERS = new LinkedHashMap<>();

    public static void registerDefaults() {
        if (!SORCERERS.isEmpty()) {
            return;
        }

        TechniqueRegistry.registerDefaults();

        registerSorcerer(new SorcererDefinition(
                "gojo",
                "Satoru Gojo",
                "The Honored One",
                "Infinity Domain",
                "A prodigy with unparalleled control and philosophical intent.",
                List.of("limitless", "hollow_purple", "six_eyes"),
                100,
                12,
                13
        ));

        registerSorcerer(new SorcererDefinition(
                "sukuna",
                "Ryomen Sukuna",
                "King of Curses",
                "Malevolent Shrine",
                "A legendary curse with devastating destructive force and a shrine domain.",
                List.of("dismantle", "cleave", "malevolent_shrine"),
                120,
                15,
                14
        ));

        registerSorcerer(new SorcererDefinition(
                "yuji",
                "Yuji Itadori",
                "The Vessel",
                "Black Flash Domain",
                "A brave vessel with courage, instincts, and explosive growth.",
                List.of("black_flash", "divine_dogs", "soul_shattering"),
                90,
                10,
                11
        ));

        registerSorcerer(new SorcererDefinition(
                "megumi",
                "Megumi Fushiguro",
                "Ten Shadows User",
                "Divine Dogs Domain",
                "A strategic shikigami specialist with balanced offense and defense.",
                List.of("ten_shadows", "divine_dogs", "max elephant"),
                95,
                11,
                12
        ));
    }

    public static void registerSorcerer(SorcererDefinition definition) {
        SORCERERS.put(definition.id(), definition);
    }

    public static SorcererDefinition getSorcerer(String id) {
        return SORCERERS.get(id);
    }

    public static List<SorcererDefinition> getAllSorcerers() {
        return new ArrayList<>(SORCERERS.values());
    }

    public static int getSorcererCount() {
        return SORCERERS.size();
    }
}
