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
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.sounds.SoundEvents
import java.awt.Color
import java.util.*
import kotlin.jvm.optionals.getOrNull

object EventSubListeners {
    val DEFAULT_CHAT_COLOR = Color(180, 84, 255)

    fun followingEventListener(event: ChannelFollowEvent) {
        val follower = Component.literal(event.userName).withStyle(ChatFormatting.AQUA)
        val after = Component.literal(" just Followed!").withStyle(ChatFormatting.WHITE)
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

    fun getSubEventTitle(user: String, text: String, endText: String): MutableComponent? {
        return Component.literal(user).withStyle(ChatFormatting.LIGHT_PURPLE)
            .append(Component.literal(text).withStyle(ChatFormatting.WHITE))
            .append(Component.literal(endText).withStyle(ChatFormatting.LIGHT_PURPLE))
    }

    fun getSubEventSmallTitle(user: String, text: String, endText: String): MutableComponent? {
        return Component.literal(user).withStyle(ChatFormatting.WHITE)
            .append(Component.literal(text).withStyle(ChatFormatting.LIGHT_PURPLE))
            .append(Component.literal(endText).withStyle(ChatFormatting.WHITE))
    }

    fun SubscriptionPlan.toReadable(): String = name.lowercase().replace(Regex("\\d"), "_$0").split("_")
        .joinToString(" ") { it.replaceFirstChar(Char::uppercaseChar) }


    fun channelPointEvent(title: String, userName: String) {
        val player = Minecraft.getInstance().player ?: return

        when (title) {
            "Hydrate!" -> titleMessage(
                Component.literal(title).withStyle(ChatFormatting.BLUE),
                Component.literal("Redeemed by $userName").withStyle(ChatFormatting.GRAY)
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
            "Creeper Aww Man!" -> player.playSound(SoundEvents.CREEPER_PRIMED)
            "Spawn Random Particle" -> randomParticle(player)
            "Point waste" -> titleMessage(Component.literal("${player.blockPosition()}"), null)
        }

        val user = Component.literal(userName).withStyle(ChatFormatting.DARK_GRAY)
        val text = Component.translatable("listener.${HydraMod.MODID}.reward.redeem").withStyle(ChatFormatting.WHITE)
        val eventTitle = darkGrayText(title)
        addChatMsg(user.append(text).append(eventTitle))

    }


    fun channelMessageListener(event: ChannelMessageEvent) {
        var messageSender = Component.literal(event.user.name).withColor(config.channelChatColor.toInt())
        val messageColor: ChatFormatting? = if (event.isHighlightedMessage) ChatFormatting.RED else null
        var formatPerms = false
        for (p in event.permissions) {
            if (p == CommandPermission.VIP || p == CommandPermission.MODERATOR) {
                formatPerms = true
                break
            }
        }
        if (event.user.name != config.credentials.username) {
            val textColor = event.messageEvent.userChatColor.getOrNull()?.let(Color::decode) ?: DEFAULT_CHAT_COLOR
            messageSender = messageSender.withColor(textColor.rgb)
        }
        addTwitchMessage(Date(), messageSender, event.message, messageColor, formatPerms)
    }
}