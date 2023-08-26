package com.theendercore.hydra

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential
import com.github.twitch4j.TwitchClient
import com.theendercore.hydra.config.ModConfig
import com.theendercore.hydra.util.AutoStart
import com.theendercore.hydra.util.EmoteRenderer
import com.theendercore.hydra.util.reg.CommandRegistry
import com.theendercore.hydra.util.reg.KeyBindingRegistry
import com.theendercore.hydra.util.reg.TickRegistry
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.text.SimpleDateFormat
import java.util.*


class HydraMod : ClientModInitializer {

    @Suppress("unused")
    override fun onInitializeClient() {
        LOGGER.info("Initializing world takeover!")

        Files.createDirectories(Path.of("$cachePath/emotes"))
        File("$logPath").createNewFile()

        ModConfig.config?.load()
        credential = OAuth2Credential("twitch", ModConfig.config?.oauthKey)

        CommandRegistry.init()
        KeyBindingRegistry.init()
        TickRegistry.init()
        AutoStart.init()

        HudRenderCallback.EVENT.register(id("effect_timer"), HudRenderCallback { matrixStack, _ ->
            if (TickRegistry.timeRemainingInTicks > 0) {
                MinecraftClient.getInstance().textRenderer.drawWithShadow(
                    matrixStack,
                    SimpleDateFormat("mm:ss").format(Date((TickRegistry.timeRemainingInTicks * 50).toLong())),
                    10f,
                    10f,
                    0xffffff
                )
            }
        })

        HudRenderCallback.EVENT.register{ matrix: MatrixStack, f: Float ->
            EmoteRenderer.render(matrix, "theend42EndEmpire", 3f, 3f, 16f, 16f, 1.0f)
        }

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

        val modPath: Path = Path.of("${FabricLoader.getInstance().configDir}/hydra")

        val cachePath: Path = Path.of("$modPath/cache")
        val logPath: Path = Path.of("$cachePath/log.txt")

        var clipCount = 1
    }
}