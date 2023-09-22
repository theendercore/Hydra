package com.theendercore.hydra.twitch

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential
import com.github.twitch4j.TwitchClient
import com.github.twitch4j.TwitchClientBuilder
import com.theendercore.hydra.HydraMod.Companion.MODID
import com.theendercore.hydra.util.Methods.addChatMsg
import com.theendercore.hydra.util.darkGrayText
import com.theendercore.hydra.util.grayText

object TwitchBot {

    @JvmField
    val ENABLED = false

    @JvmField
    var client: TwitchClient = TwitchClientBuilder.builder().build();

    @JvmField
    var credential: OAuth2Credential? = null

    fun enable(): Int {
       return if (ENABLED) 0
        else {



            1
        }

//        val config: ModConfig? = ModConfig.config
//        credential = OAuth2Credential("twitch", config!!.oauthKey)
//        if (config.username == "" || config.oauthKey == "") {
//            chatMessage(Text.translatable("command.$MODID.error.config").formatted(Formatting.RED))
//            return 0
//        }
//        if (twitchClient == null) {
//            chatMessage(darkGrayText("command.$MODID.connecting", config.username))
//            twitchClient = TwitchClientBuilder.builder()
//                .withEnableHelix(true)
//                .withEnablePubSub(true)
//                .withEnableChat(true)
//                .withChatAccount(credential)
//                .build()
//
//            chatMessage(darkGrayText("command.$MODID.connected"))
//        } else {
//            chatMessage(darkGrayText("command.$MODID.connected.already", config.username))
//            return 0
//        }
//        if (credential!!.userName == null) {
//            chatMessage(redText("command.$MODID.error.token.incorrect"))
//            disable()
//            return 0
//        }
//        if (config.extras) {
//            val resultList = twitchClient!!.helix.getUsers(
//                credential!!.accessToken,
//                null,
//                listOf(config.username)
//            ).execute()
//
//            config.broadcasterId = resultList.users[0].id
//            config.save()
//
//            twitchClient!!.pubSub.listenForChannelPointsRedemptionEvents(credential, config.broadcasterId)
//            twitchClient!!.pubSub.listenForFollowingEvents(credential, config.broadcasterId)
//            twitchClient!!.pubSub.listenForSubscriptionEvents(credential, config.broadcasterId)
//            twitchClient!!.eventManager.onEvent(RewardRedeemedEvent::class.java) { event: RewardRedeemedEvent ->
//                EventListeners.rewardRedeemedListener(
//                    event
//                )
//            }
//            twitchClient!!.eventManager.onEvent(FollowingEvent::class.java) { event: FollowingEvent ->
//                EventListeners.followingEventListener(
//                    event
//                )
//            }
//            twitchClient!!.eventManager.onEvent(
//                SubscriptionEvent::class.java
//            ) { event: SubscriptionEvent -> EventListeners.subscriptionEventListener(event) }
//
////            if (config.enableCache) {
////                val emoteList = twitchClient!!.helix.getChannelEmotes(credential!!.accessToken, config.broadcasterId).execute()
////                emoteList.emotes.forEach {
////                    LOGGER.info(it.name)
////                    saveEmote(it.images.largeImageUrl, it.name)
////                }
////            }
//
//            chatMessage(Text.translatable("command.$MODID.extras.enable"))
//        }
//
//        twitchClient!!.eventManager.onEvent(ChannelMessageEvent::class.java) { EventListeners.channelMessageListener(it) }


    }

    fun disable(): Int {
        return if (ENABLED) {
            client.pubSub.close()
            client.chat.close()
            client.close()
            addChatMsg(grayText("command.$MODID.disconnected"))
            1
        }
        else {
            addChatMsg(darkGrayText("command.$MODID.not_connected"))
            0
        }

    }
}