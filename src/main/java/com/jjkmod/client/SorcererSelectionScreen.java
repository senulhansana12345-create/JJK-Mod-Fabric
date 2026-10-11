package com.jjkmod.client;

import com.jjkmod.JJKPlayerData;
import com.jjkmod.JJKProfile;
import com.jjkmod.JJKRegistries;
import com.jjkmod.technique.SorcererDefinition;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import java.util.List;

public class SorcererSelectionScreen extends Screen {
    public SorcererSelectionScreen() {
        super(Text.literal("Sorcerer Selection"));
    }

    @Override
    protected void init() {
        super.init();
        List<SorcererDefinition> sorcerers = JJKRegistries.getAllSorcerers();

        int y = 40;
        for (SorcererDefinition sorcerer : sorcerers) {
            addDrawableChild(ButtonWidget.builder(
                    Text.literal(sorcerer.name()),
                    button -> {
                        MinecraftClient client = MinecraftClient.getInstance();
                        if (client.player != null) {
                            JJKPlayerData.setSorcerer(client.player, sorcerer.id());
                            JJKPlayerData.setTechnique(client.player, sorcerer.techniqueIds().getFirst());
                            client.player.sendMessage(Text.literal("Selected: " + sorcerer.name()), false);
                        }
                        client.setScreen(new PlayerDetailScreen(sorcerer.id()));
                    }
            ).dimensions(80, y, 220, 22).build());
            y += 30;
        }

        addDrawableChild(ButtonWidget.builder(
                Text.literal("Close"),
                button -> close()
        ).dimensions(width - 120, height - 30, 100, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, Text.literal("Choose Your Sorcerer"), width / 2, 18, 0xFFFFFF);
    }
}
