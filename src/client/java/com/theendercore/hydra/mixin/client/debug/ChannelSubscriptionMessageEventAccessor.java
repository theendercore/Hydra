package com.theendercore.hydra.mixin.client.debug;

import com.github.twitch4j.common.enums.SubscriptionPlan;
import com.github.twitch4j.eventsub.domain.Message;
import com.github.twitch4j.eventsub.events.ChannelSubscriptionMessageEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@SuppressWarnings("unused")
@Mixin(value = ChannelSubscriptionMessageEvent.class, remap = false)
public interface ChannelSubscriptionMessageEventAccessor extends EventSubUserChannelEventAccessor {
    @Accessor("tier") void setTier(SubscriptionPlan tier);
    @Accessor("message") void setMessage(Message message);
    @Accessor("cumulativeMonths") void setCumulativeMonths(Integer cumulativeMonths);
    @Accessor("streakMonths")  void setStreakMonths(@Nullable Integer streakMonths);
    @Accessor("durationMonths") void setDurationMonths(Integer durationMonths);
}
