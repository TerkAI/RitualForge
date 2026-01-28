package com.skittlemc.ritualforge.content.ritual

data class RitualDefinition(
    val id: String,
    val displayName: String,
    val bossId: String,
    val shrineType: String,
    val catalystItem: String,
    val channelingTicks: Int = 60,
    val cooldownTicks: Int = 6000
)
