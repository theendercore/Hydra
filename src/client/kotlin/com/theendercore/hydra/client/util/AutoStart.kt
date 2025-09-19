package com.theendercore.hydra.client.util

import com.theendercore.hydra.client.HydraMod
import com.theendercore.hydra.client.config.ModConfig
import com.theendercore.hydra.client.twitch.TwitchBot
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents.ClientStopping
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents

object AutoStart {
    fun init() {
        ClientPlayConnectionEvents.JOIN.register { _, _, _ ->
            if (ModConfig.Companion.config!!.autoStart && HydraMod.twitchClient == null) {
                Methods.addChatMsg(darkGrayText("system.${HydraMod.MODID}.auto_load"))
                Thread { TwitchBot.enable() }.start()
            }
        }

        ClientLifecycleEvents.CLIENT_STOPPING.register(ClientStopping {
            TwitchBot.disable()
            HydraMod.LOGGER.info("Disable Twitch bot.")
        })
    }
}