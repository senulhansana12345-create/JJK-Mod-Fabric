package com.jjkmod.command;

import com.jjkmod.JJKPlayerData;
import com.jjkmod.JJKProfile;
import com.jjkmod.JJKRegistries;
import com.jjkmod.domain.DomainManager;
import com.jjkmod.technique.SorcererDefinition;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public final class JJKCommand {
    private JJKCommand() {}

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            LiteralArgumentBuilder<ServerCommandSource> root = CommandManager.literal("jjk");

            root.then(CommandManager.literal("level")
                    .then(CommandManager.argument("value", IntegerArgumentType.integer(1, 100))
                            .executes(context -> {
                                ServerPlayerEntity player = context.getSource().getPlayer();
                                if (player == null) {
                                    return 0;
                                }
                                int value = IntegerArgumentType.getInteger(context, "value");
                                JJKPlayerData.setLevel(player, value);
                                context.getSource().sendFeedback(() -> Text.literal("Level set to " + value), false);
                                return 1;
                            })));

            root.then(CommandManager.literal("domain")
                    .executes(context -> {
                        ServerPlayerEntity player = context.getSource().getPlayer();
                        if (player == null) {
                            return 0;
                        }
                        DomainManager.activateDomain(player);
                        return 1;
                    }));

            root.then(CommandManager.literal("info")
                    .executes(context -> {
                        ServerPlayerEntity player = context.getSource().getPlayer();
                        if (player == null) {
                            return 0;
                        }
                        JJKProfile profile = JJKPlayerData.get(player);
                        SorcererDefinition sorcerer = JJKRegistries.getSorcerer(profile.getSorcererId());
                        context.getSource().sendFeedback(() -> Text.literal("Sorcerer: " + (sorcerer != null ? sorcerer.name() : profile.getSorcererId()) + ", Level: " + profile.getLevel()), false);
                        return 1;
                    }));

            dispatcher.register(root);
        });
    }
}
