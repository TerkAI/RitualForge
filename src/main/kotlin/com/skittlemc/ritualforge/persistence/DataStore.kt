package com.skittlemc.ritualforge.persistence

import com.skittlemc.ritualforge.RitualForgePlugin
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class DataStore(private val plugin: RitualForgePlugin) {

    private val file = File(plugin.dataFolder, "data.yml")
    private var config = YamlConfiguration()

    val shrineData = ShrineData(this)
    val bossStatsData = BossStatsData(this)

    fun load() {
        if (file.exists()) {
            config = YamlConfiguration.loadConfiguration(file)
        }
        shrineData.load(config)
        bossStatsData.load(config)
    }

    fun save() {
        shrineData.save(config)
        bossStatsData.save(config)
        config.save(file)
    }

    internal fun getConfig(): YamlConfiguration = config
}
