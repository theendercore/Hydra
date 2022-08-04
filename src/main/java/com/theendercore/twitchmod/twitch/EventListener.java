package com.theendercore.twitchmod.twitch;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.pubsub.events.FollowingEvent;
import com.github.twitch4j.pubsub.events.PubSubListenResponseEvent;
import com.github.twitch4j.pubsub.events.RewardRedeemedEvent;
import com.theendercore.twitchmod.TwitchMod;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import java.util.Date;
import static com.theendercore.twitchmod.TwitchMod.chatMessage;

public class EventListener {

    public void followingEventListener(FollowingEvent event) {
        chatMessage(Text.literal(event.getData().getUsername()));
    }
    public void rewardRedeemedListener(RewardRedeemedEvent event) {
        chatMessage(Text.literal(event.getRedemption().getReward().getTitle()));
//        LOGGER.info(event.toString());
    }
    public void channelMessageListener(ChannelMessageEvent event) {
//        LOGGER.info(event.toString());
        TwitchMod.addTwitchMessage(new Date(),event.getUser().getName(), event.getMessage(), Formatting.GRAY, false);
    }
}
