package com.theendercore.hydra.client.init

import com.theendercore.hydra.client.HydraMod
import com.theendercore.hydra.client.HydraMod.config
import com.theendercore.hydra.client.HydraMod.isDev
import com.theendercore.hydra.client.commands.HydraCommand
import com.theendercore.hydra.client.twitch.TwitchBot
import com.theendercore.hydra.client.util.addChatMsg
import com.theendercore.hydra.client.util.darkGrayText
import com.theendercore.hydra.client.util.disableShader
import com.theendercore.hydra.client.util.runDebugCode
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents.ClientStopping
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents

object HydraFabricEvents {
    var timeRemainingInTicks = 0

    fun init() {
        ClientTickEvents.END_CLIENT_TICK.register {
            if (timeRemainingInTicks > 0) timeRemainingInTicks-- else disableShader()

            if (isDev() && HydraKeys.testKey.isPressed) runDebugCode()
        }

        ClientPlayConnectionEvents.JOIN.register { _, _, _ ->
            if (config.autoStart && HydraMod.twitchClient == null) {
                addChatMsg(darkGrayText("command.${HydraMod.MODID}.auto_load"))
                Thread { TwitchBot.enable() }.start()
            }
        }

        ClientLifecycleEvents.CLIENT_STOPPING.register(ClientStopping {
            TwitchBot.disable()
            HydraMod.LOGGER.info("Disable Twitch bot.")
        })

        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ -> HydraCommand.register(dispatcher) }
    }
}

