package com.skittlemc.ritualforge.ritual

import com.skittlemc.ritualforge.RitualForgePlugin
import com.skittlemc.ritualforge.api.events.RitualActivateEvent
import com.skittlemc.ritualforge.ritual.shrine.ShrineDetector
import com.skittlemc.ritualforge.ritual.shrine.ActivationFlow
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player

class RitualService(private val plugin: RitualForgePlugin) {

    val shrineDetector = ShrineDetector()
    private val activationFlow = ActivationFlow(plugin)

    fun tryActivate(player: Player, location: Location): Boolean {
        val shrine = shrineDetector.detect(location) ?: return false

        val ritualDef = plugin.contentManager.getAllRitualDefinitions()
            .firstOrNull { it.shrineId == shrine.type }
            ?: return false

        val event = RitualActivateEvent(player, ritualDef.id, location)
        Bukkit.getPluginManager().callEvent(event)
        if (event.isCancelled) return false

        activationFlow.start(player, ritualDef, location)
        return true
    }
}
