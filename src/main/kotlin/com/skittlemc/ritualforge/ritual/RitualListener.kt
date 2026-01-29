package com.skittlemc.ritualforge.ritual

import com.skittlemc.ritualforge.RitualForgePlugin
import com.skittlemc.ritualforge.util.Text
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class RitualListener(private val plugin: RitualForgePlugin) : Listener {

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK) return
        val block = event.clickedBlock ?: return
        val player = event.player
        val item = event.item ?: return

        // Check if clicking a shrine core
        val shrineDetector = plugin.ritualService.shrineDetector
        val shrine = shrineDetector.detect(block.location) ?: return

        // Find ritual matching this shrine and catalyst
        val ritual = plugin.contentManager.getAllRitualDefinitions()
            .firstOrNull { it.shrineId == shrine.type && it.catalystItem == item.type }
            ?: return

        event.isCancelled = true

        // Check cooldown, permissions, etc.
        if (!player.hasPermission("ritualforge.use")) {
            player.sendMessage(Text.parse("<red>You don't have permission to activate rituals."))
            return
        }

        // Start the ritual
        val success = plugin.ritualService.tryActivate(player, block.location)
        if (success) {
            player.sendMessage(Text.parse("<gold>Ritual activation begun... Stand still!"))
            // Consume catalyst
            item.amount -= 1
        }
    }
}
