package com.theendercore.hydra.client.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import com.theendercore.hydra.client.HydraMod.config
import com.theendercore.hydra.client.twitch.TwitchBot
import com.theendercore.hydra.client.util.addChatMsg
import com.theendercore.hydra.client.util.playParticle
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.core.particles.ParticleTypes


object HydraCommand {
    fun register(dispatcher: CommandDispatcher<FabricClientCommandSource>) {
        val hydraNode = literal("hydra").build()
        dispatcher.root.addChild(hydraNode)

        val enableNode = literal("enable").executes { enable() }.build()
        hydraNode.addChild(enableNode)

        val disableNode = literal("disable").executes { disable() }.build()
        hydraNode.addChild(disableNode)

        val testNode = literal("test").executes(::test).build()
        hydraNode.addChild(testNode)
    }

    private fun enable(): Int {
        Thread { TwitchBot.enable() }.start()
        return 0
    }

    private fun disable(): Int {
        return TwitchBot.disable()
    }

    private fun test(context: CommandContext<FabricClientCommandSource>): Int {
        val source = context.source
        val player = source.player

//        addChatMsg(config.credentials.username)
        playParticle(player, ParticleTypes.TOTEM_OF_UNDYING)

        return 1
    }
}