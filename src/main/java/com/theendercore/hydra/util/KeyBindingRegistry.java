package com.theendercore.hydra.util;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.random.Random;
import org.lwjgl.glfw.GLFW;

import static com.theendercore.hydra.HydraMod.*;

public class KeyBindingRegistry {
    public static Random random = Random.create();
    private static KeyBinding helperKey1;
    private static KeyBinding helperKey2;

    public static void init() {
        helperKey1 = KeyBindingHelper.registerKeyBinding(new KeyBinding("key." + MODID + ".m4", InputUtil.Type.MOUSE, GLFW.GLFW_MOUSE_BUTTON_4, "keybinding.category." + MODID));
        helperKey2 = KeyBindingHelper.registerKeyBinding(new KeyBinding("key." + MODID + ".m5", InputUtil.Type.MOUSE, GLFW.GLFW_MOUSE_BUTTON_5, "keybinding.category." + MODID));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (helperKey1.wasPressed()) {
                MinecraftClient mcClient = MinecraftClient.getInstance();
                PlayerEntity playerEntity = mcClient.player;
                assert playerEntity != null;
                ParticleS2CPacket packet = new ParticleS2CPacket(ParticleTypes.TOTEM_OF_UNDYING, true, playerEntity.getX(), 0.0, playerEntity.getZ(), 0f, 0f, 0f, 1f, 100);
                for (int i = 0; i < packet.getCount(); ++i) {
                    double g = random.nextGaussian() * (double) packet.getOffsetX();
                    double h = random.nextGaussian() * 0d;
                    double j = random.nextGaussian() * (double) packet.getOffsetZ();
                    double k = random.nextGaussian() * (double) packet.getSpeed();
                    double l = random.nextGaussian() * (double) packet.getSpeed();
                    double m = random.nextGaussian() * (double) packet.getSpeed();

                    try {
                        mcClient.particleManager.addParticle(ParticleTypes.TOTEM_OF_UNDYING, packet.getX() + g, playerEntity.getY()+2 + h, packet.getZ() + j, k, l, m);
                        Methods.setRandomShader();
                    } catch (Throwable var16) {
                        LOGGER.warn("Could not spawn particle effect {}", packet.getParameters());
                        return;
                    }
                }
//                mcClient.particleManager.addParticle(ParticleTypes.BUBBLE, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), 0.0, 0.0, 0.0);

//                Random rand = new Random();
//                PlayerEntity player = client.player;
//                double z, x;
//                for (int i = 0; i <= 10; i++) {
//                    x = rand.nextDouble();
//                    z = rand.nextDouble();
//                    LOGGER.info("x:" + x + "\ny:" + z);
//                    assert player != null;
//                    mcClient.particleManager.addParticle(new SculkChargeParticleEffect(10), player.getX() - .5 + x, player.getEyeY() - .1, player.getZ() - .5 + z, 0, 0, 0);
//                    mcClient.particleManager.addParticle(new DustParticleEffect(new Vec3f(1, 1,1 ), 4), player.getX(), player.getEyeY(), player.getZ(), 0, 0, 0);
//                }
            }
            while (helperKey2.isPressed()){
                Methods.disableShader();
                LOGGER.info("hi");
            }
        });

    }
}
