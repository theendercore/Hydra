package com.theendercore.hydra.util

import com.mojang.brigadier.context.CommandContext
import com.theendercore.hydra.twitch.TwitchBot
import com.theendercore.hydra.util.Methods.playParticle
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.particle.ParticleTypes
import net.minecraft.text.Text


object HydraCommand {


    fun enable(): Int {
        var x = 0
        Thread {
            x = TwitchBot.enable()
        }.start()
        return x
    }

    fun disable(): Int {
        return TwitchBot.disable()
    }

    fun test(context: CommandContext<FabricClientCommandSource>): Int {
        val source = context.source
        val player = source.player

        player.sendMessage(Text.of("Test"))

        playParticle(player, ParticleTypes.TOTEM_OF_UNDYING)

        return 1
    }
}