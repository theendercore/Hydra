package com.theendercore.hydra.util;

import com.theendercore.hydra.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import javax.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.Date;

import static net.minecraft.util.Formatting.FORMATTING_CODE_PREFIX;

public class Methods {
    public static void addTwitchMessage(Date date, MutableText usernameText, String message, @Nullable Formatting chatColor, ModConfig c, Boolean isVIP) {
        MutableText timestampText = Text.literal("[" + new SimpleDateFormat(c.getTimeFormatting()).format(date) + "]").formatted(Formatting.GRAY);
        MutableText messageBodyText = Text.literal(": ").formatted(Formatting.WHITE);
        if (!isVIP) {
            message = message.replaceAll(String.valueOf(FORMATTING_CODE_PREFIX),"$");
        }
        if (chatColor == null) {
            messageBodyText.append(Text.literal(message));
        } else {
            messageBodyText.append(Text.literal(message).formatted(chatColor, Formatting.BOLD, Formatting.ITALIC));
        }
        chatMessage(timestampText.append(usernameText).append(messageBodyText));
    }

    public static void chatMessage(Text text) {
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(text);
    }

    public static void titleMessage(@Nullable Text text, @Nullable Text smallText) {
        InGameHud hud = MinecraftClient.getInstance().inGameHud;
        if (!(text == null)) {
            hud.setTitle(text);
        }
        if (!(smallText == null)) {
            hud.setSubtitle(smallText);
        }
    }
}
