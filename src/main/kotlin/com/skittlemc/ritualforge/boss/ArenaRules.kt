package com.skittlemc.ritualforge.boss

import org.bukkit.entity.Player

class ArenaRules(private val arena: Arena) {

    fun enforce(player: Player) {
        if (!arena.contains(player.location)) {
            val clamped = arena.clamp(player.location)
            player.teleport(clamped)
        }
    }

    fun resetArena() {
        // Restore arena blocks to original state if needed
    }
}
