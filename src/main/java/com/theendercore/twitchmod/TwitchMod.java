package com.theendercore.twitchmod;

import com.theendercore.twitchmod.config.ModConfig;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwitchMod implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("twitchmod");
//    TwitchClient twitchClient = TwitchClientBuilder.builder()
//            .withEnableHelix(true)
//            .build();

    @Override
    public void onInitialize() {
        ModConfig.getConfig().load();
        LOGGER.info("Hello Fabric world!");
    }
}
