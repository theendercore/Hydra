package com.theendercore.hydra.client.config

import com.theendercore.hydra.client.HydraMod.MODID
import com.theendercore.hydra.client.HydraMod.id
import me.fzzyhmstrs.fzzy_config.config.Config
import me.fzzyhmstrs.fzzy_config.config.ConfigGroup
import me.fzzyhmstrs.fzzy_config.config.ConfigSection
import me.fzzyhmstrs.fzzy_config.util.Translatable.*
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedColor

@Name("Hydra - Twitch Integrations Mod")
class HydraConfig : Config(id(MODID)) {

    @JvmField
    @Desc("Prefix for turning mc chat messages in to Twitch chat")
    var prefix = "!"

    @Name("Your chat color")
    @Desc("The color your name appears in chat")
    var channelChatColor = ValidatedColor(91, 110, 225)

    @Desc("Valid placeholders : H (hours), m (minutes), s (seconds)")
    var timeFormatting = "HH:mm"

    @Desc("Automatically enables the mod as soon as you join a world or server")
    var autoStart = false

    @Name("Extra Interrogations")
    @Desc("Extra Features includes Follow, Chanel Point and Subscribe Notifications. (All of there are hardcoded and not customizable in this update)")
    var extras = false

    @JvmField
    var credentials = CredentialsSection()

    class CredentialsSection : ConfigSection() {
        @JvmField
        @Desc("Your Twitch username")
        var username = ""

        @Suppress("unused")
        @Name("Authentication")
        var authKey = ConfigGroup("auth_key_id", true)

        @ConfigGroup.Pop
        @Name("OAuth Key")
        @Desc("Your Authentication Key")
        @Prefix("If you don't know how to get the OAuth Key check the mod page on Modrinth, CurseForge or GitHub")
        var oauthKey = ""

        @Desc("Internal value please don't touch!")
        var broadcasterId = ""
    }
}