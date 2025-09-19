package com.theendercore.hydra.mixin.client;

import com.theendercore.hydra.client.config.ModConfig;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Date;
import java.util.Objects;

import static com.theendercore.hydra.client.HydraMod.twitchClient;
import static com.theendercore.hydra.client.util.MethodsKt.addTwitchMessage;


@Mixin(ChatScreen.class)
public class ChatMixin {
    @Inject(method = "handleChatInput", at = @At("HEAD"), cancellable = true)
    public void sendMessage(String text, boolean addToHistory, CallbackInfo ci) {
        ModConfig config = ModConfig.getConfig();
        String prefix = config.getPrefix();
        if (text.startsWith(prefix) && twitchClient != null) {
            String textWithoutPrefix = text.substring(text.indexOf(prefix) + prefix.length());
            addTwitchMessage(new Date(), Text.literal(config.getUsername()).formatted(Objects.requireNonNull(config.getChannelChatColor()).getFormat()), textWithoutPrefix, null, true);
            twitchClient.getChat().sendMessage(config.getUsername(), textWithoutPrefix);
            ci.cancel();
        }
    }
}
