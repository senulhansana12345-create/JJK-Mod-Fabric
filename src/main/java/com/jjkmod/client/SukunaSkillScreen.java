package com.jjkmod.client;

import com.jjkmod.JJKPlayerData;
import com.jjkmod.JJKProfile;
import com.jjkmod.JJKRegistries;
import com.jjkmod.sukuna.SukunaAbilityManager;
import com.jjkmod.technique.SorcererDefinition;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class SukunaSkillScreen extends Screen {
    public SukunaSkillScreen() {
        super(Text.literal("Sukuna Skills - Malevolent"));
    }

    @Override
    protected void init() {
        super.init();
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        // Dismantle button
        addDrawableChild(ButtonWidget.builder(
                Text.literal("[S1] Dismantle"),
                button -> {
                    if (client.player != null) {
                        SukunaAbilityManager.executeDismantleSlash((net.minecraft.server.network.ServerPlayerEntity) client.player);
                    }
                }
        ).dimensions(20, 40, 150, 20).build());

        // Cleave button
        addDrawableChild(ButtonWidget.builder(
                Text.literal("[S2] Cleave"),
                button -> {
                    if (client.player != null) {
                        SukunaAbilityManager.executeCleaveTechnique((net.minecraft.server.network.ServerPlayerEntity) client.player);
                    }
                }
        ).dimensions(20, 70, 150, 20).build());

        // Close button
        addDrawableChild(ButtonWidget.builder(
                Text.literal("Close"),
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
        SorcererDefinition sukuna = JJKRegistries.getSorcerer("sukuna");

        context.drawCenteredTextWithShadow(textRenderer, Text.literal("\u00a74Ryomen Sukuna - King of Curses"), width / 2, 15, 0xFFFFFF);
        context.drawText(textRenderer, Text.literal("Cursed Energy: " + profile.getCursedEnergy() + "/" + profile.getMaxCursedEnergy()), 20, 210, 0xFF0000, false);
        context.drawText(textRenderer, Text.literal("Power: " + profile.getPower() + " | Control: " + profile.getControl()), 20, 230, 0xFFFFFF, false);
        context.drawText(textRenderer, Text.literal("Domain: " + (sukuna != null ? sukuna.domainName() : "Unknown")), 20, 250, 0xFFB3FF, false);
    }
}
