package com.theendercore.hydra.mixin.client.debug;

import com.github.twitch4j.common.enums.SubscriptionPlan;
import com.github.twitch4j.eventsub.events.ChannelSubscribeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@SuppressWarnings("unused")
@Mixin(value = ChannelSubscribeEvent.class, remap = false)
public interface ChannelSubscribeEventAccessor extends EventSubUserChannelEventAccessor {
    @Accessor("tier") void setTier(SubscriptionPlan tier);
    @Accessor("isGift") void setIsGift(Boolean isGift);
}
