package com.theendercore.hydra.config

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.theendercore.hydra.HydraMod.Companion.MODID
import net.fabricmc.loader.api.FabricLoader
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.PrintWriter
import java.nio.file.Files

class ModConfig {
    private val configFile: File = FabricLoader.getInstance().configDir.resolve("$MODID.json").toFile()
    var username: String
    var oauthKey: String
    var prefix: String
    var channelChatColor: Color?
    var timeFormatting: String
    var extras: Boolean
    var autoStart: Boolean
    var enableCache: Boolean

    init {
        username = DEFAULT_USERNAME
        oauthKey = DEFAULT_OAUTH_KEY
        prefix = DEFAULT_PREFIX
        channelChatColor = DEFAULT_CHANNEL_CHAT_COLOR
        timeFormatting = DEFAULT_TIME_FORMATTING
        extras = DEFAULT_EXTRAS
        autoStart = DEFAULT_AUTO_START
        enableCache = DEFAULT_ENABLE_CACHE
    }

    fun load() {
        try {
            val jsonStr = String(Files.readAllBytes(configFile.toPath()))
            if (jsonStr != "") {
                val jsonObject = JsonParser.parseString(jsonStr) as JsonObject
                username =
                    if (jsonObject.has("username")) jsonObject.getAsJsonPrimitive("username").asString else DEFAULT_USERNAME
                oauthKey =
                    if (jsonObject.has("oauthKey")) jsonObject.getAsJsonPrimitive("oauthKey").asString else DEFAULT_OAUTH_KEY
                prefix =
                    if (jsonObject.has("prefix")) jsonObject.getAsJsonPrimitive("prefix").asString else DEFAULT_PREFIX
                channelChatColor = if (jsonObject.has("channelChatColor")) Color.valueOf(
                    jsonObject.getAsJsonPrimitive("channelChatColor").asString
                ) else DEFAULT_CHANNEL_CHAT_COLOR
                timeFormatting =
                    if (jsonObject.has("timeFormatting")) jsonObject.getAsJsonPrimitive("timeFormatting").asString else DEFAULT_TIME_FORMATTING
                extras =
                    if (jsonObject.has("extras")) jsonObject.getAsJsonPrimitive("extras").asBoolean else DEFAULT_EXTRAS
                autoStart =
                    if (jsonObject.has("autoStart")) jsonObject.getAsJsonPrimitive("autoStart").asBoolean else DEFAULT_AUTO_START
                autoStart =
                    if (jsonObject.has("enableCache")) jsonObject.getAsJsonPrimitive("enableCache").asBoolean else DEFAULT_ENABLE_CACHE
            }
        } catch (e: IOException) {
            // Do nothing, we have no file,
        }
    }

    fun save() {
        val jsonObject = JsonObject()
        jsonObject.addProperty("username", username)
        jsonObject.addProperty("oauthKey", oauthKey)
        jsonObject.addProperty("prefix", prefix)
        jsonObject.addProperty("channelChatColor", channelChatColor?.getLabel())
        jsonObject.addProperty("timeFormatting", timeFormatting)
        jsonObject.addProperty("extras", extras)
        jsonObject.addProperty("autoStart", autoStart)
        jsonObject.addProperty("enableCache", enableCache)
        try {
            PrintWriter(configFile).use { out -> out.println(jsonObject) }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    companion object {
        const val DEFAULT_USERNAME = ""
        const val DEFAULT_OAUTH_KEY = ""
        const val DEFAULT_PREFIX = "!"
        val DEFAULT_CHANNEL_CHAT_COLOR = Color.BLUE
        const val DEFAULT_TIME_FORMATTING = "HH:mm"
        const val DEFAULT_EXTRAS = false
        const val DEFAULT_AUTO_START = false
        const val DEFAULT_ENABLE_CACHE = false

        private var SINGLE_INSTANCE: ModConfig? = null
        val config: ModConfig?
            get() {
                if (SINGLE_INSTANCE == null) {
                    SINGLE_INSTANCE = ModConfig()
                }
                return SINGLE_INSTANCE
            }
    }
}