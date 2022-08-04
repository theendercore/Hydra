package com.theendercore.twitchmod;

import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.pubsub.events.RewardRedeemedEvent;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.theendercore.twitchmod.config.ModConfig;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static com.theendercore.twitchmod.TwitchMod.*;

public class TwitchCommand implements Command<ServerCommandSource> {
    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ModConfig config = ModConfig.getConfig();

        TwitchMod.LOGGER.info(context.getSource().getPlayer().getName().getString());
        if (twitchClient == null) {
            chatMessage(Text.literal("twitch connecting...").formatted(Formatting.DARK_GRAY));
            twitchClient = TwitchClientBuilder.builder().withEnableHelix(true).withEnablePubSub(true).withEnableChat(true).withChatAccount(credential).build();
            chatMessage(Text.literal("twitch bot enable").formatted(Formatting.DARK_GRAY));
        }
        else {
            chatMessage(Text.literal("twitch bot is already on").formatted(Formatting.BLUE));
        }

//        twitchClient.getPubSub().listenForChannelPointsRedemptionEvents(credential, ModConfig.getConfig().getUsername());

//        twitchClient.getEventManager().onEvent(FollowingEvent.class, eventListener::followingEventListener);
        twitchClient.getEventManager().onEvent(RewardRedeemedEvent.class, eventListener::rewardRedeemedListener);
        twitchClient.getEventManager().onEvent(ChannelMessageEvent.class, eventListener::channelMessageListener);

        return 1;
    }
}
