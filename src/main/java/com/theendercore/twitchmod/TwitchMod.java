package com.theendercore.twitchmod;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.theendercore.twitchmod.config.ModConfig;
import com.theendercore.twitchmod.twitch.EventListener;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;


public class TwitchMod implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("twitchmod");
    public static final EventListener eventListener = new EventListener();
    public static TwitchClient twitchClient;
    public static OAuth2Credential credential;

    public static void addTwitchMessage(Date date, String username, String message, Formatting textColor, boolean isMeMessage) {
        MutableText timestampText = Text.literal("[" + new SimpleDateFormat("HH:mm").format(date) + "]").formatted(Formatting.GRAY);
        MutableText usernameText = Text.literal(username).formatted(textColor);
        MutableText messageBodyText;
        if (!isMeMessage) {
            messageBodyText = Text.literal(": " + message).formatted(Formatting.WHITE);
        } else {
            messageBodyText = Text.literal(" " + message);
            usernameText = Text.literal("* ").append(usernameText);
        }
        chatMessage(timestampText.append(usernameText).append(messageBodyText));
    }

    public static void chatMessage(Text text) {
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(text);
    }

    @Override
    public void onInitialize() {
        ModConfig.getConfig().load();

        credential = new OAuth2Credential("twitch", ModConfig.getConfig().getOauthKey());

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, dedicated) -> {

            LiteralCommandNode<ServerCommandSource> killNode = CommandManager.literal("twitch").executes(new TwitchCommand()).build();

            dispatcher.getRoot().addChild(killNode);
        });
    }
}
