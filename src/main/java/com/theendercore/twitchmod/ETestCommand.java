package com.theendercore.twitchmod;

import com.github.twitch4j.TwitchClientBuilder;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.theendercore.twitchmod.config.ModConfig;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

public class ETestCommand implements Command<ServerCommandSource> {
    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ModConfig config = ModConfig.getConfig();
        TwitchMod.LOGGER.info(context.getSource().getPlayer().getName().getString());
        TwitchMod.twitchClient = TwitchClientBuilder.builder().withEnableChat(true).withChatAccount(TwitchMod.credential).build();
        String Message = "ola form mc!";
        TwitchMod.twitchClient.getChat().sendMessage(config.getUsername(), Message);
        context.getSource().sendFeedback(new LiteralText(Message), false);
        return 1;
    }
}
