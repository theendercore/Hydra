package com.theendercore.hydra.config

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import com.theendercore.hydra.HydraMod
import me.shedaniel.clothconfig2.api.ConfigBuilder
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.text.Text

@Suppress("unused")
@Environment(EnvType.CLIENT)
class ModMenuCombat : ModMenuApi {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
        return ConfigScreenFactory {
            val builder = ConfigBuilder.create()
            builder.title = Text.translatable("config." + HydraMod.MODID + ".title")
            builder.savingRunnable = Runnable { ModConfig.config?.save() }
            val entryBuilder = ConfigEntryBuilder.create()
            val config = ModConfig.config
            val customizationCategory =
                builder.getOrCreateCategory(Text.translatable("config." + HydraMod.MODID + ".category.customization"))
            customizationCategory.addEntry(entryBuilder
                .startStrField(
                    Text.translatable("config." + HydraMod.MODID + ".customization.prefix"),
                    config!!.prefix
                )
                .setSaveConsumer { prefix: String -> config.prefix = prefix }
                .setTooltip(Text.translatable("config." + HydraMod.MODID + ".customization.prefix.tooltip"))
                .setDefaultValue(ModConfig.DEFAULT_PREFIX)
                .build())
            customizationCategory.addEntry(entryBuilder
                .startEnumSelector(
                    Text.translatable("config." + HydraMod.MODID + ".customization.channel_chat_color"),
                    Color::class.java,
                    config.channelChatColor
                )
                .setEnumNameProvider { value: Enum<*> -> Text.translatable("config." + HydraMod.MODID + ".customization.channel_chat_color." + value.name.lowercase()) }
                .setSaveConsumer { channelChatColor: Color? -> config.channelChatColor = channelChatColor }
                .setTooltip(Text.translatable("config." + HydraMod.MODID + ".customization.channel_chat_color.tooltip"))
                .setDefaultValue(ModConfig.DEFAULT_CHANNEL_CHAT_COLOR)
                .build())
            customizationCategory.addEntry(entryBuilder
                .startStrField(
                    Text.translatable("config." + HydraMod.MODID + ".customization.time_formatting"),
                    config.timeFormatting
                )
                .setSaveConsumer { timeFormatting: String -> config.timeFormatting = timeFormatting }
                .setTooltip(Text.translatable("config." + HydraMod.MODID + ".customization.time_formatting.tooltip"))
                .setDefaultValue(ModConfig.DEFAULT_TIME_FORMATTING)
                .build())
            customizationCategory.addEntry(entryBuilder
                .startBooleanToggle(
                    Text.translatable("config." + HydraMod.MODID + ".customization.auto_enable"),
                    config.autoStart
                )
                .setSaveConsumer { autoStart: Boolean -> config.autoStart = autoStart }
                .setTooltip(Text.translatable("config." + HydraMod.MODID + ".customization.auto_enable.tooltip"))
                .setDefaultValue(ModConfig.DEFAULT_AUTO_START)
                .build())
            val credentialsCategory =
                builder.getOrCreateCategory(Text.translatable("config." + HydraMod.MODID + ".category.credentials"))
            credentialsCategory.addEntry(entryBuilder
                .startStrField(
                    Text.translatable("config." + HydraMod.MODID + ".credentials.username"),
                    config.username
                )
                .setSaveConsumer { username: String -> config.username = username }
                .setTooltip(Text.translatable("config." + HydraMod.MODID + ".credentials.username.tooltip"))
                .setDefaultValue(ModConfig.DEFAULT_USERNAME)
                .build())
            credentialsCategory.addEntry(entryBuilder
                .startStrField(
                    Text.translatable("config." + HydraMod.MODID + ".credentials.oauthKey"),
                    config.oauthKey
                )
                .setSaveConsumer { oauthKey: String -> config.oauthKey = oauthKey }
                .setTooltip(Text.translatable("config." + HydraMod.MODID + ".credentials.oauthKey.tooltip"))
                .setDefaultValue(ModConfig.DEFAULT_OAUTH_KEY)
                .build())
            credentialsCategory.addEntry(entryBuilder
                .startBooleanToggle(
                    Text.translatable("config." + HydraMod.MODID + ".credentials.extras"),
                    config.extras
                )
                .setSaveConsumer { extras: Boolean -> config.extras = extras }
                .setTooltip(Text.translatable("config." + HydraMod.MODID + ".credentials.extras.tooltip"))
                .setDefaultValue(ModConfig.DEFAULT_EXTRAS)
                .build())
            credentialsCategory.addEntry(
                entryBuilder
                    .startTextDescription(Text.translatable("config." + HydraMod.MODID + ".credentials.info"))
                    .build()
            )
            builder.build()
        }
    }
}