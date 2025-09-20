package com.theendercore.hydra.client.util

import com.theendercore.hydra.client.HydraMod.LOGGER
import com.theendercore.hydra.client.HydraMod.config
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.particle.DefaultParticleType
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleType
import net.minecraft.registry.Holder
import net.minecraft.registry.Registries
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import java.text.SimpleDateFormat
import java.util.*

private val client: MinecraftClient = MinecraftClient.getInstance()

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
        Text.literal(SimpleDateFormat(config.timeFormatting).format(date)).formatted(Formatting.GRAY)
    val messageBodyText = Text.literal(": ").formatted(Formatting.WHITE)

    if (!isVIP) message = message.replace(Formatting.FORMATTING_CODE_PREFIX.toString().toRegex(), "$")

    if (chatColor == null) messageBodyText.append(Text.literal(message))
    else messageBodyText.append(Text.literal(message).formatted(chatColor, Formatting.BOLD, Formatting.ITALIC))

    addChatMsg(timestampText.append(usernameText).append(messageBodyText))
}

fun addChatMsg(text: Text) = client.inGameHud.chatHud.addMessage(text)
fun addChatMsg(text: String) = addChatMsg(Text.of(text))

fun titleMessage(text: Text?, smallText: Text?) {
    val hud = client.inGameHud
    text?.let(hud::setTitle)
    smallText?.let(hud::setSubtitle)
}

@Suppress("unused")
fun setRandomShader() {
//        renderer.cycleSuperSecretSetting()
}

fun disableShader() {
//        client.gameRenderer.disablePostProcessor()
}


fun playRandomSound(player: PlayerEntity) =
    Registries.SOUND_EVENT.getRandom(player.random)?.let { if (it.isPresent) player.playSound(it.get()) }

fun PlayerEntity.playSound(sound: Holder.Reference<SoundEvent>) =
    playSound(Registries.SOUND_EVENT.get(sound.key.get()), SoundCategory.PLAYERS, 1f, 1f)

fun playParticle(player: PlayerEntity, particle: ParticleType<*>): Int {
    val random = player.random
    try {
        repeat(100) {
            if (particle is ParticleEffect) {
                client.particleManager.addParticle(
                    particle,
                    player.x + random.nextGaussian() * 1f,
                    player.y + 1.8 + random.nextGaussian() * 1f,
                    player.z + random.nextGaussian() * 1f,
                    random.nextGaussian() * 1f,
                    random.nextGaussian() * 1f,
                    random.nextGaussian() * 1f
                )
            }
        }
    } catch (e: Throwable) {
        LOGGER.error("Could not spawn particle effect $particle", e)
        return 0
    }
    return 1
}

fun randomParticle(player: PlayerEntity): Int {
    val particles = Registries.PARTICLE_TYPE.filterIsInstance<DefaultParticleType>()
    if (particles.isEmpty()) return 0
    return playParticle(player, particles.random())
}
