package com.theendercore.hydra;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.theendercore.hydra.config.ModConfig;
import com.theendercore.hydra.util.CommandRegistry;
import com.theendercore.hydra.util.KeyBindingRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.Date;


public class HydraMod implements ClientModInitializer {
    public static final String MODID = "hydra";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    public static TwitchClient twitchClient;
    public static OAuth2Credential credential;
    @Override
    public void onInitializeClient() {
        ModConfig.getConfig().load();
        credential = new OAuth2Credential("twitch", ModConfig.getConfig().getOauthKey());

        CommandRegistry.init();
        KeyBindingRegistry.init();
    }
}
