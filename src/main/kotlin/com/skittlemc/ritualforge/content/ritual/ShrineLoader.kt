package com.skittlemc.ritualforge.content.ritual

import com.skittlemc.ritualforge.RitualForgePlugin
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class ShrineLoader(private val plugin: RitualForgePlugin) {

    fun load(): List<ShrineDefinition> {
        val folder = File(plugin.dataFolder, "shrines")
        if (!folder.exists()) {
            folder.mkdirs()
            return emptyList()
        }

        return folder.listFiles { f -> f.extension == "yml" }?.mapNotNull { file ->
            runCatching { parse(file) }.onFailure {
                plugin.logger.warning("Failed to load shrine from ${file.name}: ${it.message}")
            }.getOrNull()
        } ?: emptyList()
    }

    private fun parse(file: File): ShrineDefinition {
        val cfg = YamlConfiguration.loadConfiguration(file)
        val id = file.nameWithoutExtension

        val coreBlockName = cfg.getString("core_block") ?: "LODESTONE"
        val coreBlock = Material.matchMaterial(coreBlockName) ?: Material.LODESTONE

        val multiblock = cfg.getMapList("multiblock").map { map ->
            BlockOffset(
                dx = (map["dx"] as? Number)?.toInt() ?: 0,
                dy = (map["dy"] as? Number)?.toInt() ?: 0,
                dz = (map["dz"] as? Number)?.toInt() ?: 0,
                material = Material.matchMaterial(map["material"] as? String ?: "STONE") ?: Material.STONE
            )
        }.takeIf { it.isNotEmpty() }

        return ShrineDefinition(
            id = id,
            coreBlock = coreBlock,
            multiblock = multiblock
        )
    }
}
