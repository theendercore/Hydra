package com.theendercore.hydra.client.config

import net.minecraft.util.Formatting

enum class Color(val label: String) {
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

    val format = Formatting.valueOf(label)
}