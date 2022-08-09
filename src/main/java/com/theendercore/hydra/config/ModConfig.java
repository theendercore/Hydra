package com.theendercore.hydra.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Formatting;
import net.minecraft.util.StringIdentifiable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Locale;

import static com.theendercore.hydra.HydraMod.MODID;

public class ModConfig {

    public static final String DEFAULT_USERNAME = "";
    public static final String DEFAULT_OAUTH_KEY = "";
    public static final String DEFAULT_CHANNEL_ID = "";
    public static final String DEFAULT_PREFIX = "!";
    public static final Color DEFAULT_CHANNEL_CHAT_COLOR = Color.BLUE;
    public static final String DEFAULT_TIME_FORMATTING = "HH:mm";
    public static final boolean DEFAULT_EXTRAS = false;

    private static ModConfig SINGLE_INSTANCE = null;

    private final File configFile;

    private String username;
    private String oauthKey;
    private String channelID;
    private String prefix;
    private Color channelChatColor;
    private String timeFormatting;
    private boolean extras;

    public ModConfig() {
        this.configFile = FabricLoader.getInstance().getConfigDir().resolve(MODID + ".json").toFile();
        this.username = DEFAULT_USERNAME;
        this.oauthKey = DEFAULT_OAUTH_KEY;
        this.channelID = DEFAULT_CHANNEL_ID;
        this.prefix = DEFAULT_PREFIX;
        this.channelChatColor = DEFAULT_CHANNEL_CHAT_COLOR;
        this.timeFormatting = DEFAULT_TIME_FORMATTING;
        this.extras = DEFAULT_EXTRAS;
    }

    public static ModConfig getConfig() {
        if (SINGLE_INSTANCE == null) {
            SINGLE_INSTANCE = new ModConfig();
        }

        return SINGLE_INSTANCE;
    }

    public void load() {
        try {
            String jsonStr = new String(Files.readAllBytes(this.configFile.toPath()));
            if (!jsonStr.equals("")) {
                JsonObject jsonObject = (JsonObject) JsonParser.parseString(jsonStr);
                this.username = jsonObject.has("username") ? jsonObject.getAsJsonPrimitive("username").getAsString() : DEFAULT_USERNAME;
                this.oauthKey = jsonObject.has("oauthKey") ? jsonObject.getAsJsonPrimitive("oauthKey").getAsString() : DEFAULT_OAUTH_KEY;
                this.channelID = jsonObject.has("channelID") ? jsonObject.getAsJsonPrimitive("channelID").getAsString() : DEFAULT_CHANNEL_ID;
                this.prefix = jsonObject.has("prefix") ? jsonObject.getAsJsonPrimitive("prefix").getAsString() : DEFAULT_PREFIX;
                this.channelChatColor = jsonObject.has("channelChatColor") ? Color.valueOf((jsonObject.getAsJsonPrimitive("channelChatColor").getAsString())) : DEFAULT_CHANNEL_CHAT_COLOR;
                this.timeFormatting = jsonObject.has("timeFormatting") ? jsonObject.getAsJsonPrimitive("timeFormatting").getAsString() : DEFAULT_TIME_FORMATTING;
                this.extras = jsonObject.has("extras") ? jsonObject.getAsJsonPrimitive("extras").getAsBoolean() : DEFAULT_EXTRAS;
            }
        } catch (IOException e) {
            // Do nothing, we have no file and thus we have to keep everything as default
        }
    }

    public void save() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", this.username);
        jsonObject.addProperty("oauthKey", this.oauthKey);
        jsonObject.addProperty("channelID", this.channelID);
        jsonObject.addProperty("prefix", this.prefix);
        jsonObject.addProperty("channelChatColor", this.channelChatColor.getName());
        jsonObject.addProperty("timeFormatting", this.timeFormatting);
        jsonObject.addProperty("extras", this.extras);

        try (PrintWriter out = new PrintWriter(configFile)) {
            out.println(jsonObject);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOauthKey() {
        return oauthKey;
    }

    public void setOauthKey(String oauthKey) {
        this.oauthKey = oauthKey;
    }

    public String getChannelID() {
        return channelID;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Color getChannelChatColor() { return channelChatColor; }

    public void setDefaultChannelChatColor(Color channelChatColor) {
        this.channelChatColor = channelChatColor;
    }

    public String getTimeFormatting() {
        return timeFormatting;
    }

    public void setTimeFormatting(String timeFormatting) {
        this.timeFormatting = timeFormatting;
    }


    public boolean getExtras() {
        return extras;
    }

    public void setExtras(boolean extras) {
        this.extras = extras;
    }


}