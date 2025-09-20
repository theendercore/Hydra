package com.theendercore.hydra.client.util

import com.github.twitch4j.common.enums.SubscriptionPlan
import com.github.twitch4j.eventsub.domain.Message
import com.github.twitch4j.eventsub.events.ChannelSubscribeEvent
import com.github.twitch4j.eventsub.events.ChannelSubscriptionGiftEvent
import com.github.twitch4j.eventsub.events.ChannelSubscriptionMessageEvent
import com.github.twitch4j.eventsub.events.EventSubUserChannelEvent
import com.theendercore.hydra.client.twitch.EventListeners.subEvent
import com.theendercore.hydra.mixin.client.debug.ChannelSubscribeEventAccessor
import com.theendercore.hydra.mixin.client.debug.ChannelSubscriptionGiftEventAccessor
import com.theendercore.hydra.mixin.client.debug.ChannelSubscriptionMessageEventAccessor
import com.theendercore.hydra.mixin.client.debug.SubMessageAccessor

var events = listOf<EventSubUserChannelEvent>(
    buildSubEvent {
        setUserName("Jeffomatic")
        setTier(SubscriptionPlan.TWITCH_PRIME)
    },
    buildSubEvent {
        setUserName("Enddio")
        setTier(SubscriptionPlan.TIER2)
    },
    buildGiftSubEvent {
        setUserName("Giftsan")
        setTier(SubscriptionPlan.TIER1)
        setTotal(5)
    },
    buildGiftSubEvent {
        setUserName("EvilMegaCorp")
        setIsAnonymous(true)
        setTier(SubscriptionPlan.TIER3)
        setTotal(50)
        setCumulativeTotal(1000)
    },
    buildReSubEvent {
        setUserName("Devoted follower")
        setTier(SubscriptionPlan.TIER1)
        setMessage(subMessage("Chat this guys is the best streamer out there"))
        setCumulativeMonths(10)
    },
    buildReSubEvent {
        setUserName("Silent Subber")
        setTier(SubscriptionPlan.TWITCH_PRIME)
        setMessage(subMessage(""))
        setCumulativeMonths(4)
    },
    buildReSubEvent {
        setUserName("First Timer")
        setTier(SubscriptionPlan.TIER1)
        setMessage(subMessage("Heh"))
        setCumulativeMonths(1)
    },
)

fun runDebugCode() {
    events.random().let {
        when (it) {
            is ChannelSubscribeEvent -> subEvent(it)
            is ChannelSubscriptionGiftEvent -> subEvent(it)
            is ChannelSubscriptionMessageEvent -> subEvent(it)
            else -> error("What?")
        }
    }
}


fun buildSubEvent(function: ChannelSubscribeEventAccessor.() -> Unit): ChannelSubscribeEvent {
    val event = ChannelSubscribeEvent()
    function((event as ChannelSubscribeEventAccessor))
    return event
}

fun buildGiftSubEvent(function: ChannelSubscriptionGiftEventAccessor.() -> Unit): ChannelSubscriptionGiftEvent {
    val event = ChannelSubscriptionGiftEvent()
    function((event as ChannelSubscriptionGiftEventAccessor))
    return event
}

fun buildReSubEvent(function: ChannelSubscriptionMessageEventAccessor.() -> Unit): ChannelSubscriptionMessageEvent {
    val event = ChannelSubscriptionMessageEvent()
    function((event as ChannelSubscriptionMessageEventAccessor))
    return event
}

fun subMessage(text: String): Message {
    val msg = Message()
    @Suppress("KotlinConstantConditions")
    (msg as SubMessageAccessor).setText(text)
    return msg
}

