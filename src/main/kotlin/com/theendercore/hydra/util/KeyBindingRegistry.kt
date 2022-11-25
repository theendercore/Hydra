package com.theendercore.hydra.util

import com.theendercore.hydra.LOGGER
import com.theendercore.hydra.MODID
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket
import net.minecraft.particle.ParticleTypes
import net.minecraft.util.math.random.Random
import org.lwjgl.glfw.GLFW

object KeyBindingRegistry {
    private var random: Random = Random.create()
    private var helperKey1: KeyBinding? = null
    private var helperKey2: KeyBinding? = null
    fun init() {
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
                val mcClient = MinecraftClient.getInstance()
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
                        Methods.setRandomShader()
                    } catch (var16: Throwable) {
                        LOGGER.warn("Could not spawn particle effect {}", packet.parameters)
                        return@register
                    }
                }
                //                mcClient.particleManager.addParticle(ParticleTypes.BUBBLE, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), 0.0, 0.0, 0.0);

    //                Random rand = new Random();
    //                PlayerEntity player = client.player;
    //                double z, x;
    //                for (int i = 0; i <= 10; i++) {
    //                    x = rand.nextDouble();
    //                    z = rand.nextDouble();
    //                    LOGGER.info("x:" + x + "\ny:" + z);
    //                    assert player != null;
    //                    mcClient.particleManager.addParticle(new SculkChargeParticleEffect(10), player.getX() - .5 + x, player.getEyeY() - .1, player.getZ() - .5 + z, 0, 0, 0);
    //                    mcClient.particleManager.addParticle(new DustParticleEffect(new Vec3f(1, 1,1 ), 4), player.getX(), player.getEyeY(), player.getZ(), 0, 0, 0);
    //                }
            }
            while (helperKey2!!.isPressed) {
                Methods.disableShader()
                LOGGER.info("hi")
            }
        })
    }
}