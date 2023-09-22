package com.theendercore.hydra.init

import com.theendercore.hydra.commands.HydraCommand
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback

object CommandRegistry {
    fun init() {
        ClientCommandRegistrationCallback.EVENT.register(ClientCommandRegistrationCallback { dispatcher, _ ->
            val hydraNode = ClientCommandManager.literal("hydra").build()
            val enableNode = ClientCommandManager.literal("enable")
                .executes { HydraCommand.enable() }
                .build()
            val disableNode = ClientCommandManager.literal("disable")
                .executes { HydraCommand.disable() }
                .build()
            val testNode = ClientCommandManager.literal("test")
                .executes { HydraCommand.test(it) }
                .build()


            dispatcher.root.addChild(hydraNode)
            hydraNode.addChild(enableNode)
            hydraNode.addChild(disableNode)
            hydraNode.addChild(testNode)
        })
    }
}