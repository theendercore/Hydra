package com.theendercore.hydra.util

import com.theendercore.hydra.LOGGER
import com.theendercore.hydra.config.ModConfig
import com.theendercore.hydra.mixin.GameRendererAccessor
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.GameRenderer
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.math.random.Random
import java.text.SimpleDateFormat
import java.util.*

object Methods {
    private val renderer = MinecraftClient.getInstance().gameRenderer
    fun addTwitchMessage(
        date: Date?,
        usernameText: MutableText?,
        msg: String,
        chatColor: Formatting?,
        c: ModConfig,
        isVIP: Boolean?
    ) {
        var message = msg
        val timestampText = Text.literal("[" + SimpleDateFormat(c.timeFormatting).format(date) + "]").formatted(
            Formatting.GRAY
        )
        val messageBodyText = Text.literal(": ").formatted(Formatting.WHITE)
        if (!isVIP!!) {
            message = message.replace(Formatting.FORMATTING_CODE_PREFIX.toString().toRegex(), "$")
        }
        if (chatColor == null) {
            messageBodyText.append(Text.literal(message))
        } else {
            messageBodyText.append(Text.literal(message).formatted(chatColor, Formatting.BOLD, Formatting.ITALIC))
        }
        chatMessage(timestampText.append(usernameText).append(messageBodyText))
    }

    fun chatMessage(text: Text?) {
        MinecraftClient.getInstance().inGameHud.chatHud.addMessage(text)
    }

    fun titleMessage(text: Text?, smallText: Text?) {
        val hud = MinecraftClient.getInstance().inGameHud
        if (text != null) {
            hud.setTitle(text)
        }
        if (smallText != null) {
            hud.setSubtitle(smallText)
        }
    }

    fun setRandomShader() {
        val shader = GameRendererAccessor.getShaderLocations()[Random.create().nextInt(GameRenderer.SHADER_COUNT)]
        LOGGER.info("Loading shader " + shader.path)
        (renderer as GameRendererAccessor).invokeLoadShader(shader)
    }

    fun disableShader() {
        LOGGER.info("Disabling all shaders")
        //        renderer.disableShader();
    }
}