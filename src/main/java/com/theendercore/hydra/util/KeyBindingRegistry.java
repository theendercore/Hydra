package com.theendercore.hydra.util;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.SculkChargeParticleEffect;
import net.minecraft.util.math.Vec3f;
import org.lwjgl.glfw.GLFW;

import java.util.Random;

import static com.theendercore.hydra.HydraMod.*;

public class KeyBindingRegistry {
    private static KeyBinding keyBinding;

    public static void init() {
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("key." + MODID + ".test", InputUtil.Type.MOUSE, GLFW.GLFW_MOUSE_BUTTON_4, "keybinding.category." + MODID));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {
                MinecraftClient mcClient = MinecraftClient.getInstance();
                Random rand = new Random();
                PlayerEntity player = client.player;
                double z, x;
                for (int i = 0; i <= 10; i++) {
                    x = rand.nextDouble();
                    z = rand.nextDouble();
                    LOGGER.info("x:" + x + "\ny:" + z);
                    assert player != null;
                    mcClient.particleManager.addParticle(new SculkChargeParticleEffect(10), player.getX() - .5 + x, player.getEyeY() - .1, player.getZ() - .5 + z, 0, 0, 0);
                    mcClient.particleManager.addParticle(new DustParticleEffect(new Vec3f(1, 1,1 ), 4), player.getX(), player.getEyeY(), player.getZ(), 0, 0, 0);
                }
            }
        });
    }
}
