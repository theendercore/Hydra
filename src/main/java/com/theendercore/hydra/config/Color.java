package com.theendercore.hydra.config;

import net.minecraft.util.Formatting;
import net.minecraft.util.StringIdentifiable;

public enum Color implements StringIdentifiable {
    BLACK("BLACK"),
    DARK_BLUE("DARK_BLUE"),
    DARK_GREEN("DARK_GREEN"),
    DARK_AQUA("DARK_AQUA"),
    DARK_RED("DARK_RED"),
    DARK_PURPLE("DARK_PURPLE"),
    GOLD("GOLD"),
    GRAY("GRAY"),
    DARK_GRAY("DARK_GRAY"),
    BLUE("BLUE"),
    GREEN("GREEN"),
    AQUA("AQUA"),
    RED("RED"),
    LIGHT_PURPLE("LIGHT_PURPLE"),
    YELLOW("YELLOW"),
    WHITE("WHITE");
    private final String name;
    Color(String name) {
        this.name = name;
    }

    @Override
    public String asString() {
        return null;
    }
    public String getName(){
        return this.name;
    }
     public Formatting getFormat() {
        return Formatting.valueOf(this.getName());
    }
}