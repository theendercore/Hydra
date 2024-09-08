package com.theendercore.hydra.util

import com.theendercore.hydra.HydraMod.LOGGER
import com.theendercore.hydra.HydraMod.MODID
import com.theendercore.hydra.HydraMod.twitchClient
import com.theendercore.hydra.config.ModConfig
import com.theendercore.hydra.twitch.TwitchBot
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents.ClientStopping
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents

object AutoStart {
    fun init() {
        ClientPlayConnectionEvents.JOIN.register { _, _, _ ->
            if (ModConfig.config!!.autoStart && twitchClient == null) {
                Methods.addChatMsg(darkGrayText("system.$MODID.auto_load"))
                Thread { TwitchBot.enable() }.start()
            }
        }

        ClientLifecycleEvents.CLIENT_STOPPING.register(ClientStopping {
            TwitchBot.disable()
            LOGGER.info("Disable Twitch bot.")
        })
    }
}