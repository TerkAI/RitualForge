package com.skittlemc.ritualforge.content.boss

import com.skittlemc.ritualforge.RitualForgePlugin
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.EntityType
import java.io.File

class BossLoader(private val plugin: RitualForgePlugin) {

    fun load(): List<BossDefinition> {
        val folder = File(plugin.dataFolder, "bosses")
        if (!folder.exists()) {
            folder.mkdirs()
            return emptyList()
        }

        return folder.listFiles { f -> f.extension == "yml" }?.mapNotNull { file ->
            runCatching { parse(file) }.onFailure {
                plugin.logger.warning("Failed to load boss from ${file.name}: ${it.message}")
            }.getOrNull()
        } ?: emptyList()
    }

    private fun parse(file: File): BossDefinition {
        val cfg = YamlConfiguration.loadConfiguration(file)
        val id = file.nameWithoutExtension

        val phases = cfg.getMapList("phases").mapIndexed { index, map ->
            PhaseDefinition(
                index = index,
                healthThreshold = (map["health_threshold"] as? Number)?.toDouble() ?: 1.0,
                abilities = (map["abilities"] as? List<*>)?.filterIsInstance<String>() ?: emptyList(),
                animation = map["animation"] as? String
            )
        }

        val entityTypeName = cfg.getString("entity_type") ?: "ZOMBIE"
        val entityType = runCatching { EntityType.valueOf(entityTypeName.uppercase()) }
            .getOrDefault(EntityType.ZOMBIE)

        val equipment = parseEquipment(cfg.getConfigurationSection("equipment"))

        return BossDefinition(
            id = id,
            displayName = cfg.getString("display_name") ?: id,
            description = cfg.getString("description") ?: "A fearsome boss.",
            maxHealth = cfg.getDouble("max_health", 500.0),
            entityType = entityType,
            equipment = equipment,
            glowing = cfg.getBoolean("glowing", false),
            scale = cfg.getDouble("scale", 1.0),
            modelId = cfg.getString("model"),
            phases = phases,
            lootTableId = cfg.getString("loot_table"),
            arenaRadius = cfg.getDouble("arena_radius", 30.0)
        )
    }

    private fun parseEquipment(section: ConfigurationSection?): BossEquipment {
        if (section == null) return BossEquipment()

        return BossEquipment(
            mainHand = parseMaterial(section.getString("main_hand")),
            offHand = parseMaterial(section.getString("off_hand")),
            helmet = parseMaterial(section.getString("helmet")),
            chestplate = parseMaterial(section.getString("chestplate")),
            leggings = parseMaterial(section.getString("leggings")),
            boots = parseMaterial(section.getString("boots"))
        )
    }

    private fun parseMaterial(name: String?): Material? {
        if (name == null) return null
        return Material.matchMaterial(name)
    }
}
