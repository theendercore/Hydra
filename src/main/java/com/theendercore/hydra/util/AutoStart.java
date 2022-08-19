package com.theendercore.hydra.util;

import com.theendercore.hydra.config.ModConfig;
import com.theendercore.hydra.twitch.TwitchBot;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static com.theendercore.hydra.HydraMod.*;
import static com.theendercore.hydra.util.Methods.chatMessage;

public class AutoStart {

    public static void init() {
        ClientPlayConnectionEvents.JOIN.register((clientPlayNetworkHandler, packetSender, client) -> {
            if (ModConfig.getConfig().getAutoStart() && twitchClient == null) {
                chatMessage(Text.translatable("system." + MODID + ".auto_load").formatted(Formatting.DARK_GRAY));
                TwitchBot.Enable();
            }
        });

        ClientLifecycleEvents.CLIENT_STOPPING.register((client) -> {
            TwitchBot.Disable();
            LOGGER.info("Disable Twitch bot.");
        });
    }
}
