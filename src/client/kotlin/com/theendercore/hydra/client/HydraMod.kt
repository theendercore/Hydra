package com.theendercore.hydra.client

import com.github.twitch4j.TwitchClient
import com.theendercore.hydra.client.config.HydraConfig
import com.theendercore.hydra.client.init.HydraFabricEvents
import com.theendercore.hydra.client.init.HydraKeys
import me.fzzyhmstrs.fzzy_config.api.ConfigApi
import me.fzzyhmstrs.fzzy_config.api.RegisterType
import net.minecraft.util.Identifier
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

    fun id(path: String): Identifier = Identifier.of(MODID, path)

    @Suppress("unused")
    fun init() {
        LOGGER.info("Initializing world takeover!")
        HydraKeys.init()
        HydraFabricEvents.init()
    }
}
