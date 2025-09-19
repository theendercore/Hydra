package com.theendercore.hydra.client

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential
import com.github.twitch4j.TwitchClient
import com.theendercore.hydra.client.config.ModConfig
import com.theendercore.hydra.client.init.HydraFabricEvents
import com.theendercore.hydra.client.init.KeyBindingRegistry
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory


object HydraMod {
    const val MODID = "hydra"

    @JvmField
    val LOGGER: Logger = LoggerFactory.getLogger(MODID)

    @JvmField
    var twitchClient: TwitchClient? = null

    @JvmField
    var credential: OAuth2Credential? = null

    fun id(path: String): Identifier = Identifier.of(MODID, path)

    @Suppress("unused")
    fun init() {
        LOGGER.info("Initializing world takeover!")

        ModConfig.config.load()
        credential = OAuth2Credential("twitch", ModConfig.config.oauthKey)

        KeyBindingRegistry.init()
        HydraFabricEvents.init()
    }
}
