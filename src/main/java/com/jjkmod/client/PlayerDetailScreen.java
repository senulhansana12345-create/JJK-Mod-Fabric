package com.jjkmod.client;

import com.jjkmod.JJKPlayerData;
import com.jjkmod.JJKProfile;
import com.jjkmod.JJKRegistries;
import com.jjkmod.technique.SorcererDefinition;
import com.jjkmod.technique.TechniqueDefinition;
import com.jjkmod.technique.TechniqueRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class PlayerDetailScreen extends Screen {
    private final String sorcererId;

    public PlayerDetailScreen(String sorcererId) {
        super(Text.literal("Player Details"));
        this.sorcererId = sorcererId;
    }

    @Override
    protected void init() {
        super.init();
        addDrawableChild(ButtonWidget.builder(
                Text.literal("Back"),
                button -> close()
        ).dimensions(width - 120, height - 30, 100, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) {
            return;
        }

        JJKProfile profile = JJKPlayerData.get(client.player);
        SorcererDefinition sorcerer = JJKRegistries.getSorcerer(profile.getSorcererId());

        String header = (sorcerer != null ? sorcerer.name() : "Unknown") + " // " + profile.getLevel();
        context.drawCenteredTextWithShadow(textRenderer, Text.literal(header), width / 2, 18, 0xFFD166);

        int y = 50;
        context.drawText(textRenderer, Text.literal("Technique: " + profile.getTechniqueId()), 30, y, 0xFFFFFF, false);
        y += 18;
        context.drawText(textRenderer, Text.literal("Cursed Energy: " + profile.getCursedEnergy() + "/" + profile.getMaxCursedEnergy()), 30, y, 0xFFFFFF, false);
        y += 18;
        context.drawText(textRenderer, Text.literal("Power: " + profile.getPower() + "   Speed: " + profile.getSpeed() + "   Control: " + profile.getControl()), 30, y, 0xFFFFFF, false);
        y += 24;

        if (sorcerer != null) {
            context.drawText(textRenderer, Text.literal("Domain: " + sorcerer.domainName()), 30, y, 0xFFB3FF, false);
            y += 18;
            context.drawText(textRenderer, Text.literal("Description: " + sorcerer.description()), 30, y, 0xD7D7D7, false);
            y += 18;
            context.drawText(textRenderer, Text.literal("Technique List:"), 30, y, 0x9EFFE0, false);
            y += 18;
            for (String techniqueId : sorcerer.techniqueIds()) {
                TechniqueDefinition technique = TechniqueRegistry.get(techniqueId);
                context.drawText(textRenderer, Text.literal("- " + (technique != null ? technique.name() : techniqueId)), 30, y, 0xFFFFFF, false);
                y += 16;
            }
        }
    }
}
