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

public class RosterMasteryScreen extends Screen {
    public RosterMasteryScreen() {
        super(Text.literal("Sorcerer Mastery Records"));
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
        if (client.player == null) return;

        JJKProfile profile = JJKPlayerData.get((net.minecraft.server.network.ServerPlayerEntity) client.player);

        context.drawCenteredTextWithShadow(textRenderer, Text.literal("\u00a76Sorcerer Mastery Records"), width / 2, 15, 0xFFFFFF);

        int y = 40;
        context.drawText(textRenderer, Text.literal("\u00a7eYour Sorcerer: " + profile.getSorcererId().toUpperCase()), 20, y, 0xFFFFFF, false);
        y += 20;

        for (SorcererDefinition sorcerer : JJKRegistries.getAllSorcerers()) {
            boolean isSelected = sorcerer.id().equals(profile.getSorcererId());
            int color = isSelected ? 0xFFD700 : 0xAAAAAA;

            context.fill(15, y - 2, width - 15, y + 45, isSelected ? 0x55111111 : 0x22000000);
            context.drawText(textRenderer, Text.literal("\u00a7e" + sorcerer.name() + (isSelected ? " [CURRENT]" : "")), 25, y, color, false);
            y += 12;
            context.drawText(textRenderer, Text.literal("Title: " + sorcerer.title()), 35, y, 0x999999, false);
            y += 10;
            context.drawText(textRenderer, Text.literal("Stats - Pwr: " + sorcerer.power() + " | Ctl: " + sorcerer.control() + " | Energy: " + sorcerer.cursedEnergy()), 35, y, 0x777777, false);
            y += 15;
        }
    }
}
