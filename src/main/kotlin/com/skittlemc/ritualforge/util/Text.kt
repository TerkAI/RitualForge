package com.skittlemc.ritualforge.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage

object Text {

    private val miniMessage = MiniMessage.miniMessage()

    fun parse(input: String): Component {
        return miniMessage.deserialize(input)
    }

    fun parse(input: String, vararg placeholders: Pair<String, String>): Component {
        var resolved = input
        for ((key, value) in placeholders) {
            resolved = resolved.replace("<$key>", value)
        }
        return miniMessage.deserialize(resolved)
    }

    fun plain(text: String): Component {
        return Component.text(text)
    }
}
