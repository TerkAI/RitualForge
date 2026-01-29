package com.skittlemc.ritualforge.util

import org.bukkit.NamespacedKey
import org.bukkit.plugin.java.JavaPlugin

object Keys {

    private val plugin: JavaPlugin by lazy {
        JavaPlugin.getProvidingPlugin(Keys::class.java)
    }

    val CATALYST_ID = NamespacedKey("ritualforge", "catalyst_id")
    val BOSS_ID = NamespacedKey("ritualforge", "boss_id")
    val BOSS_INSTANCE = NamespacedKey("ritualforge", "boss_instance")
    val SHRINE_ID = NamespacedKey("ritualforge", "shrine_id")
}
