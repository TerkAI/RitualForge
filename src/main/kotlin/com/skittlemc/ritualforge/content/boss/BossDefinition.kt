package com.skittlemc.ritualforge.content.boss

data class BossDefinition(
    val id: String,
    val displayName: String,
    val description: String = "A fearsome boss.",
    val maxHealth: Double,
    val modelId: String,
    val phases: List<PhaseDefinition>,
    val lootTableId: String?,
    val arenaRadius: Double = 30.0
)

data class PhaseDefinition(
    val index: Int,
    val healthThreshold: Double,
    val abilities: List<String>,
    val animation: String? = null
)
