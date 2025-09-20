package com.theendercore.hydra.mixin.client.debug;

import com.github.twitch4j.eventsub.events.EventSubUserChannelEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@SuppressWarnings("unused")
@Mixin(value = EventSubUserChannelEvent.class, remap = false)
public interface EventSubUserChannelEventAccessor {
    @Accessor("broadcasterUserId") void setBroadcasterUserId(String in);
    @Accessor("broadcasterUserName") void setBroadcasterUserName(String in);
    @Accessor("broadcasterUserLogin") void setBroadcasterUserLogin(String in);
    @Accessor("userId") void setUserId(String in);
    @Accessor("userName") void setUserName(String in);
    @Accessor("userLogin") void setUserLogin(String in);
}
