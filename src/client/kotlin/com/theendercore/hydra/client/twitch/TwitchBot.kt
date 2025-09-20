package com.theendercore.hydra.client.twitch

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential
import com.github.twitch4j.TwitchClient
import com.github.twitch4j.TwitchClientBuilder
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent
import com.github.twitch4j.eventsub.socket.IEventSubSocket
import com.github.twitch4j.eventsub.subscriptions.SubscriptionTypes
import com.theendercore.hydra.client.HydraMod
import com.theendercore.hydra.client.HydraMod.LOGGER
import com.theendercore.hydra.client.HydraMod.config
import com.theendercore.hydra.client.twitch.EventListeners.channelPointRedemption
import com.theendercore.hydra.client.twitch.EventListeners.subEvent
import com.theendercore.hydra.client.util.*

object TwitchBot {

    @JvmField
    var ENABLED = false

    @JvmField
    var client: TwitchClient = TwitchClientBuilder.builder().build()

    @JvmField
    var credential: OAuth2Credential? = null

    @JvmField
    var eventSocket: IEventSubSocket? = null

    fun enable(): Int {
        return if (ENABLED) 0
        else {
            ENABLED = true
            credential = OAuth2Credential("twitch", config.credentials.oauthKey)

            val usernameInvalid = config.credentials.username.trim() == ""
            val oauthKeyInvalid = config.credentials.username.trim() == ""
            if (usernameInvalid || oauthKeyInvalid) {
                if (usernameInvalid)
                    addChatMsg(redText("command.${HydraMod.MODID}.error.config.username"))
                if (oauthKeyInvalid)
                    addChatMsg(redText("command.${HydraMod.MODID}.error.config.oauthKey"))

                return 0
            }
            if (HydraMod.twitchClient == null) {
                addChatMsg(darkGrayText("command.${HydraMod.MODID}.connecting", config.credentials.username))
                HydraMod.twitchClient = TwitchClientBuilder.builder()
                    .withEnableHelix(true)
                    .withEnableChat(true)
                    // New Api (yes its a pain)
                    .withEnableEventSocket(true)
                    .withChatAccount(credential)
                    .withDefaultAuthToken(credential)
                    .build()

                addChatMsg(darkGrayText("command.${HydraMod.MODID}.connected"))
            } else {
                addChatMsg(darkGrayText("command.${HydraMod.MODID}.connected.already", config.credentials.username))
                return 0
            }
            if (credential!!.userName == null) {
                addChatMsg(redText("command.${HydraMod.MODID}.error.token.incorrect"))
                disable()
                return 0
            }
            if (config.extras) {
                val fetchedUsers = HydraMod.twitchClient!!.helix
                    .getUsers(credential!!.accessToken, null, listOf(config.credentials.username)).execute()

                if (fetchedUsers.users.isEmpty()) {
                    error("Failed to get users! ${fetchedUsers.pagination}")
                }
                try {
                    config.credentials.broadcasterId = fetchedUsers.users[0].id
                    config.save()
                } catch (e: Exception) {
                    LOGGER.error("Filed to set broadcasterId", e)
                }

                eventSocket = HydraMod.twitchClient!!.getEventSocket()
                register(SubscriptionTypes.CHANNEL_FOLLOW_V2, EventListeners::followingEventListener)
                register(SubscriptionTypes.CHANNEL_POINTS_CUSTOM_REWARD_REDEMPTION_ADD) {
                    channelPointRedemption(it.reward.title, it.userName)
                }

                register(SubscriptionTypes.CHANNEL_SUBSCRIBE) { if (!it.isGift) subEvent(it) }
                register(SubscriptionTypes.CHANNEL_SUBSCRIPTION_GIFT) { subEvent(it) }
                register(SubscriptionTypes.CHANNEL_SUBSCRIPTION_MESSAGE) { subEvent(it) }

                addChatMsg(grayText("command.${HydraMod.MODID}.extras.enable"))
            }

            HydraMod.twitchClient!!.eventManager.onEvent(
                ChannelMessageEvent::class.java,
                EventListeners::channelMessageListener
            )

            1
        }

    }

    fun disable(): Int {
        return if (ENABLED) {
            HydraMod.twitchClient?.close()
            HydraMod.twitchClient = null
            addChatMsg(grayText("command.${HydraMod.MODID}.disconnected"))
            ENABLED = false
            1
        } else {
            addChatMsg(darkGrayText("command.${HydraMod.MODID}.not_connected"))
            0
        }
    }
}