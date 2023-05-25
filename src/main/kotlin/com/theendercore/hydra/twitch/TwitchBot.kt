package com.theendercore.hydra.twitch

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential
import com.github.twitch4j.TwitchClientBuilder
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent
import com.github.twitch4j.chat.events.channel.SubscriptionEvent
import com.github.twitch4j.pubsub.events.FollowingEvent
import com.github.twitch4j.pubsub.events.RewardRedeemedEvent
import com.theendercore.hydra.HydraMod.Companion.LOGGER
import com.theendercore.hydra.HydraMod.Companion.MODID
import com.theendercore.hydra.HydraMod.Companion.credential
import com.theendercore.hydra.HydraMod.Companion.twitchClient
import com.theendercore.hydra.config.ModConfig
import com.theendercore.hydra.util.Cashing.saveEmote
import com.theendercore.hydra.util.Methods.chatMessage
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object TwitchBot {
    fun enable(): Int {
        val config: ModConfig? = ModConfig.config
        credential = OAuth2Credential("twitch", config!!.oauthKey)
        if (config.username == "" || config.oauthKey == "") {
            chatMessage(
                Text.translatable("command.$MODID.error.config").formatted(Formatting.RED)
            )
            return 0
        }
        if (twitchClient == null) {
            chatMessage(
                Text.translatable("command.$MODID.connecting", config.username).formatted(
                    Formatting.DARK_GRAY
                )
            )
            twitchClient = TwitchClientBuilder.builder()
                .withEnableHelix(true)
                .withEnablePubSub(true)
                .withEnableChat(true)
                .withChatAccount(credential)
                .build()
            chatMessage(
                Text.translatable("command.$MODID.connected").formatted(Formatting.DARK_GRAY)
            )
        } else {
            chatMessage(
                Text.translatable("command.$MODID.connected.already", config.username).formatted(
                    Formatting.GRAY
                )
            )
            return 0
        }
        if (credential!!.userName == null) {
            chatMessage(
                Text.translatable("command.$MODID.error.token.incorrect").formatted(
                    Formatting.RED
                )
            )
            disable()
            return 0
        }
        if (config.extras) {

            val resultList = twitchClient!!.helix.getUsers(
                credential!!.accessToken,
                null,
                listOf(config.username)
            ).execute()
            val channelID = resultList.users[0].id
            LOGGER.info("\n\n$channelID\n\n")
            twitchClient!!.pubSub.listenForChannelPointsRedemptionEvents(credential, channelID)
            twitchClient!!.pubSub.listenForFollowingEvents(credential, channelID)
            twitchClient!!.pubSub.listenForSubscriptionEvents(credential, channelID)
            twitchClient!!.eventManager.onEvent(RewardRedeemedEvent::class.java) { event: RewardRedeemedEvent ->
                EventListeners.rewardRedeemedListener(
                    event
                )
            }
            twitchClient!!.eventManager.onEvent(FollowingEvent::class.java) { event: FollowingEvent ->
                EventListeners.followingEventListener(
                    event
                )
            }
            twitchClient!!.eventManager.onEvent(
                SubscriptionEvent::class.java
            ) { event: SubscriptionEvent -> EventListeners.subscriptionEventListener(event) }

            if (config.enableCache) {
                val emoteList = twitchClient!!.helix.getChannelEmotes(credential!!.accessToken, channelID).execute()
                emoteList.emotes.forEach {
                    LOGGER.info(it.name)
                    saveEmote(it.images.largeImageUrl, it.name)
                }
            }

            chatMessage(Text.translatable("command.$MODID.extras.enable"))
        }
        twitchClient!!.eventManager.onEvent(ChannelMessageEvent::class.java) { event: ChannelMessageEvent ->
            EventListeners.channelMessageListener(
                event
            )
        }
        return 1
    }

    fun disable(): Int {
        if (twitchClient == null) {
            chatMessage(
                Text.translatable("command.$MODID.not_connected").formatted(
                    Formatting.DARK_GRAY
                )
            )
            return 0
        }
        twitchClient!!.pubSub.close()
        twitchClient!!.chat.close()
        twitchClient!!.close()
        twitchClient = null
        chatMessage(Text.translatable("command.$MODID.disconnected").formatted(Formatting.GRAY))
        return 1
    }
}