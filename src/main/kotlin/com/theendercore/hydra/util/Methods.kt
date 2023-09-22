package com.theendercore.hydra.util

import com.theendercore.hydra.config.ModConfig
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.particle.DefaultParticleType
import net.minecraft.particle.ParticleType
import net.minecraft.registry.Registries
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import java.text.SimpleDateFormat
import java.util.*

object Methods {
    private val client: MinecraftClient = MinecraftClient.getInstance()
    private val renderer = client.gameRenderer
//    private var random: Random = Random.create()
    fun addTwitchMessage(
        date: Date?,
        usernameText: MutableText?,
        msg: String,
        chatColor: Formatting?,
        isVIP: Boolean,
    ) {
        var message = msg
        val timestampText =
            Text.literal("[" + SimpleDateFormat(ModConfig.config!!.timeFormatting).format(date) + "]").formatted(
                Formatting.GRAY
            )
        val messageBodyText = Text.literal(": ").formatted(Formatting.WHITE)

        if (!isVIP) message = message.replace(Formatting.FORMATTING_CODE_PREFIX.toString().toRegex(), "$")

        if (chatColor == null) messageBodyText.append(Text.literal(message))
        else messageBodyText.append(Text.literal(message).formatted(chatColor, Formatting.BOLD, Formatting.ITALIC))

        chatMessage(timestampText.append(usernameText).append(messageBodyText))
    }

    fun chatMessage(text: Text?) {
        client.inGameHud.chatHud.addMessage(text)
    }

    fun titleMessage(text: Text?, smallText: Text?) {
        val hud = client.inGameHud
        if (text != null) {
            hud.setTitle(text)
        } else {
            hud.setSubtitle(smallText)
        }
    }

    fun setRandomShader() {
//        renderer.cycleSuperSecretSetting()
    }

    fun disableShader() {
//        client.gameRenderer.disablePostProcessor()
    }

    fun playSound(player: PlayerEntity, sound: SoundEvent) {
        player.playSound(sound, SoundCategory.PLAYERS, 1f, 1f)
    }

    fun playRandomSound(player: PlayerEntity) {
//        Registries.SOUND_EVENT[random.nextBetween(0, Registries.SOUND_EVENT.size() - 1)]?.let { playSound(player, it) }
    }

    fun playParticle(player: PlayerEntity, particle: ParticleType<*>): Int {
//        for (i in 0 until 100) {
//            try {
//                client.particleManager.addParticle(
//                    particle as ParticleEffect,
//                    player.x + random.nextGaussian() * 1f,
//                    player.y + 1.8 + random.nextGaussian() * 1f,
//                    player.z + random.nextGaussian() * 1f,
//                    random.nextGaussian() * 1f,
//                    random.nextGaussian() * 1f,
//                    random.nextGaussian() * 1f
//                )
//            } catch (throwable: Throwable) {
//                LOGGER.warn(
//                    "Could not spawn particle effect {}",
//                    if (particle is DefaultParticleType) particle.asString() else particle
//                )
//                return 0
//            }
//        }
        return 1
    }

    fun randomParticle(player: PlayerEntity): Int {
        val particles = Registries.PARTICLE_TYPE.filterIsInstance<DefaultParticleType>()
//        return playParticle(player, particles[random.nextBetween(0, particles.size - 1)])
        return 0
    }
}