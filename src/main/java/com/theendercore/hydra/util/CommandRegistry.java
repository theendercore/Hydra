package com.theendercore.hydra.util;

import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class CommandRegistry {

    public static void init() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {

            LiteralCommandNode<FabricClientCommandSource> hydraNode = ClientCommandManager.literal("hydra").build();
            LiteralCommandNode<FabricClientCommandSource> enableNode = ClientCommandManager.literal("enable").executes(HydraCommands::enable).build();
            LiteralCommandNode<FabricClientCommandSource> disableNode = ClientCommandManager.literal("disable").executes(HydraCommands::disable).build();
            LiteralCommandNode<FabricClientCommandSource> testNode = ClientCommandManager.literal("test").executes(HydraCommands::test).build();

            dispatcher.getRoot().addChild(hydraNode);
            hydraNode.addChild(enableNode);
            hydraNode.addChild(disableNode);
            hydraNode.addChild(testNode);
        });
    }
}
