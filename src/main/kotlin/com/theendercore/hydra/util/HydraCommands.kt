package com.theendercore.hydra.util

import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.theendercore.hydra.twitch.TwitchBot
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object HydraCommands {
    @Throws(CommandSyntaxException::class)
    fun enable(context: CommandContext<FabricClientCommandSource?>?): Int {
        return TwitchBot.enable()
    }

    @Throws(CommandSyntaxException::class)
    fun disable(context: CommandContext<FabricClientCommandSource?>?): Int {
        return TwitchBot.disable()
    }

    @Throws(CommandSyntaxException::class)
    fun test(context: CommandContext<FabricClientCommandSource?>?): Int {
        return 1
    }
}