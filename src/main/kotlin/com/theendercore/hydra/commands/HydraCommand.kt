package com.theendercore.hydra.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import com.theendercore.hydra.config.ModConfig
import com.theendercore.hydra.twitch.TwitchBot
import com.theendercore.hydra.util.Methods.addChatMsg
import com.theendercore.hydra.util.Methods.playParticle
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.particle.ParticleTypes


object HydraCommand {
    fun register(dispatcher: CommandDispatcher<FabricClientCommandSource>) {
        val hydraNode = literal("hydra").build()
        dispatcher.root.addChild(hydraNode)

        val enableNode = literal("enable")
            .executes { enable() }
            .build()
        hydraNode.addChild(enableNode)

        val disableNode = literal("disable")
            .executes { disable() }
            .build()
        hydraNode.addChild(disableNode)

        val testNode = literal("test")
            .executes { test(it) }
            .build()
        hydraNode.addChild(testNode)
    }

    private fun enable(): Int {
        var x = 0
        Thread { x = TwitchBot.enable() }.start()
        return x
    }

    private fun disable(): Int {
        return TwitchBot.disable()
    }

    private fun test(context: CommandContext<FabricClientCommandSource>): Int {
        val source = context.source
        val player = source.player

        addChatMsg(ModConfig.config?.username ?: "n")
        playParticle(player, ParticleTypes.TOTEM_OF_UNDYING)

        return 1
    }
}