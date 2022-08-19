package com.theendercore.hydra.twitch;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.common.enums.CommandPermission;
import com.github.twitch4j.pubsub.events.FollowingEvent;
import com.github.twitch4j.pubsub.events.RewardRedeemedEvent;
import com.theendercore.hydra.config.ModConfig;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

import java.util.*;

import static com.theendercore.hydra.HydraMod.*;
import static com.theendercore.hydra.util.Methods.*;

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
                MutableText translatableText = Text.translatable("listener." + MODID + ".reward.redeem").formatted(Formatting.WHITE);
                MutableText eventTitle = Text.literal(event.getRedemption().getReward().getTitle()).formatted(Formatting.DARK_GRAY);
                chatMessage(user.append(translatableText).append(eventTitle));
            }
        }
    }

    public static void channelMessageListener(ChannelMessageEvent event) {
        ModConfig c = ModConfig.getConfig();
        MutableText messageSender = Text.literal(event.getUser().getName()).formatted(c.getChannelChatColor().getFormat());
        Formatting messageColor = null;

        if (event.isHighlightedMessage()) {
            messageColor = Formatting.RED;
        }

        boolean isVip = false;
        Set<CommandPermission> perms = event.getPermissions();
        for (CommandPermission x : perms) {
            if (x == CommandPermission.VIP || x == CommandPermission.MODERATOR) {
                isVip = true;
                break;
            }
        }

        String color = event.getMessageEvent().getTags().get("color");
        if (color != null && !Objects.equals(event.getUser().getName(), c.getUsername())) {
            messageSender.setStyle(Text.literal("").getStyle().withColor(TextColor.parse(color)));
        } else if (!Objects.equals(event.getUser().getName(), c.getUsername())) {
            messageSender.formatted(Formatting.DARK_PURPLE);
        }
        addTwitchMessage(new Date(), messageSender, event.getMessage(), messageColor, c, isVip);
    }
}
