package com.theendercore.hydra.client.twitch

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent
import com.github.twitch4j.common.enums.CommandPermission
import com.github.twitch4j.common.enums.SubscriptionPlan
import com.github.twitch4j.eventsub.events.ChannelFollowEvent
import com.github.twitch4j.eventsub.events.ChannelSubscribeEvent
import com.github.twitch4j.eventsub.events.ChannelSubscriptionGiftEvent
import com.github.twitch4j.eventsub.events.ChannelSubscriptionMessageEvent
import com.theendercore.hydra.client.HydraMod
import com.theendercore.hydra.client.HydraMod.config
import com.theendercore.hydra.client.util.*
import net.minecraft.client.MinecraftClient
import net.minecraft.sound.SoundEvents
import net.minecraft.text.MutableText
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

    fun subEvent(event: ChannelSubscribeEvent) {
        titleMessage(
            getSubEventTitle(event.userName, " Has subscribed!", ""),
            getSubEventSmallTitle("With ", event.tier.toReadable(), "")
        )
    }

    fun subEvent(event: ChannelSubscriptionGiftEvent) {
        titleMessage(
            getSubEventTitle(if (event.isAnonymous == true) "[Anonymous]" else event.userName, " Has has gifted", ""),
            getSubEventSmallTitle(
                event.total.toString(), " " + event.tier.toReadable(), " Sub${if (event.total > 1) "s" else ""}",
            )
        )
    }

    fun subEvent(event: ChannelSubscriptionMessageEvent) {
        val monthsText = if (event.cumulativeMonths > 1) " ${event.cumulativeMonths} Months" else " a Month"
        @Suppress("UNNECESSARY_SAFE_CALL")
        titleMessage(
            getSubEventTitle(event.userName, " Has subscribed for", monthsText),
            if (event?.message?.text?.isEmpty() == true) getSubEventSmallTitle("With ", event.tier.toReadable(), "")
            else getSubEventSmallTitle("", event.message.text, "")
        )
    }

    fun getSubEventTitle(user: String, text: String, endText: String): MutableText? {
        return Text.literal(user).formatted(Formatting.LIGHT_PURPLE)
            .append(Text.literal(text).formatted(Formatting.WHITE))
            .append(Text.literal(endText).formatted(Formatting.LIGHT_PURPLE))
    }

    fun getSubEventSmallTitle(user: String, text: String, endText: String): MutableText? {
        return Text.literal(user).formatted(Formatting.WHITE)
            .append(Text.literal(text).formatted(Formatting.LIGHT_PURPLE))
            .append(Text.literal(endText).formatted(Formatting.WHITE))
    }

    fun SubscriptionPlan.toReadable(): String = name.lowercase().replace(Regex("\\d"), "_$0").split("_")
        .joinToString(" ") { it.replaceFirstChar(Char::uppercaseChar) }


    fun channelPointRedemption(title: String, userName: String) {
        val player = MinecraftClient.getInstance().player ?: return

        when (title) {
            "Hydrate!" -> titleMessage(
                Text.literal(title).formatted(Formatting.BLUE),
                Text.literal("Redeemed by $userName").formatted(Formatting.GRAY)
            )

            "PP" -> HydraMod.LOGGER.info("yoo")/* "Random Shader" -> if (!client.isOnThread) {
                     client.execute {
                         try {
                             setRandomShader()
                             TickRegistry.timeRemainingInTicks = 33 * 20
                         } catch (var3: Exception) {
                             LOGGER.debug("Shader didn't apply!")
                         }
                     }
                     throw OffThreadException.INSTANCE
                 }*/
            "Play Random Sound" -> playRandomSound(player)
            "Creeper Aww Man!" -> player.playSound(SoundEvents.ENTITY_CREEPER_PRIMED)
            "Spawn Random Particle" -> randomParticle(player)
            "Point waste" -> titleMessage(Text.literal("${player.blockPos}"), null)
        }

        val user = Text.literal(userName).formatted(Formatting.DARK_GRAY)
        val text = Text.translatable("listener.${HydraMod.MODID}.reward.redeem").formatted(Formatting.WHITE)
        val eventTitle = darkGrayText(title)
        addChatMsg(user.append(text).append(eventTitle))

    }


    fun channelMessageListener(event: ChannelMessageEvent) {
        var messageSender = Text.literal(event.user.name).setColor(config.channelChatColor.toInt())
        val messageColor: Formatting? = if (event.isHighlightedMessage) Formatting.RED else null
        var formatPerms = false
        for (p in event.permissions) {
            if (p == CommandPermission.VIP || p == CommandPermission.MODERATOR) {
                formatPerms = true
                break
            }
        }
        if (event.user.name != config.credentials.username) {
            val textColor = event.messageEvent.userChatColor.getOrNull()?.let(Color::decode) ?: DEFAULT_CHAT_COLOR
            messageSender = messageSender.setColor(textColor.rgb)
        }
        addTwitchMessage(Date(), messageSender, event.message, messageColor, formatPerms)
    }
}