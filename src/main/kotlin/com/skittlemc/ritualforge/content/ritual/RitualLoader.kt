package com.skittlemc.ritualforge.content.ritual

import com.skittlemc.ritualforge.RitualForgePlugin
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class RitualLoader(private val plugin: RitualForgePlugin) {

    fun load(): List<RitualDefinition> {
        val folder = File(plugin.dataFolder, "rituals")
        if (!folder.exists()) {
            folder.mkdirs()
            return emptyList()
        }

        return folder.listFiles { f -> f.extension == "yml" }?.mapNotNull { file ->
            runCatching { parse(file) }.onFailure {
                plugin.logger.warning("Failed to load ritual from ${file.name}: ${it.message}")
            }.getOrNull()
        } ?: emptyList()
    }

    private fun parse(file: File): RitualDefinition {
        val cfg = YamlConfiguration.loadConfiguration(file)
        val id = file.nameWithoutExtension

        return RitualDefinition(
            id = id,
            displayName = cfg.getString("display_name") ?: id,
            bossId = cfg.getString("boss") ?: error("Ritual '$id' missing 'boss' field"),
            shrineType = cfg.getString("shrine_type") ?: "default",
            catalystItem = cfg.getString("catalyst") ?: error("Ritual '$id' missing 'catalyst' field"),
            channelingTicks = cfg.getInt("channeling_ticks", 60),
            cooldownTicks = cfg.getInt("cooldown_ticks", 6000)
        )
    }
}
