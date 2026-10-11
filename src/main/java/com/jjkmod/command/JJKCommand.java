package com.jjkmod.command;

import com.jjkmod.JJKPlayerData;
import com.jjkmod.JJKProfile;
import com.jjkmod.JJKRegistries;
import com.jjkmod.domain.DomainManager;
import com.jjkmod.gojo.GojoAbilityManager;
import com.jjkmod.gojo.LimitlessSystem;
import com.jjkmod.gojo.SixEyesSystem;
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
                                context.getSource().sendFeedback(() -> Text.literal("\u00a76Level set to " + value), false);
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
                                    context.getSource().sendFeedback(() -> Text.literal("\u00a7cSorcerer not found: " + id), false);
                                    return 0;
                                }
                                JJKPlayerData.setSorcerer(player, id);
                                context.getSource().sendFeedback(() -> Text.literal("\u00a76Selected " + sorcerer.name()), false);
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
                        player.sendMessage(Text.literal("\u00a7cDomain ended."), false);
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
                                "\u00a76" + sorcererName + " \u00a77[Lv " + profile.getLevel() + "]\n" +
                                "\u00a7eEnergy: " + profile.getCursedEnergy() + "/" + profile.getMaxCursedEnergy() + "\n" +
                                "\u00a7aPower: " + profile.getPower() + " Speed: " + profile.getSpeed() + " Control: " + profile.getControl()
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
                                context.getSource().sendFeedback(() -> Text.literal("\u00a76Cursed Energy set to " + amount), false);
                                return 1;
                            })));

            // /jjk unlock
            root.then(CommandManager.literal("unlock")
                    .executes(context -> {
                        ServerPlayerEntity player = context.getSource().getPlayer();
                        if (player == null) return 0;
                        JJKPlayerData.unlockDomain(player);
                        JJKProfile profile = JJKPlayerData.get(player);
                        profile.setSixEyesUnlocked(true);
                        JJKPlayerData.save(player, profile);
                        context.getSource().sendFeedback(() -> Text.literal("\u00a76Domain and Six Eyes unlocked!"), false);
                        return 1;
                    }));

            // Gojo-specific commands
            root.then(CommandManager.literal("gojo")
                    .then(CommandManager.literal("sixeyes")
                            .executes(context -> {
                                ServerPlayerEntity player = context.getSource().getPlayer();
                                if (player == null) return 0;
                                SixEyesSystem eyes = GojoAbilityManager.getSixEyesSystem(player);
                                if (eyes == null) {
                                    context.getSource().sendFeedback(() -> Text.literal("\u00a7cYou are not Gojo!"), false);
                                    return 0;
                                }
                                eyes.activateSixEyes();
                                return 1;
                            }))
                    .then(CommandManager.literal("infinity")
                            .executes(context -> {
                                ServerPlayerEntity player = context.getSource().getPlayer();
                                if (player == null) return 0;
                                LimitlessSystem limitless = GojoAbilityManager.getLimitlessSystem(player);
                                if (limitless == null) {
                                    context.getSource().sendFeedback(() -> Text.literal("\u00a7cYou are not Gojo!"), false);
                                    return 0;
                                }
                                limitless.activateInfinity();
                                return 1;
                            }))
                    .then(CommandManager.literal("blue")
                            .executes(context -> {
                                ServerPlayerEntity player = context.getSource().getPlayer();
                                if (player == null) return 0;
                                LimitlessSystem limitless = GojoAbilityManager.getLimitlessSystem(player);
                                if (limitless == null) {
                                    context.getSource().sendFeedback(() -> Text.literal("\u00a7cYou are not Gojo!"), false);
                                    return 0;
                                }
                                limitless.activateBlue();
                                return 1;
                            }))
                    .then(CommandManager.literal("red")
                            .executes(context -> {
                                ServerPlayerEntity player = context.getSource().getPlayer();
                                if (player == null) return 0;
                                LimitlessSystem limitless = GojoAbilityManager.getLimitlessSystem(player);
                                if (limitless == null) {
                                    context.getSource().sendFeedback(() -> Text.literal("\u00a7cYou are not Gojo!"), false);
                                    return 0;
                                }
                                limitless.activateRed();
                                return 1;
                            }))
                    .then(CommandManager.literal("hollowpurple")
                            .executes(context -> {
                                ServerPlayerEntity player = context.getSource().getPlayer();
                                if (player == null) return 0;
                                LimitlessSystem limitless = GojoAbilityManager.getLimitlessSystem(player);
                                if (limitless == null) {
                                    context.getSource().sendFeedback(() -> Text.literal("\u00a7cYou are not Gojo!"), false);
                                    return 0;
                                }
                                limitless.activateHollowPurple();
                                return 1;
                            })));

            dispatcher.register(root);
        });
    }
}
