package com.theendercore.twitchmod.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
@Environment(EnvType.CLIENT)
public class ModMenuCompat implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (ConfigScreenFactory<Screen>) screen -> {
            ConfigBuilder builder = ConfigBuilder.create();
            builder.setTitle(Text.translatable("config.twitchmod.title"));
            builder.setSavingRunnable(() -> ModConfig.getConfig().save());

            ConfigEntryBuilder entryBuilder = ConfigEntryBuilder.create();

            ConfigCategory credentialsCategory = builder.getOrCreateCategory(Text.translatable("config.twitchmod.category.credentials"));
            credentialsCategory.addEntry(entryBuilder
                    .startStrField(Text.translatable("config.twitchmod.credentials.username"), ModConfig.getConfig().getUsername())
                    .setSaveConsumer((s -> ModConfig.getConfig().setUsername(s)))
                    .setTooltip(Text.translatable("config.twitchmod.credentials.username.tooltip"))
                    .setDefaultValue(ModConfig.DEFAULT_USERNAME)
                    .build());
            credentialsCategory.addEntry(entryBuilder
                    .startStrField(Text.translatable("config.twitchmod.credentials.oauthKey"), ModConfig.getConfig().getOauthKey())
                    .setSaveConsumer((s -> ModConfig.getConfig().setOauthKey(s)))
                    .setTooltip(Text.translatable("config.twitchmod.credentials.oauthKey.tooltip"))
                    .setDefaultValue(ModConfig.DEFAULT_OAUTH_KEY)
                    .build());

            return builder.build();
        };
    }
}

