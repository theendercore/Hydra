package com.theendercore.hydra.util

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.command.CommandRegistryAccess

object CommandRegistry {
    fun init() {
        ClientCommandRegistrationCallback.EVENT.register(ClientCommandRegistrationCallback { dispatcher, _ ->
            val hydraNode = ClientCommandManager.literal("hydra").build()
            val enableNode = ClientCommandManager.literal("enable")
                .executes { HydraCommands.enable(it) }
                .build()
            val disableNode = ClientCommandManager.literal("disable")
                .executes { HydraCommands.disable(it) }
                .build()
            val testNode = ClientCommandManager.literal("test")
                .executes { HydraCommands.test(it) }
                .build()
            dispatcher.root.addChild(hydraNode)
            hydraNode.addChild(enableNode)
            hydraNode.addChild(disableNode)
            hydraNode.addChild(testNode)
        })
    }
}