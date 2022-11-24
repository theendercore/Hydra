package com.theendercore.hydra.util

import com.theendercore.hydra.HydraMod
import com.theendercore.hydra.config.ModConfig
import com.theendercore.hydra.twitch.TwitchBot
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents.ClientStopping
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object AutoStart {
    fun init() {
        ClientPlayConnectionEvents.JOIN.register(ClientPlayConnectionEvents.Join { _, _, _ ->
            if (ModConfig.config!!.autoStart && HydraMod.twitchClient == null) {
                Methods.chatMessage(
                    Text.translatable("system." + HydraMod.MODID + ".auto_load").formatted(Formatting.DARK_GRAY)
                )
                Thread{
                    TwitchBot.enable()
                }.start()
            }
        })
        ClientLifecycleEvents.CLIENT_STOPPING.register(ClientStopping {
            TwitchBot.disable()
            HydraMod.LOGGER.info("Disable Twitch bot.")
        })
    }
}