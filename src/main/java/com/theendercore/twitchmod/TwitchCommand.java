package com.theendercore.twitchmod;

import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.helix.domain.UserList;
import com.github.twitch4j.pubsub.events.RewardRedeemedEvent;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.theendercore.twitchmod.config.ModConfig;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import java.util.List;
import java.util.Objects;

import static com.theendercore.twitchmod.TwitchMod.*;
import static com.theendercore.twitchmod.TwitchMod.chatMessage;

public class TwitchCommand  {
    public static int enable(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ModConfig config = ModConfig.getConfig();
        if (Objects.equals(config.getChannelID(), "") || Objects.equals(config.getUsername(), "") || Objects.equals(config.getOauthKey(), "")){
            chatMessage(Text.literal("Config not set up properly!").formatted(Formatting.RED));
            return 0;
        }

        if (twitchClient == null) {
            chatMessage(Text.literal("twitch connecting...").formatted(Formatting.DARK_GRAY));
            twitchClient = TwitchClientBuilder.builder()
                    .withEnableHelix(true)
                    .withEnablePubSub(true)
                    .withEnableChat(true)
                    .withChatAccount(credential)
                    .build();
            chatMessage(Text.literal("twitch bot enable").formatted(Formatting.DARK_GRAY));
        } else {
            chatMessage(Text.literal("twitch bot is already on").formatted(Formatting.GRAY));
        }


        twitchClient.getPubSub().listenForChannelPointsRedemptionEvents(credential, config.getChannelID());

//        twitchClient.getEventManager().onEvent(FollowingEvent.class, eventListener::followingEventListener);
        twitchClient.getEventManager().onEvent(RewardRedeemedEvent.class, eventListener::rewardRedeemedListener);
        twitchClient.getEventManager().onEvent(ChannelMessageEvent.class, eventListener::channelMessageListener);

        return 1;
    }
    public static int disable(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        if (twitchClient == null) {
            chatMessage(Text.literal("twitch bot is already disabled").formatted(Formatting.DARK_GRAY));
        } else {
            twitchClient.close();
            twitchClient = null;
            chatMessage(Text.literal("twitch bot is turned off").formatted(Formatting.GRAY));
        }
        return 1;
    }
}
