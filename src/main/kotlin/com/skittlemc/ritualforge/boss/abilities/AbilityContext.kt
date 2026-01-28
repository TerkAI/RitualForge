package com.skittlemc.ritualforge.boss.abilities

import com.skittlemc.ritualforge.boss.BossInstance
import org.bukkit.entity.Player

data class AbilityContext(
    val boss: BossInstance,
    val target: Player
)
