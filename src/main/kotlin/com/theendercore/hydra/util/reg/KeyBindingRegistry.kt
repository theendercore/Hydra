package com.theendercore.hydra.util.reg

import com.theendercore.hydra.HydraMod.Companion.MODID
import com.theendercore.hydra.util.Methods.playRandomSound
import com.theendercore.hydra.util.Methods.randomParticle
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import net.minecraft.entity.player.PlayerEntity
import org.lwjgl.glfw.GLFW

object KeyBindingRegistry {

//    private var random: Random = Random.create()

    private var helperKey1: KeyBinding? = null


    fun init() {

        helperKey1 = KeyBindingHelper.registerKeyBinding(
            KeyBinding(
                "key.$MODID.m4",
                InputUtil.Type.MOUSE,
                GLFW.GLFW_MOUSE_BUTTON_4,
                "keybinding.category.$MODID"
            )
        )

        ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick register@{
            while (helperKey1!!.wasPressed()) {
                val player: PlayerEntity = it.player!!
                randomParticle(player)
                playRandomSound(player)
            }

        })
    }
}