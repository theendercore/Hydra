package com.theendercore.hydra.twitch;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.pubsub.events.FollowingEvent;
import com.github.twitch4j.pubsub.events.RewardRedeemedEvent;
import com.theendercore.hydra.config.ModConfig;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Date;
import java.util.Objects;

import static com.theendercore.hydra.HydraMod.*;

public class EventListeners {

    public static void followingEventListener(FollowingEvent event) {
        MutableText follower = Text.literal(event.getData().getDisplayName()).formatted(Formatting.AQUA);
        MutableText after = Text.literal(" just Followed!").formatted(Formatting.WHITE);
        chatMessage(follower.append(after));
    }

    public static void rewardRedeemedListener(RewardRedeemedEvent event, ServerPlayerEntity player) {
        String eTitle = event.getRedemption().getReward().getTitle();
        switch (eTitle) {
            case "Hydrate!" ->
                    titleMessage(Text.literal(event.getRedemption().getReward().getTitle()).formatted(Formatting.BLUE), Text.literal("Redeemed by " + event.getRedemption().getUser().getDisplayName()).formatted(Formatting.GRAY));
            case "PP" -> LOGGER.info("yoo");
            case "Point waste" -> titleMessage(Text.literal(player.getPos().toString()), null);
            default -> {
                MutableText user = Text.literal(event.getRedemption().getUser().getDisplayName()).formatted(Formatting.DARK_GRAY);
                MutableText translatableText = Text.translatable("listener." + MODID + ".reward").formatted(Formatting.WHITE);
                MutableText eventTitle = Text.literal(event.getRedemption().getReward().getTitle()).formatted(Formatting.DARK_GRAY);
                chatMessage(user.append(translatableText).append(eventTitle));
            }
        }
    }

    public static void channelMessageListener(ChannelMessageEvent event) {
        ModConfig c = ModConfig.getConfig();
        Formatting userColor = Formatting.GRAY;
        Formatting messageColor = null;
        if (Objects.equals(event.getUser().getName(), c.getUsername())) {
            userColor = c.getChannelChatColor().getFormat();
        }
        if (event.isHighlightedMessage()) {
            messageColor = Formatting.RED;
        }
        addTwitchMessage(new Date(), event.getUser().getName(), event.getMessage(), userColor, messageColor, false, c);
    }
}
