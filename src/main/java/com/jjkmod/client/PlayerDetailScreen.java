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
    private int scrollOffset = 0;

    public PlayerDetailScreen(String sorcererId) {
        super(Text.literal("Player Details"));
        this.sorcererId = sorcererId;
    }

    @Override
    protected void init() {
        super.init();
        
        // Skills button
        addDrawableChild(ButtonWidget.builder(
                Text.literal("View Skills"),
                button -> {
                    MinecraftClient client = MinecraftClient.getInstance();
                    if (sorcererId.equals("gojo")) {
                        client.setScreen(new GojoSkillScreen());
                    } else if (sorcererId.equals("sukuna")) {
                        client.setScreen(new SukunaSkillScreen());
                    }
                }
        ).dimensions(20, height - 60, 100, 20).build());
        
        addDrawableChild(ButtonWidget.builder(
                Text.literal("Back"),
                button -> MinecraftClient.getInstance().setScreen(new SorcererSelectionScreen())
        ).dimensions(width - 120, height - 30, 100, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        String selectedId = ClientSelectionManager.getSelectedSorcererId();
        SorcererDefinition sorcerer = JJKRegistries.getSorcerer(selectedId);
        if (sorcerer == null) {
            sorcerer = JJKRegistries.getSorcerer("gojo");
        }

        JJKProfile profile = JJKPlayerData.get((net.minecraft.server.network.ServerPlayerEntity) client.player);

        int y = 20;
        // Header
        context.drawCenteredTextWithShadow(textRenderer, Text.literal("\u00a76" + sorcerer.name() + " - " + sorcerer.title()), width / 2, y, 0xFFD166);
        y += 25;

        // Stats panel
        context.fill(15, y - 2, width - 15, y + 90, 0x55000000);
        context.drawText(textRenderer, Text.literal("\u00a7eLevel: " + profile.getLevel()), 25, y, 0xFFFFFF, false);
        y += 15;
        context.drawText(textRenderer, Text.literal("Cursed Energy: " + profile.getCursedEnergy() + "/" + profile.getMaxCursedEnergy()), 25, y, 0x00FF00, false);
        y += 15;
        context.drawText(textRenderer, Text.literal("Power: " + profile.getPower() + " | Speed: " + profile.getSpeed() + " | Control: " + profile.getControl()), 25, y, 0xFFFFFF, false);
        y += 15;
        context.drawText(textRenderer, Text.literal("Experience: " + profile.getExperience() + "/1000"), 25, y, 0xFFAA00, false);
        y += 25;

        // Domain info
        context.fill(15, y - 2, width - 15, y + 50, 0x55110055);
        context.drawText(textRenderer, Text.literal("\u00a7d" + sorcerer.domainName()), 25, y, 0xFFB3FF, false);
        y += 15;
        context.drawText(textRenderer, Text.literal("Domain Unlocked: " + (profile.isDomainUnlocked() ? "\u00a72Yes" : "\u00a7cNo")), 25, y, 0xFFFFFF, false);
        y += 20;

        // Description
        y += 10;
        context.drawText(textRenderer, Text.literal("\u00a77Description:"), 25, y, 0xD7D7D7, false);
        y += 12;
        String[] descLines = sorcerer.description().split(" ");
        StringBuilder line = new StringBuilder();
        for (String word : descLines) {
            if ((line.length() + word.length()) > 60) {
                context.drawText(textRenderer, Text.literal(line.toString()), 25, y, 0xAAAAAA, false);
                y += 10;
                line = new StringBuilder(word);
            } else {
                line.append(word).append(" ");
            }
        }
        if (line.length() > 0) {
            context.drawText(textRenderer, Text.literal(line.toString()), 25, y, 0xAAAAAA, false);
        }

        y += 20;
        // Techniques list
        context.drawText(textRenderer, Text.literal("\u00a79Techniques:"), 25, y, 0x9EFFE0, false);
        y += 12;
        for (String techniqueId : sorcerer.techniqueIds()) {
            TechniqueDefinition technique = TechniqueRegistry.get(techniqueId);
            if (technique != null) {
                context.drawText(textRenderer, Text.literal("\u00a7e- " + technique.name() + " (\u00a73" + technique.element() + "\u00a7e)"), 35, y, 0xFFFFFF, false);
                y += 12;
            }
        }
    }
}
