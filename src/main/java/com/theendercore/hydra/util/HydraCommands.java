package com.theendercore.hydra.util;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.server.command.ServerCommandSource;

import static com.theendercore.hydra.HydraMod.LOGGER;
import static com.theendercore.hydra.twitch.TwitchBot.*;

public class HydraCommands {
    public static int enable(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
//        return Enable(context.getSource());
        LOGGER.info(context.toString());
        return 1;
    }

    public static int disable(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        return Disable();
    }

    public static int test(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {

        return 1;
    }
}
