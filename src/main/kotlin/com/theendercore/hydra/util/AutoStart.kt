package com.theendercore.hydra.util

import com.theendercore.hydra.HydraMod.Companion.LOGGER
import com.theendercore.hydra.HydraMod.Companion.MODID
import com.theendercore.hydra.HydraMod.Companion.twitchClient
import com.theendercore.hydra.config.ModConfig
import com.theendercore.hydra.twitch.TwitchBot
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents.ClientStopping
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object AutoStart {
    fun init() {

        ClientPlayConnectionEvents.JOIN.register(ClientPlayConnectionEvents.Join { _, _, _ ->
            if (ModConfig.config!!.autoStart && twitchClient == null) {
                Methods.chatMessage(
                    Text.translatable("system.$MODID.auto_load").formatted(Formatting.DARK_GRAY)
                )
                Thread{
                    TwitchBot.enable()
                }.start()
            }
        })

        ClientLifecycleEvents.CLIENT_STOPPING.register(ClientStopping {
            TwitchBot.disable()
            LOGGER.info("Disable Twitch bot.")
        })
    }
}