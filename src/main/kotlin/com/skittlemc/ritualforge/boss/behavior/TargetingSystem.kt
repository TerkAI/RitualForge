package com.skittlemc.ritualforge.boss.behavior

import com.skittlemc.ritualforge.boss.BossInstance
import org.bukkit.GameMode
import org.bukkit.entity.Player

class TargetingSystem {

    fun selectTarget(boss: BossInstance): Player? {
        val arena = boss.arena
        val center = arena.center
        val world = center.world ?: return null

        return world.players
            .filter { it.gameMode == GameMode.SURVIVAL || it.gameMode == GameMode.ADVENTURE }
            .filter { arena.contains(it.location) }
            .minByOrNull { it.location.distanceSquared(center) }
    }
}
