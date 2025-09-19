package com.theendercore.hydra.client

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential
import com.github.twitch4j.TwitchClient
import com.theendercore.hydra.client.config.ModConfig
import com.theendercore.hydra.client.init.CommandRegistry
import com.theendercore.hydra.client.init.KeyBindingRegistry
import com.theendercore.hydra.client.init.TickRegistry
import com.theendercore.hydra.client.util.AutoStart
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

//    val modPath: Path = Path.of("${FabricLoader.getInstance().configDir}/hydra")

//        val cachePath: Path = Path.of("$modPath/cache")
//        val logPath: Path = Path.of("$cachePath/log.txt")

//    var clipCount = 1

    @Suppress("unused")
    fun init() {
        LOGGER.info("Initializing world takeover!")

//        Files.createDirectories(Path.of("$cachePath/emotes"))
//        File("$logPath").createNewFile()

        ModConfig.config?.load()
        credential = OAuth2Credential("twitch", ModConfig.config?.oauthKey)

        CommandRegistry.init()
        KeyBindingRegistry.init()
        TickRegistry.init()
        AutoStart.init()

//        HudRenderCallback.EVENT.register { graphics, _ ->
//            if (TickRegistry.timeRemainingInTicks > 0) {
//                graphics.drawText(
//                    MinecraftClient.getInstance().textRenderer,
//                    SimpleDateFormat("mm:ss").format(Date((TickRegistry.timeRemainingInTicks * 50).toLong())),
//                    10,
//                    10,
//                    0xffffff,
//                    true
//                )
//            }
//        }
    }
}
