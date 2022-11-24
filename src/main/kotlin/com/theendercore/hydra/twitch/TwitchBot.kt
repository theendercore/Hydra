package com.theendercore.hydra.twitch

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential
import com.github.twitch4j.TwitchClientBuilder
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent
import com.github.twitch4j.chat.events.channel.SubscriptionEvent
import com.github.twitch4j.pubsub.events.FollowingEvent
import com.github.twitch4j.pubsub.events.RewardRedeemedEvent
import com.theendercore.hydra.HydraMod
import com.theendercore.hydra.config.ModConfig
import com.theendercore.hydra.util.Methods
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object TwitchBot {
    fun enable(): Int {
        val config: ModConfig? = ModConfig.config
        HydraMod.credential = OAuth2Credential("twitch", config!!.oauthKey)
        if (config.username == "" || config.oauthKey == "") {
            Methods.chatMessage(
                Text.translatable("command." + HydraMod.MODID + ".error.config").formatted(Formatting.RED)
            )
            return 0
        }
        if (HydraMod.twitchClient == null) {
            Methods.chatMessage(
                Text.translatable("command." + HydraMod.MODID + ".connecting", config.username).formatted(
                    Formatting.DARK_GRAY
                )
            )
            HydraMod.twitchClient = TwitchClientBuilder.builder()
                .withEnableHelix(true)
                .withEnablePubSub(true)
                .withEnableChat(true)
                .withChatAccount(HydraMod.credential)
                .build()
            Methods.chatMessage(
                Text.translatable("command." + HydraMod.MODID + ".connected").formatted(Formatting.DARK_GRAY)
            )
        } else {
            Methods.chatMessage(
                Text.translatable("command." + HydraMod.MODID + ".connected.already", config.username).formatted(
                    Formatting.GRAY
                )
            )
            return 0
        }
        if (HydraMod.credential.userName == null) {
            Methods.chatMessage(
                Text.translatable("command." + HydraMod.MODID + ".error.token.incorrect").formatted(
                    Formatting.RED
                )
            )
            disable()
            return 0
        }
        if (config.extras) {
            val resultList = HydraMod.twitchClient.helix.getUsers(
                HydraMod.credential.accessToken,
                null,
                listOf(config.username)
            ).execute()
            val channelID = resultList.users[0].id
            HydraMod.twitchClient.pubSub.listenForChannelPointsRedemptionEvents(HydraMod.credential, channelID)
            HydraMod.twitchClient.pubSub.listenForFollowingEvents(HydraMod.credential, channelID)
            HydraMod.twitchClient.pubSub.listenForSubscriptionEvents(HydraMod.credential, channelID)
            HydraMod.twitchClient.eventManager.onEvent(RewardRedeemedEvent::class.java) { event: RewardRedeemedEvent ->
                EventListeners.rewardRedeemedListener(
                    event
                )
            }
            HydraMod.twitchClient.eventManager.onEvent(FollowingEvent::class.java) { event: FollowingEvent ->
                EventListeners.followingEventListener(
                    event
                )
            }
            HydraMod.twitchClient.eventManager.onEvent(
                SubscriptionEvent::class.java
            ) { event: SubscriptionEvent -> EventListeners.subscriptionEventListener(event) }
            Methods.chatMessage(Text.translatable("command." + HydraMod.MODID + ".extras.enable"))
        }
        HydraMod.twitchClient.eventManager.onEvent(ChannelMessageEvent::class.java) { event: ChannelMessageEvent ->
            EventListeners.channelMessageListener(
                event
            )
        }
        return 1
    }

    fun disable(): Int {
        if (HydraMod.twitchClient == null) {
            Methods.chatMessage(
                Text.translatable("command." + HydraMod.MODID + ".disconnected.already").formatted(
                    Formatting.DARK_GRAY
                )
            )
            return 0
        }
        HydraMod.twitchClient.pubSub.close()
        HydraMod.twitchClient.chat.close()
        HydraMod.twitchClient.close()
        HydraMod.twitchClient = null
        Methods.chatMessage(Text.translatable("command." + HydraMod.MODID + ".disconnected").formatted(Formatting.GRAY))
        return 1
    }
}