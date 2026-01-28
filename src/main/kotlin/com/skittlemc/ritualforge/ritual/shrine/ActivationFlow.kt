package com.skittlemc.ritualforge.ritual.shrine

import com.skittlemc.ritualforge.RitualForgePlugin
import com.skittlemc.ritualforge.content.ritual.RitualDefinition
import com.skittlemc.ritualforge.util.Tasks
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Player

class ActivationFlow(private val plugin: RitualForgePlugin) {

    fun start(player: Player, ritual: RitualDefinition, location: Location) {
        val world = location.world ?: return

        // Channeling phase: particles + sound over duration
        var tick = 0
        Tasks.repeat(plugin, 0L, 1L) { taskId ->
            if (tick >= ritual.channelingTicks) {
                org.bukkit.Bukkit.getScheduler().cancelTask(taskId)
                complete(player, ritual, location)
                return@repeat
            }

            world.spawnParticle(Particle.ENCHANT, location.clone().add(0.5, 1.5, 0.5), 10)
            if (tick % 20 == 0) {
                world.playSound(location, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.0f + tick * 0.01f)
            }
            tick++
        }
    }

    private fun complete(player: Player, ritual: RitualDefinition, location: Location) {
        val bossDef = plugin.contentManager.getBossDefinition(ritual.bossId) ?: return
        val spawnLoc = location.clone().add(0.0, 2.0, 0.0)

        location.world?.playSound(location, Sound.ENTITY_ENDER_DRAGON_GROWL, 2.0f, 0.5f)
        plugin.bossService.spawn(bossDef, spawnLoc)
    }
}
