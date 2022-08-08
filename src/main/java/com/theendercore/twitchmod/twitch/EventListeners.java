package com.theendercore.twitchmod.twitch;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.pubsub.events.FollowingEvent;
import com.github.twitch4j.pubsub.events.RewardRedeemedEvent;
import com.theendercore.twitchmod.config.ModConfig;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Date;
import java.util.Objects;

import static com.theendercore.twitchmod.TwitchMod.*;

public class EventListeners {

    public static void followingEventListener(FollowingEvent event) {
        chatMessage(Text.literal(event.getData().getUsername()));
    }

    public static void rewardRedeemedListener(RewardRedeemedEvent event) {
        String eTitle = event.getRedemption().getReward().getTitle();
        switch (eTitle) {
            case "Hydrate!" -> titleMessage(Text.literal(event.getRedemption().getReward().getTitle()), null);
            case "PP" -> LOGGER.info("yoo");
            default -> {
                MutableText eventTitle = Text.literal(event.getRedemption().getReward().getTitle()).formatted(Formatting.DARK_GRAY);
                MutableText translatableText = Text.translatable("listener.twitchmod.reward");
                MutableText user = Text.literal(event.getRedemption().getUser().getDisplayName()).formatted(Formatting.DARK_GRAY);
                chatMessage(eventTitle.append(translatableText).append(user));
            }
        }
    }

    public static void channelMessageListener(ChannelMessageEvent event) {
        ModConfig c = ModConfig.getConfig();
        if (Objects.equals(event.getUser().getName(), c.getUsername())) {
            addTwitchMessage(new Date(), event.getUser().getName(), event.getMessage(), c.getChannelChatColor(), false, c);
        } else {
            addTwitchMessage(new Date(), event.getUser().getName(), event.getMessage(), Formatting.GRAY, false, c);
        }

    }
}
