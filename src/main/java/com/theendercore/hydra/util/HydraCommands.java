package com.theendercore.hydra.util;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.theendercore.hydra.twitch.TwitchBot;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class HydraCommands {
    public static int enable(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        return TwitchBot.Enable();
    }

    public static int disable(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        return TwitchBot.Disable();
    }

    public static int test(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {

        return 1;
    }
}
