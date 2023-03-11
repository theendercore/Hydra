package com.theendercore.hydra.util

import com.theendercore.hydra.twitch.TwitchBot

@Suppress("unused")
object HydraCommands {
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

    fun test(): Int {
        return 1
    }
}