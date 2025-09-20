package com.theendercore.hydra.mixin.client.debug;

import com.github.twitch4j.eventsub.domain.Message;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = Message.class, remap = false)
public interface SubMessageAccessor {
    @Accessor("text") void setText(String text);
}
