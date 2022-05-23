package com.theendercore.twitchmod;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.theendercore.twitchmod.config.ModConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwitchMod implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("twitchmod");
    public static TwitchClient twitchClient;
    public static OAuth2Credential credential;

    @Override
    public void onInitialize() {
        ModConfig.getConfig().load();

        credential = new OAuth2Credential("twitch", ModConfig.getConfig().getOauthKey());

        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {

            LiteralCommandNode<ServerCommandSource> killNode = CommandManager.literal("etest").executes(new ETestCommand()).build();

            dispatcher.getRoot().addChild(killNode);
        });
    }
}
