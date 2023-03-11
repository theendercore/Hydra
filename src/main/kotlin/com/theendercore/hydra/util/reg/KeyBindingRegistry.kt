package com.theendercore.hydra.util.reg

import com.theendercore.hydra.HydraMod.Companion.LOGGER
import com.theendercore.hydra.HydraMod.Companion.MODID
import com.theendercore.hydra.HydraMod.Companion.id
import com.theendercore.hydra.util.Methods
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket
import net.minecraft.particle.ParticleTypes
import net.minecraft.util.math.random.Random
import org.lwjgl.glfw.GLFW
import java.text.SimpleDateFormat
import java.util.*

object KeyBindingRegistry {
    private var random: Random = Random.create()
    private var helperKey1: KeyBinding? = null
    private var helperKey2: KeyBinding? = null
    var timeRemainingInTicks = 0


    fun init() {

        HudRenderCallback.EVENT.register(id("effect_timer"), HudRenderCallback { matrixStack, _ ->
            if (timeRemainingInTicks > 0) {
                MinecraftClient.getInstance().textRenderer.drawWithShadow(
                    matrixStack,
                    "Time Left: ${
                        SimpleDateFormat("mm:ss").format(Date((timeRemainingInTicks * 50).toLong()))
                    }",
                    100f,
                    100f,
                    0xFFFFF
                )
            }
        })
        helperKey1 = KeyBindingHelper.registerKeyBinding(
            KeyBinding(
                "key.$MODID.m4",
                InputUtil.Type.MOUSE,
                GLFW.GLFW_MOUSE_BUTTON_4,
                "keybinding.category.$MODID"
            )
        )
        helperKey2 = KeyBindingHelper.registerKeyBinding(
            KeyBinding(
                "key.$MODID.m5",
                InputUtil.Type.MOUSE,
                GLFW.GLFW_MOUSE_BUTTON_5,
                "keybinding.category.$MODID"
            )
        )
        ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick register@{
            while (helperKey1!!.wasPressed()) {
                val mcClient = it
                val playerEntity: PlayerEntity = mcClient.player!!
                val packet = ParticleS2CPacket(
                    ParticleTypes.TOTEM_OF_UNDYING,
                    true,
                    playerEntity.x,
                    0.0,
                    playerEntity.z,
                    0f,
                    0f,
                    0f,
                    1f,
                    100
                )
                for (i in 0 until packet.count) {
                    val g = random.nextGaussian() * packet.offsetX.toDouble()
                    val h = random.nextGaussian() * 0.0
                    val j = random.nextGaussian() * packet.offsetZ.toDouble()
                    val k = random.nextGaussian() * packet.speed.toDouble()
                    val l = random.nextGaussian() * packet.speed.toDouble()
                    val m = random.nextGaussian() * packet.speed.toDouble()
                    try {
                        mcClient.particleManager.addParticle(
                            ParticleTypes.TOTEM_OF_UNDYING,
                            packet.x + g,
                            playerEntity.y + 2 + h,
                            packet.z + j,
                            k,
                            l,
                            m
                        )
                    } catch (var16: Throwable) {
                        LOGGER.warn("Could not spawn particle effect {}", packet.parameters)
                        return@register
                    }
                }
            }


//            while (helperKey2!!.wasPressed()) {
//
//            }

            if (timeRemainingInTicks > 0
            ) {
                timeRemainingInTicks--
            } else {
                Methods.disableShader()
            }
        })
    }
}