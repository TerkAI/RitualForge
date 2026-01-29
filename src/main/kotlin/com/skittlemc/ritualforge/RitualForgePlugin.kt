package com.skittlemc.ritualforge

import com.skittlemc.ritualforge.api.RitualForgeAPI
import com.skittlemc.ritualforge.boss.BossService
import com.skittlemc.ritualforge.commands.RitualForgeCommand
import com.skittlemc.ritualforge.content.ContentManager
import com.skittlemc.ritualforge.gui.GuiListener
import com.skittlemc.ritualforge.gui.GuiManager
import com.skittlemc.ritualforge.persistence.DataStore
import com.skittlemc.ritualforge.ritual.RitualListener
import com.skittlemc.ritualforge.ritual.RitualService
import org.bukkit.plugin.java.JavaPlugin

class RitualForgePlugin : JavaPlugin() {

    lateinit var contentManager: ContentManager private set
    lateinit var bossService: BossService private set
    lateinit var ritualService: RitualService private set
    lateinit var dataStore: DataStore private set
    lateinit var guiManager: GuiManager private set
    lateinit var api: RitualForgeAPI private set

    override fun onEnable() {
        saveDefaultConfig()
        saveDefaultBosses()

        dataStore = DataStore(this)
        contentManager = ContentManager(this)
        bossService = BossService(this)
        ritualService = RitualService(this)
        guiManager = GuiManager(this)
        api = RitualForgeAPI(this)

        contentManager.loadAll()
        dataStore.load()
        bossService.start()

        // Register listeners
        server.pluginManager.registerEvents(GuiListener(this), this)
        server.pluginManager.registerEvents(RitualListener(this), this)

        // Register commands
        getCommand("ritualforge")?.let { cmd ->
            val executor = RitualForgeCommand(this)
            cmd.setExecutor(executor)
            cmd.tabCompleter = executor
        }

        logger.info("RitualForge enabled.")
    }

    override fun onDisable() {
        if (::bossService.isInitialized) {
            bossService.shutdown()
        }
        if (::dataStore.isInitialized) {
            dataStore.save()
        }
        logger.info("RitualForge disabled.")
    }

    private fun saveDefaultBosses() {
        val bossesFolder = java.io.File(dataFolder, "bosses")
        if (!bossesFolder.exists()) {
            bossesFolder.mkdirs()
            // Save default boss configuration
            saveResource("bosses/poopy_guardian.yml", false)
        }
    }
}
