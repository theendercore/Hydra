package com.theendercore.hydra.mixin.client.debug;

import com.github.twitch4j.common.enums.SubscriptionPlan;
import com.github.twitch4j.eventsub.events.ChannelSubscriptionGiftEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = ChannelSubscriptionGiftEvent.class, remap = false )
public interface ChannelSubscriptionGiftEventAccessor extends EventSubUserChannelEventAccessor {
    @Accessor("tier") void setTier(SubscriptionPlan tier);
    @Accessor("total") void setTotal(Integer total);
    @Accessor("cumulativeTotal") void setCumulativeTotal(@Nullable Integer total);
    @Accessor("isAnonymous") void setIsAnonymous(Boolean total);
}
