package com.theendercore.hydra.twitch

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential
import com.github.twitch4j.TwitchClient
import com.github.twitch4j.TwitchClientBuilder
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent
import com.github.twitch4j.chat.events.channel.SubscriptionEvent
import com.github.twitch4j.eventsub.events.ChannelFollowEvent
import com.github.twitch4j.eventsub.socket.IEventSubSocket
import com.github.twitch4j.eventsub.subscriptions.SubscriptionTypes
import com.github.twitch4j.pubsub.events.RewardRedeemedEvent
import com.theendercore.hydra.HydraMod.MODID
import com.theendercore.hydra.HydraMod.twitchClient
import com.theendercore.hydra.config.ModConfig
import com.theendercore.hydra.util.Methods.addChatMsg
import com.theendercore.hydra.util.darkGrayText
import com.theendercore.hydra.util.grayText
import com.theendercore.hydra.util.redText

object TwitchBot {

    @JvmField
    val ENABLED = false

    @JvmField
    var client: TwitchClient = TwitchClientBuilder.builder().build()

    @JvmField
    var credential: OAuth2Credential? = null

    @JvmField
    var eventSocket: IEventSubSocket? = null

    fun enable(): Int {
        return if (ENABLED) 0
        else {

            val config: ModConfig? = ModConfig.config
            credential = OAuth2Credential("twitch", config!!.oauthKey)

            if (config.username == "" || config.oauthKey == "") {
                addChatMsg(redText("command.$MODID.error.config"))
                return 0
            }
            if (twitchClient == null) {
                addChatMsg(darkGrayText("command.$MODID.connecting", config.username))
                twitchClient = TwitchClientBuilder.builder()
                    .withEnableHelix(true)
                    .withEnablePubSub(true)
                    .withEnableChat(true)
                    .withChatAccount(credential)
                    // New Api (yes its a pain)
                    .withEnableEventSocket(true)
                    .build()

                addChatMsg(darkGrayText("command.$MODID.connected"))
            } else {
                addChatMsg(darkGrayText("command.$MODID.connected.already", config.username))
                return 0
            }
            if (credential!!.userName == null) {
                addChatMsg(redText("command.$MODID.error.token.incorrect"))
                disable()
                return 0
            }
            if (config.extras) {
                val fetchedUsers = twitchClient!!.helix
                    .getUsers(credential!!.accessToken, null, listOf(config.username)).execute()

                config.broadcasterId = fetchedUsers.users[0].id
                config.save()

                twitchClient!!.pubSub.listenForChannelPointsRedemptionEvents(credential, config.broadcasterId)
                twitchClient!!.eventManager.onEvent(
                    RewardRedeemedEvent::class.java, EventListeners::rewardRedeemedListener
                )

                twitchClient!!.pubSub.listenForSubscriptionEvents(credential, config.broadcasterId)
                twitchClient!!.eventManager.onEvent(
                    SubscriptionEvent::class.java, EventListeners::subscriptionEventListener
                )

                eventSocket = twitchClient!!.getEventSocket()

                // create subscription
                eventSocket?.register(
                    SubscriptionTypes.CHANNEL_FOLLOW_V2
                        .prepareSubscription({ it.broadcasterUserId(config.broadcasterId).build() }, null)
                )

                // register event handler
                eventSocket?.eventManager?.onEvent(
                    ChannelFollowEvent::class.java, EventListeners::followingEventListener
                )

//                twitchClient!!.pubSub.listenForFollowingEvents(credential, config.broadcasterId)
//                twitchClient!!.eventManager.onEvent(FollowingEvent::class.java, EventListeners::followingEventListener)


                addChatMsg(grayText("command.$MODID.extras.enable"))
            }

            twitchClient!!.eventManager.onEvent(ChannelMessageEvent::class.java, EventListeners::channelMessageListener)

            1
        }

    }

    fun disable(): Int {
        return if (ENABLED) {
            client.pubSub.close()
            client.chat.close()
            client.close()
            addChatMsg(grayText("command.$MODID.disconnected"))
            1
        } else {
            addChatMsg(darkGrayText("command.$MODID.not_connected"))
            0
        }
    }
}