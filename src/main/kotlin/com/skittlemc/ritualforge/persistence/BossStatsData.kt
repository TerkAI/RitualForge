package com.skittlemc.ritualforge.persistence

import org.bukkit.configuration.file.YamlConfiguration

class BossStatsData(private val store: DataStore) {

    data class BossStats(
        val bossId: String,
        var timesSpawned: Int = 0,
        var timesKilled: Int = 0,
        var fastestKillTicks: Int = Int.MAX_VALUE
    )

    private val stats = mutableMapOf<String, BossStats>()

    fun load(config: YamlConfiguration) {
        stats.clear()
        val section = config.getConfigurationSection("boss_stats") ?: return
        for (key in section.getKeys(false)) {
            val s = section.getConfigurationSection(key) ?: continue
            stats[key] = BossStats(
                bossId = key,
                timesSpawned = s.getInt("times_spawned", 0),
                timesKilled = s.getInt("times_killed", 0),
                fastestKillTicks = s.getInt("fastest_kill_ticks", Int.MAX_VALUE)
            )
        }
    }

    fun save(config: YamlConfiguration) {
        config.set("boss_stats", null)
        for ((id, stat) in stats) {
            config.set("boss_stats.$id.times_spawned", stat.timesSpawned)
            config.set("boss_stats.$id.times_killed", stat.timesKilled)
            if (stat.fastestKillTicks < Int.MAX_VALUE) {
                config.set("boss_stats.$id.fastest_kill_ticks", stat.fastestKillTicks)
            }
        }
    }

    fun recordSpawn(bossId: String) {
        getOrCreate(bossId).timesSpawned++
    }

    fun recordKill(bossId: String, durationTicks: Int) {
        val s = getOrCreate(bossId)
        s.timesKilled++
        if (durationTicks < s.fastestKillTicks) {
            s.fastestKillTicks = durationTicks
        }
    }

    fun get(bossId: String): BossStats? = stats[bossId]

    private fun getOrCreate(bossId: String): BossStats {
        return stats.getOrPut(bossId) { BossStats(bossId) }
    }
}
