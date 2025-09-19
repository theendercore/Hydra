@file:Suppress("unused")

package com.theendercore.hydra.client.util

import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

fun darkGrayText(key: String): MutableText = Text.translatable(key).formatted(Formatting.DARK_GRAY)
fun darkGrayText(key: String, vararg args: String): MutableText = Text.translatable(key, *args).formatted(Formatting.DARK_GRAY)


fun grayText(key: String): MutableText = Text.translatable(key).formatted(Formatting.GRAY)
fun grayText(key: String, vararg args: String): MutableText = Text.translatable(key, *args).formatted(Formatting.GRAY)
fun redText(key: String): MutableText = Text.translatable(key).formatted(Formatting.RED)
fun redText(key: String, vararg args: String): MutableText = Text.translatable(key, *args).formatted(Formatting.RED)