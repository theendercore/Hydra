package com.theendercore.hydra

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential
import com.github.twitch4j.TwitchClient
import com.theendercore.hydra.config.ModConfig
import com.theendercore.hydra.util.AutoStart
import com.theendercore.hydra.util.CommandRegistry
import com.theendercore.hydra.util.KeyBindingRegistry
import net.fabricmc.api.ClientModInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory





const val MODID = "hydra"
@JvmField
var twitchClient: TwitchClient? = null
@JvmField
var credential: OAuth2Credential? = null
@JvmField
val LOGGER: Logger = LoggerFactory.getLogger(MODID)

@Suppress("unused")
fun onInitialize() {
        LOGGER.info("Initializing world takeover!")

        ModConfig.config?.load()
        credential = OAuth2Credential("twitch", ModConfig.config?.oauthKey)

//        CommandRegistry.init()
//        KeyBindingRegistry.init()
//        AutoStart.init()
}

