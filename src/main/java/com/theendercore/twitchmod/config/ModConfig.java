package com.theendercore.twitchmod.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

public class ModConfig {

    public static final String DEFAULT_CHANNEL = "";
    public static final String DEFAULT_USERNAME = "";
    public static final String DEFAULT_OAUTH_KEY = "";

    private static ModConfig SINGLE_INSTANCE = null;
    private final File configFile;

    private String channel;
    private String username;
    private String oauthKey;

    public ModConfig() {
        this.configFile = FabricLoader.getInstance().getConfigDir().resolve("twitchchat.json").toFile();
        this.channel = DEFAULT_CHANNEL;
        this.username = DEFAULT_USERNAME;
        this.oauthKey = DEFAULT_OAUTH_KEY;
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
                this.channel = jsonObject.has("channel") ? jsonObject.getAsJsonPrimitive("channel").getAsString() : DEFAULT_CHANNEL;
                this.username = jsonObject.has("username") ? jsonObject.getAsJsonPrimitive("username").getAsString() : DEFAULT_USERNAME;
                this.oauthKey = jsonObject.has("oauthKey") ? jsonObject.getAsJsonPrimitive("oauthKey").getAsString() : DEFAULT_OAUTH_KEY;
            }
        } catch (IOException e) {
            // Do nothing, we have no file and thus we have to keep everything as default
        }
    }

    public void save() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("channel", this.channel);
        jsonObject.addProperty("username", this.username);
        jsonObject.addProperty("oauthKey", this.oauthKey);

        try (PrintWriter out = new PrintWriter(configFile)) {
            out.println(jsonObject);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
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
}