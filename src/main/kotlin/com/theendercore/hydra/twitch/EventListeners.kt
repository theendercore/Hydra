package com.theendercore.hydra.twitch

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent
import com.github.twitch4j.chat.events.channel.SubscriptionEvent
import com.github.twitch4j.common.enums.CommandPermission
import com.github.twitch4j.pubsub.events.FollowingEvent
import com.github.twitch4j.pubsub.events.RewardRedeemedEvent
import com.theendercore.hydra.HydraMod.Companion.LOGGER
import com.theendercore.hydra.HydraMod.Companion.MODID
import com.theendercore.hydra.config.ModConfig
import com.theendercore.hydra.util.Methods
import com.theendercore.hydra.util.reg.KeyBindingRegistry
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.Text
import net.minecraft.text.TextColor
import net.minecraft.util.Formatting
import java.util.*

object EventListeners {
    fun followingEventListener(event: FollowingEvent) {
        val follower = Text.literal(event.data.displayName).formatted(Formatting.AQUA)
        val after = Text.literal(" just Followed!").formatted(Formatting.WHITE)
        Methods.chatMessage(follower.append(after))
    }

    fun subscriptionEventListener(event: SubscriptionEvent) {
        LOGGER.info(event.toString())
        val subscriber = Text.literal(event.user.name).formatted(Formatting.LIGHT_PURPLE)
        val months = Text.literal(event.months.toString()).formatted(Formatting.LIGHT_PURPLE)
        Methods.titleMessage(
            subscriber.append(Text.literal(" Has Subscribed for ")).formatted(Formatting.WHITE).append(months)
                .append("Months!"), Text.literal(event.message.toString()).formatted(
                Formatting.GRAY
            )
        )
    }

    fun rewardRedeemedListener(event: RewardRedeemedEvent) {
        val eTitle = event.redemption.reward.title
        val player: PlayerEntity? = MinecraftClient.getInstance().player
        when (eTitle) {

            "Hydrate!" -> Methods.titleMessage(
                Text.literal(event.redemption.reward.title).formatted(Formatting.BLUE),
                Text.literal("Redeemed by " + event.redemption.user.displayName).formatted(
                    Formatting.GRAY
                )
            )

            "PP" -> LOGGER.info("yoo")

            "Point waste" -> {
                assert(player != null)
                Methods.titleMessage(Text.literal(player!!.pos.toString()), null)
            }
            "Random Shader"->{
                Methods.titleMessage( Text.of("hi"), null)

                Methods.setRandomShader()
                KeyBindingRegistry.timeRemainingInTicks = 5 *20
            }
        }

        val user = Text.literal(event.redemption.user.displayName).formatted(Formatting.DARK_GRAY)
        val translatableText = Text.translatable("listener.$MODID.reward.redeem").formatted(
            Formatting.WHITE
        )
        val eventTitle = Text.literal(eTitle).formatted(Formatting.DARK_GRAY)
        Methods.chatMessage(user.append(translatableText).append(eventTitle))

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
        val color = event.messageEvent.tags["color"]
        if (event.user.name != c.username) {
            messageSender =
                if (color != null) messageSender.setStyle(Text.literal("").style.withColor(TextColor.parse(color))) else messageSender.formatted(
                    Formatting.DARK_PURPLE
                )
        }
        Methods.addTwitchMessage(Date(), messageSender, event.message, messageColor, isVip)
    }
}