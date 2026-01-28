package com.skittlemc.ritualforge.persistence

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.configuration.file.YamlConfiguration

class ShrineData(private val store: DataStore) {

    data class SavedShrine(
        val id: String,
        val world: String,
        val x: Double,
        val y: Double,
        val z: Double,
        val type: String
    ) {
        fun toLocation(): Location? {
            val w = Bukkit.getWorld(world) ?: return null
            return Location(w, x, y, z)
        }
    }

    private val shrines = mutableMapOf<String, SavedShrine>()

    fun load(config: YamlConfiguration) {
        shrines.clear()
        val section = config.getConfigurationSection("shrines") ?: return
        for (key in section.getKeys(false)) {
            val s = section.getConfigurationSection(key) ?: continue
            shrines[key] = SavedShrine(
                id = key,
                world = s.getString("world") ?: continue,
                x = s.getDouble("x"),
                y = s.getDouble("y"),
                z = s.getDouble("z"),
                type = s.getString("type") ?: "default"
            )
        }
    }

    fun save(config: YamlConfiguration) {
        config.set("shrines", null)
        for ((id, shrine) in shrines) {
            config.set("shrines.$id.world", shrine.world)
            config.set("shrines.$id.x", shrine.x)
            config.set("shrines.$id.y", shrine.y)
            config.set("shrines.$id.z", shrine.z)
            config.set("shrines.$id.type", shrine.type)
        }
    }

    fun register(shrine: SavedShrine) { shrines[shrine.id] = shrine }
    fun unregister(id: String) { shrines.remove(id) }
    fun get(id: String): SavedShrine? = shrines[id]
    fun all(): Collection<SavedShrine> = shrines.values
}
