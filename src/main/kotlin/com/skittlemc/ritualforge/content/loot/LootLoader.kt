package com.skittlemc.ritualforge.content.loot

import com.skittlemc.ritualforge.RitualForgePlugin
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class LootLoader(private val plugin: RitualForgePlugin) {

    fun load(): List<LootTableDefinition> {
        val folder = File(plugin.dataFolder, "loot")
        if (!folder.exists()) {
            folder.mkdirs()
            return emptyList()
        }

        return folder.listFiles { f -> f.extension == "yml" }?.mapNotNull { file ->
            runCatching { parse(file) }.onFailure {
                plugin.logger.warning("Failed to load loot table from ${file.name}: ${it.message}")
            }.getOrNull()
        } ?: emptyList()
    }

    private fun parse(file: File): LootTableDefinition {
        val cfg = YamlConfiguration.loadConfiguration(file)
        val id = file.nameWithoutExtension

        val entries = cfg.getMapList("entries").map { map ->
            val materialName = map["material"] as? String ?: "DIAMOND"
            LootEntry(
                material = Material.matchMaterial(materialName) ?: Material.DIAMOND,
                amount = parseRange(map["amount"]),
                weight = (map["weight"] as? Number)?.toDouble() ?: 1.0,
                customModelData = (map["custom_model_data"] as? Number)?.toInt(),
                displayName = map["display_name"] as? String
            )
        }

        return LootTableDefinition(id = id, entries = entries)
    }

    private fun parseRange(raw: Any?): IntRange {
        return when (raw) {
            is Number -> raw.toInt()..raw.toInt()
            is String -> {
                val parts = raw.split("..")
                if (parts.size == 2) {
                    (parts[0].toIntOrNull() ?: 1)..(parts[1].toIntOrNull() ?: 1)
                } else 1..1
            }
            else -> 1..1
        }
    }
}
