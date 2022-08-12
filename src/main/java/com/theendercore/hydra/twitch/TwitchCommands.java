package com.theendercore.hydra.twitch;

import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.helix.domain.UserList;
import com.github.twitch4j.pubsub.events.FollowingEvent;
import com.github.twitch4j.pubsub.events.RewardRedeemedEvent;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.theendercore.hydra.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.SculkChargeParticleEffect;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.Objects;

import static com.theendercore.hydra.HydraMod.*;
import static com.theendercore.hydra.util.Messages.*;

public class TwitchCommands {
    public static int enable(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ModConfig config = ModConfig.getConfig();
        if (Objects.equals(config.getUsername(), "") || Objects.equals(config.getOauthKey(), "")) {
            chatMessage(Text.translatable("command." + MODID + ".error.config").formatted(Formatting.RED));
            return 0;
        }

        if (twitchClient == null) {
            chatMessage(Text.translatable("command." + MODID + ".connecting", config.getUsername()).formatted(Formatting.DARK_GRAY));
            twitchClient = TwitchClientBuilder.builder().withEnableHelix(true).withEnablePubSub(true).withEnableChat(true).withChatAccount(credential).build();
            chatMessage(Text.translatable("command." + MODID + ".connected").formatted(Formatting.DARK_GRAY));

        } else {
            chatMessage(Text.translatable("command." + MODID + ".connected.already", config.getUsername()).formatted(Formatting.GRAY));
        }

        if (config.getExtras()) {
            UserList resultList = twitchClient.getHelix().getUsers(credential.getAccessToken(), null, List.of(config.getUsername())).execute();
            String channelID = resultList.getUsers().get(0).getId();
            LOGGER.info(channelID);
            twitchClient.getPubSub().listenForChannelPointsRedemptionEvents(credential, channelID);
            twitchClient.getPubSub().listenForFollowingEvents(credential, channelID);
            twitchClient.getEventManager().onEvent(RewardRedeemedEvent.class,(c) ->  EventListeners.rewardRedeemedListener(c, context.getSource().getPlayer()));
            twitchClient.getEventManager().onEvent(FollowingEvent.class, EventListeners::followingEventListener);
            chatMessage(Text.translatable("command." + MODID + ".extras.enable"));
        }

        twitchClient.getEventManager().onEvent(ChannelMessageEvent.class, EventListeners::channelMessageListener);
        return 1;
    }

    public static int disable(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        if (twitchClient == null) {
            chatMessage(Text.translatable("command." + MODID + ".disconnected.already").formatted(Formatting.DARK_GRAY));
            return 0;
        }
        twitchClient.getPubSub().close();
        twitchClient.getChat().close();
        twitchClient.close();
        twitchClient = null;
        chatMessage(Text.translatable("command." + MODID + ".disconnected").formatted(Formatting.GRAY));

        return 1;
    }

    public static int test(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {

    return 1;
    }
}
