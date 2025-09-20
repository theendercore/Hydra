@file:Suppress("unused")

package com.theendercore.hydra.client.util

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component.translatable
import net.minecraft.network.chat.MutableComponent

fun darkGrayText(key: String): MutableComponent = translatable(key).withStyle(ChatFormatting.DARK_GRAY)
fun darkGrayText(key: String, vararg args: String): MutableComponent = translatable(key, *args).withStyle(ChatFormatting.DARK_GRAY)

fun grayText(key: String): MutableComponent = translatable(key).withStyle(ChatFormatting.GRAY)
fun grayText(key: String, vararg args: String): MutableComponent = translatable(key, *args).withStyle(ChatFormatting.GRAY)
fun redText(key: String): MutableComponent = translatable(key).withStyle(ChatFormatting.RED)
fun redText(key: String, vararg args: String): MutableComponent = translatable(key, *args).withStyle(ChatFormatting.RED)