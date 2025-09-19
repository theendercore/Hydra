package com.theendercore.hydra.client.util

import com.github.twitch4j.eventsub.condition.ChannelEventSubCondition
import com.github.twitch4j.eventsub.condition.EventSubCondition
import com.github.twitch4j.eventsub.events.EventSubEvent
import com.github.twitch4j.eventsub.subscriptions.SubscriptionType
import com.theendercore.hydra.client.HydraMod.config
import com.theendercore.hydra.client.twitch.TwitchBot.eventSocket
import java.util.function.Consumer

@Suppress("UNCHECKED_CAST")
fun <C : EventSubCondition, B, E : EventSubEvent> register(
    type: SubscriptionType<C, B, E>, consumer: Consumer<E>,
) {
    eventSocket?.register(
        type.prepareSubscription({
            when (it) {
                is ChannelEventSubCondition.ChannelEventSubConditionBuilder<*, *> ->
                    it.broadcasterUserId(config.credentials.broadcasterId).build() as C

                is EventSubCondition.EventSubConditionBuilder<*, *> -> it.build() as C
                else -> error("Event code no worky, ender very sad!")
            }
        }, null)
    )
    eventSocket?.eventManager?.onEvent(type.eventClass as Class<E>, consumer)
}
