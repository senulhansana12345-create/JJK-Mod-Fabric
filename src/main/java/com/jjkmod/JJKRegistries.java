package com.jjkmod;

import com.jjkmod.technique.SorcererDefinition;
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
                "One of the strongest sorcerers in the world, with Six Eyes, Limitless, and the ability to reshape space itself.",
                List.of("limitless", "infinity", "six_eyes", "blue", "red", "hollow_purple"),
                100,
                20,
                18
        ));

        registerSorcerer(new SorcererDefinition(
                "sukuna",
                "Ryomen Sukuna",
                "King of Curses",
                "Malevolent Shrine",
                "The most feared curse with devastating slashes, shrine-based pressure, and overwhelming authority over cursed energy.",
                List.of("dismantle", "cleave", "malevolent_shrine", "open_transfiguration"),
                120,
                25,
                15
        ));

        registerSorcerer(new SorcererDefinition(
                "yuji",
                "Yuji Itadori",
                "The Vessel",
                "Black Flash Domain",
                "A fearless vessel with raw power, instinctive momentum, and explosive impact techniques.",
                List.of("black_flash", "soul_shattering", "convergence", "divine_dogs"),
                90,
                16,
                14
        ));

        registerSorcerer(new SorcererDefinition(
                "megumi",
                "Megumi Fushiguro",
                "Ten Shadows User",
                "Divine Dogs Domain",
                "A disciplined and tactical shikigami user who controls the battlefield through geometry, summons, and pressure.",
                List.of("ten_shadows", "divine_dogs_megumi", "nue", "max elephant"),
                95,
                18,
                17
        ));

        registerSorcerer(new SorcererDefinition(
                "geto",
                "Suguru Geto",
                "Cursed Womb: Death Painting",
                "Cursed Womb Domain",
                "A cursed manipulator whose domain and cursed techniques create fear, control, and horror on the battlefield.",
                List.of("dismantle", "malevolent_shrine", "open_transfiguration"),
                110,
                18,
                16
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
