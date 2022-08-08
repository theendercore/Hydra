package com.theendercore.hydra;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.theendercore.hydra.config.ModConfig;
import com.theendercore.hydra.twitch.TwitchCommands;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.Date;


public class HydraMod implements ModInitializer {
    public static final String MODID = "hydra";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    public static TwitchClient twitchClient;
    public static OAuth2Credential credential;

    public static void addTwitchMessage(Date date, String username, String message, Formatting textColor, boolean isMeMessage, ModConfig c) {
        MutableText timestampText = Text.literal("[" + new SimpleDateFormat(c.getTimeFormatting()).format(date) + "]").formatted(Formatting.GRAY);
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

    public static void titleMessage(@Nullable Text text, @Nullable Text smallText) {
        InGameHud hud = MinecraftClient.getInstance().inGameHud;
        if (!(text == null)) {
            hud.setTitle(text);
        }
        if (!(smallText == null)) {
            hud.setSubtitle(smallText);
        }
    }

    @Override
    public void onInitialize() {
        ModConfig.getConfig().load();

        credential = new OAuth2Credential("twitch", ModConfig.getConfig().getOauthKey());

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, dedicated) -> {

            LiteralCommandNode<ServerCommandSource> twitchNode = CommandManager.literal("twitch").build();
            LiteralCommandNode<ServerCommandSource> enableNode = CommandManager.literal("enable").executes(TwitchCommands::enable).build();
            LiteralCommandNode<ServerCommandSource> disableNode = CommandManager.literal("disable").executes(TwitchCommands::disable).build();

            dispatcher.getRoot().addChild(twitchNode);
            twitchNode.addChild(enableNode);
            twitchNode.addChild(disableNode);
        });
    }
}
