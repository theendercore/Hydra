package com.theendercore.hydra.util.reg

import com.theendercore.hydra.HydraMod.Companion.LOGGER
import com.theendercore.hydra.HydraMod.Companion.MODID
import com.theendercore.hydra.HydraMod.Companion.clipCount
import com.theendercore.hydra.HydraMod.Companion.credential
import com.theendercore.hydra.HydraMod.Companion.logPath
import com.theendercore.hydra.HydraMod.Companion.twitchClient
import com.theendercore.hydra.config.ModConfig.Companion.config
import com.theendercore.hydra.util.Methods.chatMessage
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import org.lwjgl.glfw.GLFW
import java.io.File

object KeyBindingRegistry {

//    private var random: Random = Random.create()

    private var helperKey1: KeyBinding? = null
    private var markerKey: KeyBinding? = null
    private var clipKey: KeyBinding? = null


    fun init() {

        helperKey1 = KeyBindingHelper.registerKeyBinding(
            KeyBinding(
                "key.$MODID.m4",
                InputUtil.Type.MOUSE,
                GLFW.GLFW_MOUSE_BUTTON_4,
                "keybinding.category.$MODID"
            )
        )

        markerKey = KeyBindingHelper.registerKeyBinding(
            KeyBinding(
                "key.$MODID.markerKey",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_DOWN,
                "keybinding.category.$MODID"
            )
        )
        clipKey = KeyBindingHelper.registerKeyBinding(
            KeyBinding(
                "key.$MODID.clipKey",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT,
                "keybinding.category.$MODID"
            )
        )



        ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick register@{

            while (helperKey1!!.wasPressed()) {
//                val player: PlayerEntity = it.player!!
            }

            while (clipKey!!.wasPressed()) {
                if (twitchClient != null && config != null) {
                    Thread {
                        val clipData =
                            twitchClient!!.helix.createClip(credential!!.accessToken, "710193183", false).execute()

                        clipData.data.forEach { clip ->
                            LOGGER.info("Created Clip with ID: ${clip.id}")
                            log("Clip Link: https://www.twitch.tv/${config!!.username}/clip/${clip.id}")
                        }

                        chatMessage(Text.literal("Clip created ").formatted(Formatting.AQUA))
                    }.start()
                } else chatMessage(Text.translatable("command.$MODID.not_connected").formatted(Formatting.DARK_GRAY))

            }

            while (markerKey!!.wasPressed()) {
                if (config != null && twitchClient != null) {
                    Thread {
                        val success = twitchClient!!.chat.sendMessage(config!!.username, "/marker $clipCount")
                        LOGGER.info(success.toString())
                        if (success) {
                            chatMessage(Text.literal("Marker $clipCount").formatted(Formatting.GRAY))
                            clipCount++
                        }
                    }.start()
                } else chatMessage(Text.translatable("command.$MODID.not_connected").formatted(Formatting.DARK_GRAY))
            }

        })
    }

    fun log(line: String) {
        val f = File("$logPath")
        f.appendText(line + System.getProperty("line.separator"))
    }
}