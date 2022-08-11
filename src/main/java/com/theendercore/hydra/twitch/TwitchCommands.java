package com.theendercore.hydra.twitch;

import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.pubsub.events.FollowingEvent;
import com.github.twitch4j.pubsub.events.RewardRedeemedEvent;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.theendercore.hydra.config.ModConfig;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Objects;

import static com.theendercore.hydra.HydraMod.*;


public class TwitchCommands {
    public static int enable(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ModConfig config = ModConfig.getConfig();
        if (Objects.equals(config.getUsername(), "") || Objects.equals(config.getOauthKey(), "")) {
            chatMessage(Text.translatable("command." + MODID + ".error.config").formatted(Formatting.RED));
            return 0;
        }

        if (twitchClient == null) {
            chatMessage(Text.translatable("command." + MODID + ".connecting", config.getUsername()).formatted(Formatting.DARK_GRAY));
            twitchClient = TwitchClientBuilder.builder().withEnablePubSub(true).withEnableChat(true).withChatAccount(credential).build();
            chatMessage(Text.translatable("command." + MODID + ".connected").formatted(Formatting.DARK_GRAY));
        } else {
            chatMessage(Text.translatable("command." + MODID + ".connected.already", config.getUsername()).formatted(Formatting.GRAY));
        }

        if (!Objects.equals(config.getChannelID(), "") & config.getExtras()) {
            twitchClient.getPubSub().listenForChannelPointsRedemptionEvents(credential, config.getChannelID());
            twitchClient.getPubSub().listenForFollowingEvents(credential, config.getChannelID());
            twitchClient.getEventManager().onEvent(RewardRedeemedEvent.class, EventListeners::rewardRedeemedListener);
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
}
