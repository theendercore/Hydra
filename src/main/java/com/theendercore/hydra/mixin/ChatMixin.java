package com.theendercore.hydra.mixin;

import com.theendercore.hydra.config.ModConfig;
import com.theendercore.hydra.util.Methods;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Date;
import java.util.Objects;

import static com.theendercore.hydra.HydraMod.twitchClient;


@Mixin(ChatScreen.class)
public class ChatMixin {
    @Inject(at = @At("HEAD"), method = "sendMessage(Ljava/lang/String;Z)Z", cancellable = true)
    public void sendMessage(String text, boolean addToHistory, CallbackInfoReturnable<Boolean> info) {
        ModConfig config = ModConfig.Companion.getConfig();
        assert config != null;
        String prefix = config.getPrefix();
        if (text.startsWith(prefix) & twitchClient != null) {
            String textWithoutPrefix = text.substring(text.indexOf(prefix) + prefix.length());
            Methods.INSTANCE.addTwitchMessage(new Date(), Text.literal(config.getUsername()).formatted(Objects.requireNonNull(config.getChannelChatColor()).getFormat()), textWithoutPrefix, null , true);
            twitchClient.getChat().sendMessage(config.getUsername(), textWithoutPrefix);
            info.setReturnValue(true);
        }
    }
}
