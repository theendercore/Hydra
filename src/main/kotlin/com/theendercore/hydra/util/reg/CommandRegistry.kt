package com.theendercore.hydra.util.reg

import com.theendercore.hydra.util.HydraCommands
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback

object CommandRegistry {
    fun init() {
        ClientCommandRegistrationCallback.EVENT.register(ClientCommandRegistrationCallback { dispatcher, _ ->
            val hydraNode = ClientCommandManager.literal("hydra").build()
            val enableNode = ClientCommandManager.literal("enable")
                .executes { HydraCommands.enable() }
                .build()
            val disableNode = ClientCommandManager.literal("disable")
                .executes { HydraCommands.disable() }
                .build()
            val testNode = ClientCommandManager.literal("test")
                .executes { HydraCommands.test() }
                .build()
            dispatcher.root.addChild(hydraNode)
            hydraNode.addChild(enableNode)
            hydraNode.addChild(disableNode)
            hydraNode.addChild(testNode)
        })
    }
}