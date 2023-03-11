package com.theendercore.hydra.mixin;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;


@Mixin(GameRenderer.class)
public interface GameRendererAccessor {

    @Accessor("SHADERS_LOCATIONS")
    static Identifier[] getShaderLocations() {
        throw new AssertionError();
    }

    @Invoker("loadShader")
    void invokeLoadShader(Identifier id);
}

