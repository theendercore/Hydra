package com.theendercore.hydra.client.init

import com.theendercore.hydra.client.HydraMod
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper.registerKeyBinding
import net.minecraft.client.option.KeyBind
import org.lwjgl.glfw.GLFW

object HydraKeys {
    fun init() = Unit

    @Suppress("unused")
    val testKey: KeyBind = registerKeyBinding(
        KeyBind(
            "key.${HydraMod.MODID}.test_key", GLFW.GLFW_KEY_UNKNOWN, "keybinding.category.${HydraMod.MODID}"
        )
    )
}