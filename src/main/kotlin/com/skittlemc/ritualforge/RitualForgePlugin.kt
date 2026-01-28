package com.skittlemc.ritualforge

import com.skittlemc.ritualforge.api.RitualForgeAPI
import com.skittlemc.ritualforge.boss.BossService
import com.skittlemc.ritualforge.content.ContentManager
import com.skittlemc.ritualforge.persistence.DataStore
import com.skittlemc.ritualforge.ritual.RitualService
import org.bukkit.plugin.java.JavaPlugin

class RitualForgePlugin : JavaPlugin() {

    lateinit var contentManager: ContentManager private set
    lateinit var bossService: BossService private set
    lateinit var ritualService: RitualService private set
    lateinit var dataStore: DataStore private set
    lateinit var api: RitualForgeAPI private set

    override fun onEnable() {
        saveDefaultConfig()

        dataStore = DataStore(this)
        contentManager = ContentManager(this)
        bossService = BossService(this)
        ritualService = RitualService(this)
        api = RitualForgeAPI(this)

        contentManager.loadAll()
        dataStore.load()

        logger.info("RitualForge enabled.")
    }

    override fun onDisable() {
        bossService.shutdown()
        dataStore.save()
        logger.info("RitualForge disabled.")
    }
}
