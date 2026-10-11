package com.jjkmod.client;

import com.jjkmod.JJKRegistries;
import com.jjkmod.technique.SorcererDefinition;
import com.jjkmod.technique.TechniqueDefinition;
import com.jjkmod.technique.TechniqueRegistry;
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
                        ClientSelectionManager.setSelectedSorcererId(sorcerer.id());
                        MinecraftClient.getInstance().setScreen(new PlayerDetailScreen(sorcerer.id()));
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
