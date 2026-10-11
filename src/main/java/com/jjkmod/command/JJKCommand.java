package com.jjkmod.command;

import com.jjkmod.JJKPlayerData;
import com.jjkmod.JJKProfile;
import com.jjkmod.JJKRegistries;
import com.jjkmod.domain.DomainManager;
import com.jjkmod.technique.SorcererDefinition;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public final class JJKCommand {
    private JJKCommand() {}

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            LiteralArgumentBuilder<ServerCommandSource> root = CommandManager.literal("jjk");

            // /jjk level <value>
            root.then(CommandManager.literal("level")
                    .then(CommandManager.argument("value", IntegerArgumentType.integer(1, 100))
                            .executes(context -> {
                                ServerPlayerEntity player = context.getSource().getPlayer();
                                if (player == null) return 0;
                                int value = IntegerArgumentType.getInteger(context, "value");
                                JJKPlayerData.setLevel(player, value);
                                context.getSource().sendFeedback(() -> Text.literal("§6Level set to " + value), false);
                                return 1;
                            })));

            // /jjk sorcerer <id>
            root.then(CommandManager.literal("sorcerer")
                    .then(CommandManager.argument("id", StringArgumentType.string())
                            .executes(context -> {
                                ServerPlayerEntity player = context.getSource().getPlayer();
                                if (player == null) return 0;
                                String id = StringArgumentType.getString(context, "id");
                                SorcererDefinition sorcerer = JJKRegistries.getSorcerer(id);
                                if (sorcerer == null) {
                                    context.getSource().sendFeedback(() -> Text.literal("§cSorcerer not found: " + id), false);
                                    return 0;
                                }
                                JJKPlayerData.setSorcerer(player, id);
                                context.getSource().sendFeedback(() -> Text.literal("§6Selected " + sorcerer.name()), false);
                                return 1;
                            })));

            // /jjk domain
            root.then(CommandManager.literal("domain")
                    .executes(context -> {
                        ServerPlayerEntity player = context.getSource().getPlayer();
                        if (player == null) return 0;
                        DomainManager.activateDomain(player);
                        return 1;
                    }));

            // /jjk enddomain
            root.then(CommandManager.literal("enddomain")
                    .executes(context -> {
                        ServerPlayerEntity player = context.getSource().getPlayer();
                        if (player == null) return 0;
                        DomainManager.endDomain(player.getUuid());
                        player.sendMessage(Text.literal("§cDomain ended."), false);
                        return 1;
                    }));

            // /jjk info
            root.then(CommandManager.literal("info")
                    .executes(context -> {
                        ServerPlayerEntity player = context.getSource().getPlayer();
                        if (player == null) return 0;
                        JJKProfile profile = JJKPlayerData.get(player);
                        SorcererDefinition sorcerer = JJKRegistries.getSorcerer(profile.getSorcererId());
                        String sorcererName = sorcerer != null ? sorcerer.name() : profile.getSorcererId();
                        context.getSource().sendFeedback(() -> Text.literal(
                                "§6" + sorcererName + " §7[Lv " + profile.getLevel() + "]\n" +
                                "§eEnergy: " + profile.getCursedEnergy() + "/" + profile.getMaxCursedEnergy() + "\n" +
                                "§aPower: " + profile.getPower() + " Speed: " + profile.getSpeed() + " Control: " + profile.getControl()
                        ), false);
                        return 1;
                    }));

            // /jjk energy <amount>
            root.then(CommandManager.literal("energy")
                    .then(CommandManager.argument("amount", IntegerArgumentType.integer(0, 1000))
                            .executes(context -> {
                                ServerPlayerEntity player = context.getSource().getPlayer();
                                if (player == null) return 0;
                                int amount = IntegerArgumentType.getInteger(context, "amount");
                                JJKProfile profile = JJKPlayerData.get(player);
                                profile.setCursedEnergy(amount);
                                JJKPlayerData.save(player, profile);
                                context.getSource().sendFeedback(() -> Text.literal("§6Cursed Energy set to " + amount), false);
                                return 1;
                            })));

            // /jjk unlock
            root.then(CommandManager.literal("unlock")
                    .executes(context -> {
                        ServerPlayerEntity player = context.getSource().getPlayer();
                        if (player == null) return 0;
                        JJKPlayerData.unlockDomain(player);
                        context.getSource().sendFeedback(() -> Text.literal("§6Domain unlocked!"), false);
                        return 1;
                    }));

            dispatcher.register(root);
        });
    }
}
