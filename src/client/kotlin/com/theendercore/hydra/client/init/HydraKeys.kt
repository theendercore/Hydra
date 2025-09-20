package com.theendercore.hydra.client.init

import com.theendercore.hydra.client.HydraMod
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper.registerKeyBinding
import net.minecraft.client.KeyMapping
import org.lwjgl.glfw.GLFW

object HydraKeys {
    fun init() = Unit

    @Suppress("unused")
    val testKey: KeyMapping = registerKeyBinding(
        KeyMapping(
            "key.${HydraMod.MODID}.test_key", GLFW.GLFW_KEY_UNKNOWN, "keybinding.category.${HydraMod.MODID}"
        )
    )
}