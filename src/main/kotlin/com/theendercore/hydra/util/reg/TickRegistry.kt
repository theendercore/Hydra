package com.theendercore.hydra.util.reg

import com.theendercore.hydra.util.Methods
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents

object TickRegistry {

    var timeRemainingInTicks = 0

    fun init() {
        ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick register@{

            if (timeRemainingInTicks > 0
            ) {
                timeRemainingInTicks--
            } else {
                Methods.disableShader()

            }
        })
    }
}