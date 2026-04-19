package com.theendercore.hydra.client.init

import com.theendercore.hydra.client.HydraMod
import com.theendercore.hydra.client.HydraMod.id
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper.registerKeyBinding
import net.minecraft.client.KeyMapping
import net.minecraft.client.KeyMapping.Category
import org.lwjgl.glfw.GLFW

object HydraKeys {
    fun init() = Unit

    var category = Category.register(id(HydraMod.MODID))

    @Suppress("unused")
    val testKey: KeyMapping = registerKeyBinding(
        KeyMapping("key.${HydraMod.MODID}.test_key", GLFW.GLFW_KEY_UNKNOWN, category)
    )
}