package com.skittlemc.ritualforge.content.ritual

import org.bukkit.Material

data class RitualDefinition(
    val id: String,
    val displayName: String,
    val bossId: String,
    val shrineId: String,
    val catalystItem: Material = Material.NETHER_STAR,
    val channelingTicks: Int = 60,
    val cooldownTicks: Int = 6000
)
