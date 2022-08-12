package com.theendercore.hydra.util;

import com.mojang.brigadier.tree.LiteralCommandNode;
import com.theendercore.hydra.twitch.TwitchCommands;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class CommandRegistry {

    public static void init() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, dedicated) -> {

            LiteralCommandNode<ServerCommandSource> hydraNode = CommandManager.literal("hydra").build();
            LiteralCommandNode<ServerCommandSource> enableNode = CommandManager.literal("enable").executes(TwitchCommands::enable).build();
            LiteralCommandNode<ServerCommandSource> disableNode = CommandManager.literal("disable").executes(TwitchCommands::disable).build();
            LiteralCommandNode<ServerCommandSource> testNode = CommandManager.literal("test").executes(TwitchCommands::test).build();

            dispatcher.getRoot().addChild(hydraNode);
            hydraNode.addChild(enableNode);
            hydraNode.addChild(disableNode);
            hydraNode.addChild(testNode);
        });
    }
}
