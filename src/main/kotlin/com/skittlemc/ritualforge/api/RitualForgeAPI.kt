package com.skittlemc.ritualforge.api

import com.skittlemc.ritualforge.RitualForgePlugin
import com.skittlemc.ritualforge.boss.BossInstance
import com.skittlemc.ritualforge.content.boss.BossDefinition
import org.bukkit.Location
import java.util.UUID

/**
 * Public-facing API for other plugins to interact with RitualForge.
 */
class RitualForgeAPI(private val plugin: RitualForgePlugin) {

    fun spawnBoss(definitionId: String, location: Location): BossInstance? {
        val definition = plugin.contentManager.getBossDefinition(definitionId) ?: return null
        return plugin.bossService.spawn(definition, location)
    }

    fun despawnBoss(instanceId: UUID) {
        plugin.bossService.despawn(instanceId)
    }

    fun getActiveBosses(): Collection<BossInstance> = plugin.bossService.activeBosses()

    fun getBossDefinition(id: String): BossDefinition? = plugin.contentManager.getBossDefinition(id)
}
