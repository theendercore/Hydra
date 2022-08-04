package com.theendercore.twitchmod.mixin;

import com.theendercore.twitchmod.TwitchMod;
import com.theendercore.twitchmod.config.ModConfig;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Date;

import static com.theendercore.twitchmod.TwitchMod.*;
import static net.fabricmc.api.EnvType.CLIENT;


@Environment(CLIENT)
@Mixin(ChatScreen.class)
public class ChatMixin {
    @Inject(at = @At("HEAD"), method = "sendMessage(Ljava/lang/String;Z)Z", cancellable = true)
    public void sendMessage(String text, boolean addToHistory, CallbackInfoReturnable<Boolean> info) {
        ModConfig config = ModConfig.getConfig();
        String prefix = "!";
        if (text.startsWith(prefix) & twitchClient != null) {
            String textWithoutPrefix = text.substring(text.indexOf(prefix) + prefix.length());
            addTwitchMessage(new Date(),config.getUsername(), textWithoutPrefix, Formatting.BLUE, false);
            twitchClient.getChat().sendMessage(config.getUsername(), textWithoutPrefix);
            info.setReturnValue(true);
        }
        else if (text.startsWith(prefix)) {
            chatMessage(Text.literal("1auuh").formatted(Formatting.AQUA, Formatting.ITALIC));
            info.setReturnValue(true);
        }
    }
}
