package com.theendercore.hydra

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential
import com.github.twitch4j.TwitchClient
import com.theendercore.hydra.config.ModConfig
import com.theendercore.hydra.util.AutoStart
import com.theendercore.hydra.util.reg.CommandRegistry
import com.theendercore.hydra.util.reg.KeyBindingRegistry
import net.fabricmc.api.ClientModInitializer
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class HydraMod : ClientModInitializer {
    @Suppress("unused")
    override fun onInitializeClient() {
        LOGGER.info("Initializing world takeover!")

        ModConfig.config?.load()
        credential = OAuth2Credential("twitch", ModConfig.config?.oauthKey)

        CommandRegistry.init()
        KeyBindingRegistry.init()
        AutoStart.init()
    }

    companion object {
        const val MODID = "hydra"
        @JvmField
        val LOGGER: Logger = LoggerFactory.getLogger(MODID)

        @JvmField
        var twitchClient: TwitchClient? = null

        @JvmField
        var credential: OAuth2Credential? = null

        val id: (String) -> Identifier = { Identifier(MODID, it) }
    }
}