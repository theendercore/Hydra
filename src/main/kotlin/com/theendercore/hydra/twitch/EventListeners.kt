package com.theendercore.hydra.twitch

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent
import com.github.twitch4j.chat.events.channel.SubscriptionEvent
import com.github.twitch4j.common.enums.CommandPermission
import com.github.twitch4j.eventsub.events.ChannelFollowEvent
import com.github.twitch4j.pubsub.events.RewardRedeemedEvent
import com.theendercore.hydra.HydraMod.LOGGER
import com.theendercore.hydra.HydraMod.MODID
import com.theendercore.hydra.config.ModConfig
import com.theendercore.hydra.util.Methods.addChatMsg
import com.theendercore.hydra.util.Methods.addTwitchMessage
import com.theendercore.hydra.util.Methods.playRandomSound
import com.theendercore.hydra.util.Methods.playSound
import com.theendercore.hydra.util.Methods.randomParticle
import com.theendercore.hydra.util.Methods.titleMessage
import com.theendercore.hydra.util.darkGrayText
import net.minecraft.client.MinecraftClient
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import java.awt.Color
import java.util.*
import kotlin.jvm.optionals.getOrNull

object EventListeners {
    val DEFAULT_CHAT_COLOR = Color(180, 84, 255)

    fun followingEventListener(event: ChannelFollowEvent) {
        val follower = Text.literal(event.userName).formatted(Formatting.AQUA)
        val after = Text.literal(" just Followed!").formatted(Formatting.WHITE)
        addChatMsg(follower.append(after))
    }

    fun subscriptionEventListener(event: SubscriptionEvent) {
        LOGGER.info(event.toString())
        val subscriber = Text.literal(event.user.name).formatted(Formatting.LIGHT_PURPLE)
        val months = Text.literal(event.months.toString()).formatted(Formatting.LIGHT_PURPLE)
        titleMessage(
            subscriber.append(Text.literal(" Has Subscribed for ")).formatted(Formatting.WHITE).append(months)
                .append("Months!"), Text.literal(event.message.toString()).formatted(
                Formatting.GRAY
            )
        )
    }

    fun rewardRedeemedListener(event: RewardRedeemedEvent) {
        val title = event.redemption.reward.title
        val player = MinecraftClient.getInstance().player!!

        val client = MinecraftClient.getInstance()
        when (title) {
            "Hydrate!" -> titleMessage(
                Text.literal(title).formatted(Formatting.BLUE),
                Text.literal("Redeemed by " + event.redemption.user.displayName).formatted(Formatting.GRAY)
            )

            "PP" -> LOGGER.info("yoo")
            "Random Shader" -> {
                if (!client.isOnThread) {
//                    client.execute {
//                        try {
//                            setRandomShader()
//                            TickRegistry.timeRemainingInTicks = 33 * 20
//                        } catch (var3: Exception) {
//                            LOGGER.debug("Shader didn't apply!")
//                        }
//                    }
//                    throw OffThreadException.INSTANCE
                }
            }

            "Play Random Sound" -> playRandomSound(player)
            "Creeper Aww Man!" -> playSound(player, SoundEvents.ENTITY_CREEPER_PRIMED)
            "Spawn Random Particle" -> randomParticle(player)
            "Point waste" -> titleMessage(Text.literal("${player.pos}"), null)
        }

        val user = Text.literal(event.redemption.user.displayName).formatted(Formatting.DARK_GRAY)
        val text = Text.translatable("listener.$MODID.reward.redeem").formatted(Formatting.WHITE)
        val eventTitle = darkGrayText(title)
        addChatMsg(user.append(text).append(eventTitle))

    }


    fun channelMessageListener(event: ChannelMessageEvent) {
        val c: ModConfig? = ModConfig.config
        var messageSender = Text.literal(event.user.name).formatted(c!!.channelChatColor?.format)
        var messageColor: Formatting? = null
        if (event.isHighlightedMessage) {
            messageColor = Formatting.RED
        }
        var isVip = false
        val perms = event.permissions
        for (x in perms) {
            if (x == CommandPermission.VIP || x == CommandPermission.MODERATOR) {
                isVip = true
                break
            }
        }
        val color = event.messageEvent.userChatColor // .tags["color"]
        if (event.user.name != c.username) {
            val textColor = color.getOrNull()?.let { Color.decode(it) } ?: DEFAULT_CHAT_COLOR
            messageSender = messageSender.setStyle(Text.literal("").style.withColor(textColor.rgb))
        }
        addTwitchMessage(Date(), messageSender, event.message, messageColor, isVip)
    }
}