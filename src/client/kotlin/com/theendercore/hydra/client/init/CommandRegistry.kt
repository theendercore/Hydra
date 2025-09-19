package com.theendercore.hydra.client.init

import com.theendercore.hydra.client.commands.HydraCommand
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback

object CommandRegistry {
    fun init() {
        ClientCommandRegistrationCallback.EVENT.register(ClientCommandRegistrationCallback { dispatcher, _ ->
          HydraCommand.register(dispatcher)
        })
    }
}