package com.theendercore.hydra.client.util


import com.theendercore.hydra.client.HydraMod.LOGGER
import com.theendercore.hydra.client.HydraMod.config
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.core.Holder
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.player.Player
import java.text.SimpleDateFormat
import java.util.*

private val client: Minecraft = Minecraft.getInstance()

//    private var random: Random = Random.create()
fun addTwitchMessage(
    date: Date,
    usernameText: MutableComponent,
    msg: String,
    chatColor: ChatFormatting?,
    isVIP: Boolean,
) {
    var message = msg
    val timestampText =
        if (config.timeFormatting.isEmpty()) Component.literal("")
        else Component.literal(SimpleDateFormat(config.timeFormatting).format(date)).withStyle(ChatFormatting.GRAY)
    val messageBodyText = Component.literal(": ").withStyle(ChatFormatting.WHITE)

    if (!isVIP) message = message.replace(ChatFormatting.PREFIX_CODE.toString().toRegex(), "$")

    if (chatColor == null) messageBodyText.append(Component.literal(message))
    else messageBodyText.append(
        Component.literal(message).withStyle(chatColor, ChatFormatting.BOLD, ChatFormatting.ITALIC)
    )

    addChatMsg(timestampText.append(usernameText).append(messageBodyText))
}

fun addChatMsg(text: Component) {
    client.execute {
        try {
            client.gui.chat.addMessage(text)
        } catch (e: Throwable) {
            LOGGER.error("Message could not Send!", e)
        }
    }
}

fun addChatMsg(text: String) = addChatMsg(Component.literal(text))

fun titleMessage(text: Component?, smallText: Component?) {
    val hud = client.gui
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


fun playRandomSound(player: Player) =
    BuiltInRegistries.SOUND_EVENT.getRandom(player.random).let { if (it.isPresent) player.playSound(it.get()) }

fun Player.playSound(sound: Holder.Reference<SoundEvent>) =
    level().playLocalSound(x, y, z, sound.value(), SoundSource.PLAYERS, 1f, 1f, false)

fun playParticle(player: Player, particle: ParticleType<*>): Int {
    val random = player.random
    try {
        repeat(100) {
            if (particle is ParticleOptions) {
                client.particleEngine.createParticle(
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

fun randomParticle(player: Player): Int {
    val particles = BuiltInRegistries.PARTICLE_TYPE.filterIsInstance<SimpleParticleType>()
    if (particles.isEmpty()) return 0
    return playParticle(player, particles.random())
}
