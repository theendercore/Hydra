package com.theendercore.hydra.client.twitch

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential
import com.github.twitch4j.TwitchClient
import com.github.twitch4j.TwitchClientBuilder
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent
import com.github.twitch4j.chat.events.channel.SubscriptionEvent
import com.github.twitch4j.eventsub.events.ChannelFollowEvent
import com.github.twitch4j.eventsub.socket.IEventSubSocket
import com.github.twitch4j.eventsub.subscriptions.SubscriptionTypes
import com.github.twitch4j.pubsub.events.RewardRedeemedEvent
import com.theendercore.hydra.client.HydraMod
import com.theendercore.hydra.client.HydraMod.LOGGER
import com.theendercore.hydra.client.HydraMod.config
import com.theendercore.hydra.client.util.addChatMsg
import com.theendercore.hydra.client.util.darkGrayText
import com.theendercore.hydra.client.util.grayText
import com.theendercore.hydra.client.util.redText

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
                    .withEnablePubSub(true)
                    .withEnableChat(true)
                    .withChatAccount(credential)
                    // New Api (yes its a pain)
                    .withEnableEventSocket(true)
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

                HydraMod.twitchClient!!.pubSub.listenForChannelPointsRedemptionEvents(credential, config.credentials.broadcasterId)
                HydraMod.twitchClient!!.eventManager.onEvent(
                    RewardRedeemedEvent::class.java, EventListeners::rewardRedeemedListener
                )

                HydraMod.twitchClient!!.pubSub.listenForSubscriptionEvents(credential, config.credentials.broadcasterId)
                HydraMod.twitchClient!!.eventManager.onEvent(
                    SubscriptionEvent::class.java, EventListeners::subscriptionEventListener
                )

                eventSocket = HydraMod.twitchClient!!.getEventSocket()

                // create subscription
                eventSocket?.register(
                    SubscriptionTypes.CHANNEL_FOLLOW_V2
                        .prepareSubscription({ it.broadcasterUserId(config.credentials.broadcasterId).build() }, null)
                )

                // register event handler
                eventSocket?.eventManager?.onEvent(
                    ChannelFollowEvent::class.java, EventListeners::followingEventListener
                )

//                twitchClient!!.pubSub.listenForFollowingEvents(credential, config.broadcasterId)
//                twitchClient!!.eventManager.onEvent(FollowingEvent::class.java, EventListeners::followingEventListener)


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
//            client.pubSub.close()
//            client.chat.close()
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