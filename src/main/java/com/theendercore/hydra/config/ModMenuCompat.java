package com.theendercore.hydra.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import java.util.Locale;

import static com.theendercore.hydra.HydraMod.MODID;

@Environment(EnvType.CLIENT)
public class ModMenuCompat implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (ConfigScreenFactory<Screen>) screen -> {
            ConfigBuilder builder = ConfigBuilder.create();
            builder.setTitle(Text.translatable("config."+MODID+".title"));
            builder.setSavingRunnable(() -> ModConfig.getConfig().save());

            ConfigEntryBuilder entryBuilder = ConfigEntryBuilder.create();
            ModConfig config = ModConfig.getConfig();

            ConfigCategory customizationCategory = builder.getOrCreateCategory(Text.translatable("config."+MODID+".category.customization"));

            customizationCategory.addEntry(entryBuilder
                    .startStrField(Text.translatable("config."+MODID+".customization.prefix"), config.getPrefix())
                    .setSaveConsumer((config::setPrefix))
                    .setTooltip(Text.translatable("config."+MODID+".customization.prefix.tooltip"))
                    .setDefaultValue(ModConfig.DEFAULT_PREFIX)
                    .build());
            customizationCategory.addEntry(entryBuilder
                    .startEnumSelector(Text.translatable("config."+MODID+".customization.channel_chat_color"), Formatting.class, config.getChannelChatColor())
                    .setEnumNameProvider((value) -> Text.translatable("config."+MODID+".customization.channel_chat_color." + value.name().toLowerCase(Locale.ROOT)))
                    .setSaveConsumer(config::setDefaultChannelChatColor)
                    .setTooltip(Text.translatable("config."+MODID+".customization.channel_chat_color.tooltip"))
                    .setDefaultValue(ModConfig.DEFAULT_CHANNEL_CHAT_COLOR)
                    .build());
            customizationCategory.addEntry(entryBuilder
                    .startStrField(Text.translatable("config."+MODID+".customization.time_formatting"), config.getTimeFormatting())
                    .setSaveConsumer((config::setTimeFormatting))
                    .setTooltip(Text.translatable("config."+MODID+".customization.time_formatting.tooltip"))
                    .setDefaultValue(ModConfig.DEFAULT_TIME_FORMATTING)
                    .build());

            ConfigCategory credentialsCategory = builder.getOrCreateCategory(Text.translatable("config."+MODID+".category.credentials"));

            credentialsCategory.addEntry(entryBuilder
                    .startStrField(Text.translatable("config."+MODID+".credentials.username"), config.getUsername())
                    .setSaveConsumer((config::setUsername))
                    .setTooltip(Text.translatable("config."+MODID+".credentials.username.tooltip"))
                    .setDefaultValue(ModConfig.DEFAULT_USERNAME)
                    .build());
            credentialsCategory.addEntry(entryBuilder
                    .startStrField(Text.translatable("config."+MODID+".credentials.oauthKey"), config.getOauthKey())
                    .setSaveConsumer((config::setOauthKey))
                    .setTooltip(Text.translatable("config."+MODID+".credentials.oauthKey.tooltip"))
                    .setDefaultValue(ModConfig.DEFAULT_OAUTH_KEY)
                    .build());
            credentialsCategory.addEntry(entryBuilder
                    .startBooleanToggle(Text.translatable("config."+MODID+".credentials.extras"), config.getExtras())
                    .setSaveConsumer((config::setExtras))
                    .setTooltip(Text.translatable("config."+MODID+".credentials.extras.tooltip"))
                    .setDefaultValue(ModConfig.DEFAULT_EXTRAS)
                    .build());
            credentialsCategory.addEntry(entryBuilder
                    .startStrField(Text.translatable("config."+MODID+".credentials.channelID"), config.getChannelID())
                    .setSaveConsumer((config::setChannelID))
                    .setTooltip(Text.translatable("config."+MODID+".credentials.channelID.tooltip"))
                    .setDefaultValue(ModConfig.DEFAULT_CHANNEL_ID)
                    .build());
            credentialsCategory.addEntry(entryBuilder
                    .startTextDescription(Text.translatable("config."+MODID+".credentials.info"))
                    .build()
            );
            return builder.build();
        };
    }
}

