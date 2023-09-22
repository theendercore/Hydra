package com.theendercore.hydra.config

import net.minecraft.util.Formatting

enum class Color(private val label: String) {
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


    fun getLabel(): String {
        return this.label
    }

    val format: Formatting
        get() = Formatting.valueOf(this.label)
}