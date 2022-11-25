package com.theendercore.hydra.util

import com.theendercore.hydra.MODID
import com.theendercore.hydra.config.ModConfig
import com.theendercore.hydra.twitch.TwitchBot
import com.theendercore.hydra.twitchClient
import com.theendercore.hydra.LOGGER
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