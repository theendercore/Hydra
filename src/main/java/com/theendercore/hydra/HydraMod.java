    package com.theendercore.hydra;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.theendercore.hydra.config.ModConfig;
import com.theendercore.hydra.util.AutoStart;
import com.theendercore.hydra.util.CommandRegistry;
import com.theendercore.hydra.util.KeyBindingRegistry;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class HydraMod implements ClientModInitializer {
    public static final String MODID = "hydra";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    public static TwitchClient twitchClient;
    public static OAuth2Credential credential;
    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing world takeover!");
        ModConfig.getConfig().load();

        credential = new OAuth2Credential("twitch", ModConfig.getConfig().getOauthKey());

        CommandRegistry.init();
        KeyBindingRegistry.init();
        AutoStart.init();
    }
}
