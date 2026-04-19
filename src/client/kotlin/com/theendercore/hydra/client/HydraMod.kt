package com.theendercore.hydra.client

import com.github.twitch4j.TwitchClient
import com.theendercore.hydra.client.config.HydraConfig
import com.theendercore.hydra.client.init.HydraFabricEvents
import com.theendercore.hydra.client.init.HydraKeys
import me.fzzyhmstrs.fzzy_config.api.ConfigApi
import me.fzzyhmstrs.fzzy_config.api.RegisterType
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.resources.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory


object HydraMod {
    const val MODID = "hydra"

    @JvmField
    val config = ConfigApi.registerAndLoadConfig(::HydraConfig, RegisterType.CLIENT)

    @JvmField
    val LOGGER: Logger = LoggerFactory.getLogger(MODID)

    @JvmField
    var twitchClient: TwitchClient? = null

    fun id(path: String): Identifier = Identifier.fromNamespaceAndPath(MODID, path)

    fun isDev() = FabricLoader.getInstance().isDevelopmentEnvironment

    @Suppress("unused")
    fun init() {
        LOGGER.info("Initializing world takeover!")
        if (isDev()) HydraKeys.init()
        HydraFabricEvents.init()
    }
}
