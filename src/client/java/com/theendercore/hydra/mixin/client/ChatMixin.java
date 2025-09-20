package com.theendercore.hydra.mixin.client;

import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Date;

import static com.theendercore.hydra.client.HydraMod.config;
import static com.theendercore.hydra.client.HydraMod.twitchClient;
import static com.theendercore.hydra.client.util.MethodsKt.addTwitchMessage;


@Mixin(ChatScreen.class)
public class ChatMixin {
    @Inject(method = "handleChatInput", at = @At("HEAD"), cancellable = true)
    public void sendMessage(String text, boolean addToHistory, CallbackInfo ci) {
        String prefix = config.prefix;
        if (text.startsWith(prefix) && twitchClient != null) {
            String textWithoutPrefix = text.substring(text.indexOf(prefix) + prefix.length());
            addTwitchMessage(new Date(), Component.literal(config.credentials.username).withColor(config.getChannelChatColor().toInt()), textWithoutPrefix, null, true);
            twitchClient.getChat().sendMessage(config.credentials.username, textWithoutPrefix);
            ci.cancel();
        }
    }
}
